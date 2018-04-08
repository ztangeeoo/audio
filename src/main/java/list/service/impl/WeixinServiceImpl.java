package list.service.impl;

import list.service.WeixinService;
import list.util.http.RestTemplateUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/3.
 */
@Service
public class WeixinServiceImpl implements WeixinService {
    private static final String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String ACCESS_TICKET_URL="https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
    private static final String ACCESS_QR_URL="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";

    @Value("${gzh.appid}")
    private String appid;
    @Value("${gzh.secret}")
    private String secret;

    @Override
    public void getQR(String bookId,HttpServletResponse response) {
        JSONObject result = RestTemplateUtil
                .excute("GET",  String.format(ACCESS_TOKEN_URL,appid,secret) , null);
        Map<String, String> intMap = new HashMap<>();
        intMap.put("scene_str", bookId);
        Map<String, Map<String, String>> mapMap = new HashMap<>();
        mapMap.put("scene", intMap);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("action_name", "QR_LIMIT_STR_SCENE");
        paramsMap.put("action_info", mapMap);
        JSONObject jsonObject = JSONObject.fromObject(paramsMap);
        //{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "test"}}}
        JSONObject excute = RestTemplateUtil
                .excute("POST", String.format(ACCESS_TICKET_URL, result.getString("access_token")), jsonObject);
        getErWeiMa(String.format(ACCESS_QR_URL,excute.getString("ticket")),bookId,response);
    }
    private void getErWeiMa(String url,String bookId,HttpServletResponse resp)  {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {

            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url ,
                    HttpMethod.GET,
                    new HttpEntity<byte[]>(headers),
                    byte[].class);

            byte[] result = response.getBody();

            inputStream = new ByteArrayInputStream(result);

            //File file = new File("E://"+bookId+".jpg");

       /*     if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
            try {
                outputStream =  resp.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int len = 0;
            byte[] buf = new byte[1024];
            try {
                while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                    try {
                        outputStream.write(buf, 0, len);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    }


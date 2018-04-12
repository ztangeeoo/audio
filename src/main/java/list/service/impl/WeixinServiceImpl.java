package list.service.impl;

import com.thoughtworks.xstream.XStream;
import list.constant.Constant;
import list.dto.Article;
import list.dto.NewsMessage;
import list.dto.TestMessageDTO;
import list.service.WeixinService;
import list.util.ConvertUtil;
import list.util.http.RestTemplateUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/3.
 */
@Service
public class WeixinServiceImpl implements WeixinService {
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String ACCESS_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
    private static final String ACCESS_QR_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";

    @Value("${gzh.appid}")
    private String appid;
    @Value("${gzh.secret}")
    private String secret;

    @Override
    public void getQR(String bookId, HttpServletResponse response) {
        JSONObject result = RestTemplateUtil
                .excute("GET", String.format(ACCESS_TOKEN_URL, appid, secret), null,MediaType.APPLICATION_JSON_UTF8);
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
                .excute("POST", String.format(ACCESS_TICKET_URL, result.getString("access_token")), jsonObject,MediaType.APPLICATION_JSON_UTF8);
        getErWeiMa(String.format(ACCESS_QR_URL, excute.getString("ticket")), bookId, response);
    }

    @Override
    public Object sendTextMessage(Map<String, String> map) {
        String fromUserName = map.get("FromUserName");
        String toUserName = map.get("ToUserName");

        TestMessageDTO message = new TestMessageDTO();
        message.setToUserName(fromUserName);
        message.setFromUserName(toUserName);
        message.setMsgType("text");
        message.setCreateTime(new Date().getTime());
        message.setContent("欢迎欢迎！");
        //logger.info("系统返回给用户的参数，str={}", "");
        return ConvertUtil.objectToXml(message);
    }

    @Override
    public Object sendEventMessage(Map<String, String> map) {
        String fromUserName = map.get("FromUserName");
        String toUserName = map.get("ToUserName");

        String event = map.get("Event");
        String eventKey = map.get("EventKey");
        if (StringUtils.isEmpty(eventKey)) {
            TestMessageDTO message = new TestMessageDTO();
            message.setToUserName(fromUserName);
            message.setFromUserName(toUserName);
            message.setMsgType("text");
            message.setCreateTime(new Date().getTime());
            message.setContent("谢谢关注！");
            return ConvertUtil.objectToXml(message);
        } else {
            String s = null;
            if (event.equals(Constant.EVENT_TYPE_SUBSCRIBE)) {
                 s = eventKey.substring(8);

            } else if (event.equals(Constant.EVENT_TYPE_SCAN)) {
                 s = eventKey;
            }
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setToUserName(fromUserName);
            newsMessage.setFromUserName(toUserName);
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setMsgType(Constant.RESP_MESSAGE_TYPE_NEWS);
            ArrayList<Article> list = new ArrayList<>();
            Article article = new Article();
            article.setTitle("书名");
            article.setDescription("什么书");
            article.setPicUrl("https://audiolist.oss-cn-shenzhen.aliyuncs.com/images.jpg?Expires=1523199516&OSSAccessKeyId=TMP.AQG-OV2y0K-N-tqOBUjFAOPVTN7K5_fRlD3_ZK5-j0lCUj2SkEcjUysONX5MADAtAhUAv1Py9E1xuHsFCsNkWOPyjpexsuACFDr6kkrU7fSXAc_IIyX4bbU84rJW&Signature=SGzxrrqDzemmsJhhDjQDgo6o8pI%3D");
            article.setUrl("http://119.29.177.27:80/audio/getAudioList/" + s);
            list.add(article);
            newsMessage.setArticleCount(list.size());
            newsMessage.setArticles(list);
            XStream xs = new XStream();
            xs.alias("xml", newsMessage.getClass());
            xs.alias("item", new Article().getClass());
            return xs.toXML(newsMessage);
        }
    }



    private void getErWeiMa(String url, String bookId, HttpServletResponse resp) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {

            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
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
                outputStream = resp.getOutputStream();
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


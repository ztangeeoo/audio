package list.util.http;

import list.dto.ResultEnum;
import list.exception.AudioException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateUtil implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);
    private static RestTemplate restTemplate;
    @Autowired
    private RestTemplate template;

    private RestTemplateUtil() {
    }

    public static void setRestTemplate(RestTemplate restTemplate) {
        RestTemplateUtil.restTemplate = restTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRestTemplate(template);
    }

    public static JSONObject excute(String method, String url, JSONObject params,MediaType mediaType) {
        JSONObject  result = new JSONObject();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            HttpEntity<JSONObject> entity = null;
            if (params != null) {
                entity = new HttpEntity<>(params, headers);
            }
            switch (method) {
                case "GET":
                    result = restTemplate.exchange(url, HttpMethod.GET, entity, JSONObject.class).getBody();
                    break;
                case "POST":
                    result = restTemplate.exchange(url, HttpMethod.POST, entity, JSONObject.class).getBody();
                    break;
                case "PUT":
                    result = restTemplate.exchange(url, HttpMethod.PUT, entity, JSONObject.class).getBody();
                    break;
                case "PATCH":
                    result = restTemplate.exchange(url, HttpMethod.PATCH, entity, JSONObject.class).getBody();
                    break;
                case "DELETE":
                    result = restTemplate.exchange(url, HttpMethod.DELETE, entity, JSONObject.class).getBody();
                    break;
                default:
                    break;
            }
            if (logger.isInfoEnabled()) {
                logger.info("RestTemplateUtil执行成功, url = [{}] {}, params = {}, responseBody = {}",
                        method, url, params, result==null?null:result.toString());
            }
            return result;
        } catch (HttpClientErrorException e) {
            logger.error("RestTemplateUtil HttpClientErrorException, url = [{}] {}, params = {}, detail = {}",
                    method, url, params, e.getMessage());
        } catch (RestClientException e) {
            logger.error("RestTemplateUtil RestClientException, url = [{}] {}, params = {}, detail = {}",
                    method, url, params, e.getMessage());
        }
        logger.error("RestTemplate执行报错");
        throw new AudioException(ResultEnum.RC_0401001);
    }
}

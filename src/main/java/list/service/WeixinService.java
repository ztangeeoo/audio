package list.service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/3.
 */
public interface WeixinService {



    Object sendTextMessage(Map<String,String> map);

    Object sendEventMessage(Map<String,String> map);


}

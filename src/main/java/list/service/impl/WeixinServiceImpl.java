package list.service.impl;

import com.thoughtworks.xstream.XStream;
import list.constant.Constant;
import list.dao.BookInfoRepository;
import list.dto.Article;
import list.dto.NewsMessage;
import list.dto.TestMessageDTO;
import list.entity.BookInfo;
import list.service.WeixinService;
import list.util.ConvertUtil;
import list.util.UploadFileUtil;
import list.util.http.RestTemplateUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/3.
 */
@Service
public class WeixinServiceImpl implements WeixinService {


    @Value("${gzh.appid}")
    private String appid;
    @Value("${gzh.secret}")
    private String secret;

    @Autowired
    private BookInfoRepository bookInfoRepository;



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



}


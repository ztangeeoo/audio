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
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/3.
 */
@Service
public class WeixinServiceImpl implements WeixinService {
    private static final Logger logger = LoggerFactory.getLogger(WeixinServiceImpl.class);


    @Value("${gzh.appid}")
    private String appid;
    @Value("${gzh.secret}")
    private String secret;
    @Value("${WX_SERVER_URL}")
    private String baseUrl;
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
            BookInfo one = bookInfoRepository.findOne(s);
            NewsMessage newsMessage = new NewsMessage();
            newsMessage.setToUserName(fromUserName);
            newsMessage.setFromUserName(toUserName);
            newsMessage.setCreateTime(new Date().getTime());
            newsMessage.setMsgType(Constant.RESP_MESSAGE_TYPE_NEWS);
            ArrayList<Article> list = new ArrayList<>();
            Article article = new Article();
            article.setTitle(one.getBookName());
            article.setDescription(one.getBookDescription());
            article.setPicUrl(one.getBookCover());
            article.setUrl(baseUrl + s);
            logger.info("文章内容:{}",ToStringBuilder.reflectionToString(article));
            list.add(article);
            newsMessage.setArticleCount(list.size());
            newsMessage.setArticles(list);
            XStream xs = new XStream();
            logger.info("图文数据:{}", ToStringBuilder.reflectionToString(newsMessage));
            xs.alias("xml", newsMessage.getClass());
            xs.alias("item", new Article().getClass());
            return xs.toXML(newsMessage);
        }
    }


}


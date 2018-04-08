package list.controller;

import com.thoughtworks.xstream.XStream;
import list.constant.Constant;
import list.dto.Article;
import list.dto.NewsMessage;
import list.dto.ResultUtil;
import list.dto.TestMessageDTO;
import list.service.WeixinService;
import list.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/30.
 */
@RestController
public class WXController {
    private static final Logger logger = LoggerFactory.getLogger(WXController.class);

    @Autowired
    private WeixinService weixinService;

    @GetMapping(value = "ztang/tvlist")
    public Object listAudio(HttpServletRequest request) {

        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        String echostr = request.getParameter("echostr");
        return echostr;

    }

    @PostMapping(value = "ztang/tvlist")
    public Object listTv(HttpServletRequest request) {
        Map<String, String> map = ConvertUtil.xmlToMap(request);
        String msgType = map.get("MsgType");
        logger.info("用户访问的参数：map={}", map);

        if (Constant.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {
            return sendTextMessage(map);
        } else if (Constant.REQ_MESSAGE_TYPE_EVENT.equals(msgType)) {
            return sendEventMessage(map);
        }
        return null;
    }

    @PostMapping("/lixue/getQR")
    public Object getQR(@RequestParam String bookId, HttpServletResponse response){
         weixinService.getQR(bookId,response);
        return ResultUtil.success("二维码生成成功！");

    }

    public Object sendTextMessage(Map<String, String> map) {
        String fromUserName = map.get("FromUserName");
        String toUserName = map.get("ToUserName");

        TestMessageDTO message = new TestMessageDTO();
        message.setToUserName(fromUserName);
        message.setFromUserName(toUserName);
        message.setMsgType("text");
        message.setCreateTime(new Date().getTime());
        message.setContent("傻逼吧你！");
        logger.info("系统返回给用户的参数，str={}", "");
        return ConvertUtil.objectToXml(message);
    }

    public Object sendEventMessage(Map<String, String> map) {
        String fromUserName = map.get("FromUserName");
        String toUserName = map.get("ToUserName");

        String event = map.get("Event");
        if (event.equals(Constant.EVENT_TYPE_SUBSCRIBE)) {
            String eventKey = map.get("EventKey");
            if (StringUtils.isEmpty(eventKey)) {
                TestMessageDTO message = new TestMessageDTO();
                message.setToUserName(fromUserName);
                message.setFromUserName(toUserName);
                message.setMsgType("text");
                message.setCreateTime(new Date().getTime());
                message.setContent("谢谢关注！");
                return ConvertUtil.objectToXml(message);
            }else{
                 String s = eventKey.substring(8);
                /*     message.setContent("点击播放本书的音频:http://119.29.177.27:80/audio/getAudioList/" + s);*/
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
    }else {
        return null;
        }
    }
}

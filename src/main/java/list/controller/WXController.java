package list.controller;

import list.constant.Constant;
import list.dto.ResultUtil;
import list.service.WeixinService;
import list.util.ConvertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/30.
 */
@RestController
public class WXController {
    private static final Logger logger = LoggerFactory.getLogger(WXController.class);

    @Autowired
    private WeixinService weixinService;

    @GetMapping(value = "lixue/tvlist")
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

    @PostMapping(value = "lixue/tvlist")
    public Object listTv(HttpServletRequest request) {
        Map<String, String> map = ConvertUtil.xmlToMap(request);
        String msgType = map.get("MsgType");
        logger.info("用户访问的参数：map={}", map);

        if (Constant.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {
            return weixinService.sendTextMessage(map);
        } else if (Constant.REQ_MESSAGE_TYPE_EVENT.equals(msgType)) {
            return weixinService.sendEventMessage(map);
        }
        return null;
    }


}

package list.controller;

import list.dao.UserInfoRepository;
import list.entity.UserInfo;
import list.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ztang
 * @date 9:54 2018/4/4
 */
@RestController
public class LoginContoller {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @PostMapping(value = "login")
    public ModelAndView login(HttpServletRequest request) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        UserInfo userInfo = userInfoRepository.findByName(name);
        if (userInfo!=null && userInfo.getPassword().equals(MD5Util.getMD5(password))) {
            request.getSession().setAttribute("user",userInfo);
            return new ModelAndView("/lixue/qr");
        } else {
            return new ModelAndView("/login");
        }
    }
}

package list.controller;

import list.dao.UserInfoRepository;
import list.entity.UserInfo;
import list.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ztang
 * @date 9:54 2018/4/4
 */
@RestController
public class LoginContoller {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @PostMapping(value = "login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        UserInfo userInfo = userInfoRepository.findByName(name);
        if (userInfo != null && userInfo.getPassword().equals(MD5Util.getMD5(password))) {
            request.getSession().setAttribute("user", userInfo);
            response.sendRedirect("/audio/homeList?pageNumber=1&pageSize=10");
        } else {
            response.sendRedirect("loginout");
        }
    }
    @GetMapping(value = "loginout")
    public ModelAndView loginout(HttpServletRequest request){
        request.getSession().invalidate();
        request.getSession().removeAttribute("user");
        return new ModelAndView("/login");
    }

}

package list.util;

import list.dao.UserInfoRepository;
import list.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ztang
 * @date 10:42 2018/4/4
 */
@Component
public class InitUtil {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @PostConstruct
    public void init(){
        UserInfo userInfo = new UserInfo();
        userInfo.setName("admin");
        userInfo.setPassword(MD5Util.getMD5("admin"));
        userInfoRepository.save(userInfo);

    }
}

package list.service;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/4/3.
 */
public interface WeixinService {

    void getQR(String bookId, HttpServletResponse response);
}

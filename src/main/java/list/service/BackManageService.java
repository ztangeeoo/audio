package list.service;

import list.dto.PageDTO;
import list.entity.BookInfo;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ztang
 * @date 12:56 2018/3/28
 */
public interface BackManageService {
    /**
     * 文件上传
     */
    void addVideo(MultipartFile file, String bookId,String fileTime);

    BookInfo getAudioList(String bookId);

    void addBook(HttpServletRequest request,MultipartFile bookCover);

    List<BookInfo> findListBook(PageDTO pageDTO);




}

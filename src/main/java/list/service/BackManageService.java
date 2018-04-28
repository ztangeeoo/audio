package list.service;

import list.dto.PageDTO;
import list.entity.BookInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    void addVideo(MultipartFile file, String bookId, String fileTime);

    BookInfo getAudioList(String bookId);

    void addBook(HttpServletRequest request, MultipartFile bookCover);

    List<BookInfo> findListBook(PageDTO pageDTO);

    void changeBook(HttpServletRequest request, MultipartFile bookCover);

    BookInfo findby(String bookId);

    void removeBook(String bookId);

    void removeVideo(String bookId, String audioId);

    ArrayList<Integer> countPage(PageDTO pageDTO);

    ArrayList<Integer> countPageByBook(PageDTO pageDTO, String bookName);

    String getQR(String bookId, HttpServletResponse response);

    List<BookInfo> findBooks(String bookName, PageDTO pageDTO);

    void deleteAll();


}

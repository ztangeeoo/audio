package list.service;

import list.entity.BookInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

/**
 * @author ztang
 * @date 12:56 2018/3/28
 */
public interface FileService {
    /**
     * 文件上传
     */
    void Upload(MultipartFile file, String bookId);

    BookInfo getAudioList(String bookId);




}

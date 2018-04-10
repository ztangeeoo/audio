package list.service.impl;

import list.dao.BookInfoRepository;
import list.dto.PageDTO;
import list.entity.AudioInfo;
import list.entity.BookInfo;
import list.service.BackManageService;
import list.util.UploadFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ztang
 * @date 12:58 2018/3/28
 */
@Service
public class BackManageServiceImpl implements BackManageService {

    @Value("${gzh.appid}")
    private String appid;
    @Value("${gzh.secret}")
    private String secret;

    @Autowired
    private BookInfoRepository bookInfoRepository;


    @Override
    public void Upload(MultipartFile file, String bookId) {
        String fileName = file.getOriginalFilename();
        String url = UploadFileUtil.upload(file);
        BookInfo info = bookInfoRepository.findOne(bookId);
        if (info == null) {
            BookInfo bookInfo = new BookInfo();
            bookInfo.setBookId(bookId);
            ArrayList<AudioInfo> list = new ArrayList<>();
            AudioInfo audioInfo = new AudioInfo();
            audioInfo.setFileName(fileName);
            audioInfo.setFileUrl(url.toString());
            list.add(audioInfo);
            bookInfo.setAudioInfoList(list);
            bookInfo.setCreateAt(new Date());
            bookInfoRepository.save(bookInfo);
        } else {
            AudioInfo ai = new AudioInfo();
            ai.setFileName(fileName);
            ai.setFileUrl(url.toString());
            info.getAudioInfoList().add(ai);
            bookInfoRepository.save(info);
        }
    }

    @Override
    public BookInfo getAudioList(String bookId) {
        return bookInfoRepository.findOne(bookId);
    }

    @Override
    public void addBook(HttpServletRequest request,MultipartFile bookCover) {
        String bookDescription =  request.getParameter("bookDescription");
        String bookName = request.getParameter("bookName");
        String bookId = request.getParameter("bookId");
        String bookImage = UploadFileUtil.upload(bookCover);
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookCover(bookImage);
        bookInfo.setBookDescription(bookDescription);
        bookInfo.setBookId(bookId);
        bookInfo.setBookName(bookName);
        bookInfo.setCreateAt(new Date());
        bookInfoRepository.save(bookInfo);
    }

    @Override
    public Page<BookInfo> findListBook(PageDTO params) {
        int pageSize = params.getPageSize() == 0 ? 15 : params.getPageSize();
        Pageable pageable = new PageRequest(params.getPageNum() - 1, pageSize);
        return bookInfoRepository.findAllByOrderByCreateAtDesc(pageable);

    }
}

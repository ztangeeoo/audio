package list.service.impl;

import list.dao.BookInfoRepository;
import list.dto.PageDTO;
import list.entity.AudioInfo;
import list.entity.BookInfo;
import list.service.BackManageService;
import list.util.UploadFileUtil;
import list.util.http.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void addVideo(MultipartFile file, String bookId, String fileTime) {
        String fileName = file.getOriginalFilename();
        String url = UploadFileUtil.upload(file);
        BookInfo bookInfo = bookInfoRepository.findOne(bookId);

        ArrayList<AudioInfo> list = new ArrayList<>();
        AudioInfo audioInfo = new AudioInfo();
        audioInfo.setAudioId(UUID.randomUUID().toString().replace("-", "").toUpperCase());
        audioInfo.setFileTime(fileTime);
        audioInfo.setFileName(fileName);
        audioInfo.setFileUrl(url);
        list.add(audioInfo);
        bookInfo.setAudioInfoList(list);
        bookInfoRepository.save(bookInfo);
        RestTemplateUtil.excute("GET","http://localhost:8004/audio/videoList?bookId="+bookId,null, MediaType.TEXT_HTML);
    }

    @Override
    public BookInfo getAudioList(String bookId) {
        return bookInfoRepository.findOne(bookId);
    }

    @Override
    public void addBook(HttpServletRequest request, MultipartFile bookCover) {
        String bookDescription = request.getParameter("bookDescription");
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
    public List<BookInfo> findListBook(PageDTO params) {
        Query query = new Query();
        query.limit(params.getPageSize());

        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createAt");
        Sort orders = new Sort(order);
        query.with(orders);
        return mongoTemplate.find(query, BookInfo.class);
    }
}

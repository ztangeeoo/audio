package list.service.impl;

import list.dao.BookInfoRepository;
import list.dto.PageDTO;
import list.entity.AudioInfo;
import list.entity.BookInfo;
import list.service.BackManageService;
import list.util.UploadFileUtil;
import list.util.http.RestTemplateUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author ztang
 * @date 12:58 2018/3/28
 */
@Service
public class BackManageServiceImpl implements BackManageService {
    private static final Logger logger = LoggerFactory.getLogger(BackManageServiceImpl.class);

    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
    private static final String ACCESS_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
    private static final String ACCESS_QR_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";

    @Value("${gzh.appid}")
    private String appid;
    @Value("${gzh.secret}")
    private String secret;

    @Autowired
    private BookInfoRepository bookInfoRepository;
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public void addVideo(MultipartFile[] files, String bookId, String fileTime) {
        MultipartFile file = null;
        BookInfo bookInfo = bookInfoRepository.findOne(bookId);
        List<AudioInfo> list = bookInfo.getAudioInfoList();
        if (ObjectUtils.isEmpty(list)) {
            list = new ArrayList<>();
        }
        for (int i = 0; i < files.length; i++) {
            file = files[i];
            if (file != null) {
                String fileName = file.getOriginalFilename();
                String url = UploadFileUtil.uploadFile(file);
                AudioInfo audioInfo = new AudioInfo();
                audioInfo.setAudioId(UUID.randomUUID().toString().replace("-", "").toUpperCase());
                audioInfo.setFileTime(fileTime);
                audioInfo.setFileName(fileName);
                audioInfo.setFileUrl(url);
                list.add(audioInfo);
            }
        }
        bookInfo.setAudioInfoList(list);
        bookInfoRepository.save(bookInfo);
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
        String bookImage = UploadFileUtil.uploadFile(bookCover);
        BookInfo bookInfo = new BookInfo();
        bookInfo.setBookCover(bookImage);
        bookInfo.setBookDescription(bookDescription);
        bookInfo.setBookId(bookId);
        bookInfo.setBookName(bookName);
        bookInfo.setCreateAt(new Date());
        bookInfoRepository.save(bookInfo);
    }


    @Override
    public void changeBook(HttpServletRequest request, MultipartFile bookCover) {
        String bookDescription = request.getParameter("bookDescription");
        String bookName = request.getParameter("bookName");
        String bookId = request.getParameter("bookId");
        BookInfo bookInfo = bookInfoRepository.findOne(bookId);
        if (!bookCover.isEmpty()) {
            String bookImage = UploadFileUtil.uploadFile(bookCover);
            bookInfo.setBookCover(bookImage);
        }
        bookInfo.setBookDescription(bookDescription);
        bookInfo.setBookName(bookName);
        bookInfo.setCreateAt(new Date());
        bookInfoRepository.save(bookInfo);
    }

    @Override
    public List<BookInfo> findListBook(PageDTO params) {

        int pageSize = params.getPageSize();
        Query query = new Query();
        query.skip(pageSize * (params.getPageNum() - 1));
        query.limit(pageSize);

        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createAt");
        Sort orders = new Sort(order);
        query.with(orders);
        return mongoTemplate.find(query, BookInfo.class);
    }


    @Override
    public BookInfo findby(String bookId) {
        return bookInfoRepository.findOne(bookId);
    }

    @Override
    public void removeBook(String bookId) {
        bookInfoRepository.delete(bookId);
    }

    @Override
    public void removeVideo(String bookId, String audioId) {
        BookInfo one = bookInfoRepository.findOne(bookId);
        List<AudioInfo> audioInfoList = one.getAudioInfoList();
        Iterator<AudioInfo> iterator = audioInfoList.iterator();
        while (iterator.hasNext()) {
            AudioInfo next = iterator.next();
            if (audioId.equals(next.getAudioId())) {
                iterator.remove();
            }
        }
        one.setAudioInfoList(audioInfoList);
        bookInfoRepository.save(one);
    }

    @Override
    public ArrayList<Integer> countPage(PageDTO pageDTO) {
        ArrayList<Integer> pageList = new ArrayList<>();
        long count = bookInfoRepository.count();
        if (count > 0) {
            int page = (int) ((count + pageDTO.getPageSize() - 1) / pageDTO.getPageSize());
            for (int i = 1; i <= page; i++) {
                pageList.add(i);
            }
        }
        return pageList;
    }

    @Override
    public ArrayList<Integer> countPageByBook(PageDTO pageDTO, String bookName) {
        long count = bookInfoRepository.countByBookNameContains(bookName);
        ArrayList<Integer> pageList = new ArrayList<>();
        if (count > 0) {
            int page = (int) ((count + pageDTO.getPageSize() - 1) / pageDTO.getPageSize());
            for (int i = 1; i <= page; i++) {
                pageList.add(i);
            }
        }
        return pageList;
    }


    @Override
    public String getQR(String bookId, HttpServletResponse response) {
        JSONObject result = RestTemplateUtil
                .excute("GET", String.format(ACCESS_TOKEN_URL, appid, secret), null, MediaType.APPLICATION_JSON_UTF8);
        logger.debug("获取token={}", result.toString());
        Map<String, String> intMap = new HashMap<>();
        intMap.put("scene_str", bookId);
        Map<String, Map<String, String>> mapMap = new HashMap<>();
        mapMap.put("scene", intMap);
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("action_name", "QR_LIMIT_STR_SCENE");
        paramsMap.put("action_info", mapMap);
        JSONObject jsonObject = JSONObject.fromObject(paramsMap);
        //{"action_name": "QR_LIMIT_STR_SCENE", "action_info": {"scene": {"scene_str": "test"}}}
        JSONObject excute = RestTemplateUtil
                .excute("POST", String.format(ACCESS_TICKET_URL, result.getString("access_token")), jsonObject, MediaType.APPLICATION_JSON_UTF8);
        logger.debug("获取ticket={}", excute.toString());
        String qrCode = getErWeiMa(String.format(ACCESS_QR_URL, excute.getString("ticket")), bookId);
        BookInfo one = bookInfoRepository.findOne(bookId);
        one.setQrCode(qrCode);
        bookInfoRepository.save(one);
        return qrCode;
    }

    @Override
    public List<BookInfo> findBooks(String bookName, PageDTO params) {
        int pageSize = params.getPageSize();
        Query query = new Query();
        query.skip(pageSize * (params.getPageNum() - 1));
        query.limit(pageSize);
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createAt");
        Sort orders = new Sort(order);
        query.addCriteria(Criteria.where("bookName").regex(bookName)).with(orders);
        return mongoTemplate.find(query, BookInfo.class);
    }

    @Override
    public void deleteAll() {
        bookInfoRepository.deleteAll();
    }

    private String getErWeiMa(String url, String bookId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity<byte[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<byte[]>(headers),
                byte[].class);

        byte[] result = response.getBody();
        logger.debug("获取二维码,response={}", response.getStatusCode());
        return UploadFileUtil.uploadImage(result, bookId);
    }
}

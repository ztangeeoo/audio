package list.service.impl;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.ObjectMetadata;
import list.dao.FileInfoRepository;
import list.dto.ResultEnum;
import list.entity.AudioInfo;
import list.entity.BookInfo;
import list.exception.AudioException;
import list.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author ztang
 * @date 12:58 2018/3/28
 */
@Service
public class FileServiceImpl implements FileService {
    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${gzh.appid}")
    private String appid;
    @Value("${gzh.secret}")
    private String secret;

    @Autowired
    private FileInfoRepository fileInfoRepository;


    @Override
    public void Upload(MultipartFile file, String bookId) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        OSSClient ossClient = new OSSClient(endpoint, new DefaultCredentialProvider(accessKeyId, accessKeySecret), null);
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        URL url = null;
        // meta.setContentLength(file.length());
        try {
            String md5 = BinaryUtil.toBase64String(BinaryUtil.calculateMd5(file.getBytes()));
            meta.setContentMD5(md5);
            meta.setContentType(contentType);
            ossClient.putObject("audiolist", fileName, new ByteArrayInputStream(file.getBytes()), meta);
            Date expiration = new Date(new Date().getTime() + 100*3600 * 1000);// 生成URL
            url = ossClient.generatePresignedUrl("audiolist", fileName, expiration);
        } catch (IOException e) {
            throw new AudioException(ResultEnum.RC_0401001);
        } finally {
            ossClient.shutdown();
        }
        BookInfo info = fileInfoRepository.findOne(bookId);
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
            fileInfoRepository.save(bookInfo);
        } else {
            AudioInfo ai = new AudioInfo();
            ai.setFileName(fileName);
            ai.setFileUrl(url.toString());
            info.getAudioInfoList().add(ai);
            fileInfoRepository.save(info);
        }
    }

    @Override
    public BookInfo getAudioList(String bookId) {
        return fileInfoRepository.findOne(bookId);
    }

}

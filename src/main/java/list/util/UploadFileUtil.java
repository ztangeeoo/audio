package list.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.ObjectMetadata;
import list.dto.ResultEnum;
import list.exception.AudioException;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.Random;

/**
 * @author ztang
 * @date 16:41 2018/4/10
 */
@Component
public class UploadFileUtil implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(UploadFileUtil.class);

    private static String ep;
    private static String ak;
    private static String aks;
    private static String bn;
    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${oss.bucketName}")
    private String bucketName;

    @Override
    public void afterPropertiesSet() throws Exception {
        ep = endpoint;
        ak = accessKeyId;
        aks = accessKeySecret;
        bn = bucketName;
    }

    public static String uploadFile(MultipartFile file) {
        String contentType = file.getContentType();
        long time = new Date().getTime();
        String fileName = String.valueOf(time);
        logger.debug("创建oss对象前的时间，time={}",System.currentTimeMillis());
        OSSClient ossClient = new OSSClient(ep, new DefaultCredentialProvider(ak, aks), null);
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        logger.debug("创建oss对象后的时间，time={}",System.currentTimeMillis());
        try {
            meta.setContentType(contentType);
            logger.debug("上传文件前的时间，time={}",System.currentTimeMillis());
            ossClient.putObject(bn, fileName, file.getInputStream(), meta);
            logger.debug("上传文件后的时间，time={}",System.currentTimeMillis());
        } catch (IOException e) {
            throw new AudioException(ResultEnum.RC_0401001);
        }
        logger.debug("关闭oss流前的时间，time={}",System.currentTimeMillis());
        ossClient.shutdown();
        logger.debug("关闭oss流后的时间，time={}",System.currentTimeMillis());
        return String.format("https://%s.oss-cn-shenzhen.aliyuncs.com/%s", bn, fileName);
    }


    public static String uploadImage(byte[] bytes, String bookId) {
        ObjectMetadata meta = new ObjectMetadata();
        String fileName = bookId + ".jpg";
        meta.setContentType(MediaType.IMAGE_JPEG_VALUE);
        OSSClient ossClient = new OSSClient(ep,
                new DefaultCredentialProvider(ak, aks), null);
        logger.debug("开始上传图片!fileName={}", fileName);
        ossClient.putObject(bn, fileName, PictureUtil.insertLogo(bytes), meta);
        ossClient.shutdown();
        logger.debug("上传文件完成!");
        return String.format("https://%s.oss-cn-shenzhen.aliyuncs.com/%s", bn, fileName);
    }

    public static String uploadBookCover(InputStream inputStream, String fileName) {
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(MediaType.IMAGE_JPEG_VALUE);
        OSSClient ossClient = new OSSClient(ep,
                new DefaultCredentialProvider(ak, aks), null);
        logger.debug("开始上传图片!fileName={}", fileName);
        ossClient.putObject(bn, fileName, inputStream, meta);
        ossClient.shutdown();
        logger.debug("上传文件完成!");
        return String.format("https://%s.oss-cn-shenzhen.aliyuncs.com/%s", bn, fileName);
    }


    public static void main(String[] args) {
        long time = new Date().getTime();
        System.out.println(time);
        int fileId = (int) (100 + Math.random() * 10000000);
        String fileName = String.valueOf(fileId);
        System.out.println(fileName);
        byte[] buffer = null;
        try {
            File file = new File("G:\\PlayAudio/1.jpg");
            ByteArrayOutputStream bos;
            try (FileInputStream fis = new FileInputStream(file)) {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                fis.close();
            }
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType(MediaType.IMAGE_JPEG_VALUE);
        OSSClient ossClient = new OSSClient("http://oss-cn-shenzhen.aliyuncs.com",
                new DefaultCredentialProvider("LTAIKXdZgQKa0RT9", "Ohci0Ycs3NnXJGCRRPhcGFkXaDnIZ9"), null);
        ossClient.putObject("audiocss", "6.jgp", new ByteArrayInputStream(buffer), meta);

        System.out.println("https://audiocss.oss-cn-shenzhen.aliyuncs.com/" + "6.jgp");

    }
}

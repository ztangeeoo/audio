package list.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.ObjectMetadata;
import list.dto.ResultEnum;
import list.exception.AudioException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * @author ztang
 * @date 16:41 2018/4/10
 */
@Component
public class UploadFileUtil implements InitializingBean {

    private static String ep;
    private static String ak;
    private static String aks;
    @Value("${oss.endpoint}")
    private String endpoint;
    @Value("${oss.accessKeyId}")
    private String accessKeyId;
    @Value("${oss.accessKeySecret}")
    private String accessKeySecret;

    @Override
    public void afterPropertiesSet() throws Exception {
        ep=endpoint;
        ak=accessKeyId;
        aks=accessKeySecret;
    }
    public static String upload(MultipartFile file){
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        OSSClient ossClient = new OSSClient(ep, new DefaultCredentialProvider(ak, aks), null);
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
        return url.toString();
    }
}

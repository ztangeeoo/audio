package list.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Random;


/**
 * @author ztang
 * @date 14:22 2018/4/25
 */
@Component
public class PictureUtil implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(PictureUtil.class);


    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 430;
    // LOGO宽度
    private static final int WIDTH = 86;
    // LOGO高度
    private static final int HEIGHT = 86;

    private static String LOGO_URL;

    @Value("${logo.url}")
    private String logoUrl;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGO_URL = logoUrl;
    }


    /**
     * 插入LOGO
     *
     * @param source       二维码图片
     * @param imgPath      LOGO图片地址
     * @param needCompress 是否压缩
     * @throws Exception
     */
    private static BufferedImage insertImage(BufferedImage source, String imgPath,
                                             boolean needCompress) throws Exception {

        Image src = ImageIO.read(new URL(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > WIDTH) {
                width = WIDTH;
            }
            if (height > HEIGHT) {
                height = HEIGHT;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = tag.createGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        BufferedImage bi = new BufferedImage(QRCODE_SIZE, QRCODE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D graph = bi.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(source, 0, 0, QRCODE_SIZE, QRCODE_SIZE, null);
        graph.drawImage(src, x, y, null);
        graph.dispose();
        bi.flush();
        source.flush();
        return bi;
    }

    public static ByteArrayInputStream compressPic(MultipartFile file, int width, int height) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            Image src = ImageIO.read(file.getInputStream());
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = tag.createGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            src = image;

            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graph = bi.createGraphics();
            graph.drawImage(src, 0, 0, width, height, null);

            bi.flush();
            graph.dispose();
            g.dispose();
            image.flush();
            src.flush();
            ImageIO.write(bi, FORMAT_NAME, bos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parse(bos);
    }


    public static ByteArrayInputStream insertLogo(byte[] bytes) {
        BufferedImage bufferedImage = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        // File file = new File("G:\\PlayAudio/hehe.jpg");
        try {
            bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
            BufferedImage bi = PictureUtil.insertImage(bufferedImage, LOGO_URL, true);
            ImageIO.write(bi, FORMAT_NAME, bos);
        } catch (Exception e) {
            logger.error("插入logo失败！", e.getMessage(), e);
        }
        return parse(bos);
    }

    public static ByteArrayInputStream parse(OutputStream out) {
        ByteArrayOutputStream baos = (ByteArrayOutputStream) out;
        ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
        return swapStream;
    }

    public static void main(String[] args) throws Exception {
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream("G:\\PlayAudio/logo (2).jpg"));
        // 插入图片
        BufferedImage image = PictureUtil.insertImage(bufferedImage, "https://audiocss.oss-cn-shenzhen.aliyuncs.com/TIM%E5%9B%BE%E7%89%8720180502135457.png", true);
        String file = new Random().nextInt(99999999) + ".jpg";
        System.out.println(file);
        ImageIO.write(image, "jpg", new File("G:\\PlayAudio/" + file));
      /*  BufferedImage im = ImageIO.read(new FileInputStream("G:\\PlayAudio/6066903.jpg"));
        BufferedImage source = ImageIO.read(new FileInputStream("G:\\PlayAudio/123132.jpg"));
        BufferedImage bi = new BufferedImage(430, 430, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        Shape shape = new RoundRectangle2D.Float(172, 172, 86, 86, 6, 6);
        g.setStroke(new BasicStroke(3f));
        g.draw(shape);
        g.drawImage(source, 0, 0, 430, 430, null);
        g.drawImage(im, 172, 172, 86, 86, null);
        g.dispose();
        im.flush();
        source.flush();


        String file = new Random().nextInt(99999999) + ".jpg";
        System.out.println(file);
        ImageIO.write(bi, "jpg", new File("G:\\PlayAudio/" + file));*/


    }


}

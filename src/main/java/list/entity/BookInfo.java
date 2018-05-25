package list.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

/**
 * @author ztang
 * @date 13:00 2018/3/28
 */
@Document(collection = "book_info")
public class BookInfo {
    //书名 文件名 文件url  创建时间
    @Id
    private String bookId;
    @Field("booK_name")
    private String bookName;
    @Field("book_cover")
    private String bookCover;
    @Field
    private String bookDescription;
    @Field("audio_list")
    private List<AudioInfo> audioInfoList;
    @Field("qr_code")
    private String qrCode;

    /********卖了多少本***************/
/*    private String saleQuantity;

    private String bookHits;*/


    @Field("create_at")
    private Date createAt;


    public BookInfo() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public List<AudioInfo> getAudioInfoList() {
        return audioInfoList;
    }

    public void setAudioInfoList(List<AudioInfo> audioInfoList) {
        this.audioInfoList = audioInfoList;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }


}

package list.entity;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Created by Administrator on 2018/4/8.
 */
public class AudioInfo {
    @Field("audio_id")
    private String audioId;
    @Field("file_name")
    private String fileName;

    @Field
    private String fileTime;

    @Field("file_url")
    private String fileUrl;


    public String getAudioId() {
        return audioId;
    }

    public void setAudioId(String audioId) {
        this.audioId = audioId;
    }

    public String getFileTime() {
        return fileTime;
    }

    public void setFileTime(String fileTime) {
        this.fileTime = fileTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}

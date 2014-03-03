package org.liyou.qixiaobo.entities.request;

/**
 * Created by Administrator on 14-3-1.
 */
public class VoiceRequestMessage extends BaseRequestMessage {
    // 媒体ID
    private String MediaId;
    // 语音格式
    private String Format;
    //语音识别结果
    private String Recognition;

    public String getRecognition () {
        return Recognition;
    }

    public void setRecognition (String recognition) {
        Recognition = recognition;
    }


    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }

    @Override
    public String toString () {
        return "VoiceRequestMessage{" +
                "MediaId='" + MediaId + '\'' +
                ", Format='" + Format + '\'' +
                ", Recognition='" + Recognition + '\'' +
                "} " + super.toString ();
    }
}

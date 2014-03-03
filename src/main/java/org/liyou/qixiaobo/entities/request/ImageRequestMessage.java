package org.liyou.qixiaobo.entities.request;

/**
 * Created by Administrator on 14-3-1.
 */
public class ImageRequestMessage extends BaseRequestMessage {
    // 图片链接
    private String PicUrl;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    @Override
    public String toString () {
        return "ImageRequestMessage{" +
                "PicUrl='" + PicUrl + '\'' +
                "} " + super.toString ();
    }
}

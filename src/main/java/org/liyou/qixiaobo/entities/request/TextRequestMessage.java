package org.liyou.qixiaobo.entities.request;

/**
 * Created by Administrator on 14-3-1.
 */
public class TextRequestMessage extends BaseRequestMessage {
    // 消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    @Override
    public String toString () {
        return "TextResponseMessage{" +
                "Content='" + Content + '\'' +
                "} " + super.toString ();
    }
}

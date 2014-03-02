package org.liyou.qixiaobo.entities.response;

import org.liyou.qixiaobo.utils.MessageUtil;

/**
 * Created by Administrator on 14-3-1.
 */
public class TextMessage extends  BaseMessage {
    // 回复的消息内容
    private String Content;

    public TextMessage () {
        this.setMsgType (MessageUtil.RESP_MESSAGE_TYPE_TEXT);
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}

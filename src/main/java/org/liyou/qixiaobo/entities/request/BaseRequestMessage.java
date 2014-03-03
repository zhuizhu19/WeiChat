package org.liyou.qixiaobo.entities.request;

/**
 * Created by Administrator on 14-3-1.
 */
public class BaseRequestMessage extends BaseEvent {
    // 消息id，64位整型
    private long MsgId;

    public long getMsgId () {
        return MsgId;
    }

    public void setMsgId (long msgId) {
        MsgId = msgId;
    }

    @Override
    public String toString () {
        return "BaseRequestMessage{" +
                "MsgId=" + MsgId +
                "} " + super.toString ();
    }
}

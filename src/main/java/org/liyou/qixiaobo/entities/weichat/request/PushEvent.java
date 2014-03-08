package org.liyou.qixiaobo.entities.weichat.request;

import org.liyou.qixiaobo.utils.MessageUtil;

/**
 * Created by Administrator on 14-3-2.
 */
public class PushEvent extends BaseEvent{
    public PushEvent () {
        this.setMsgType (MessageUtil.REQ_MESSAGE_TYPE_EVENT);
    }

    private String Event;

    public String getEvent () {
        return Event;
    }

    public void setEvent (String event) {
        Event = event;
    }

    @Override
    public String toString () {
        return "PushEvent{" +
                "Event='" + Event + '\'' +
                "} " + super.toString ();
    }
}

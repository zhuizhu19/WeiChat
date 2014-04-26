package org.liyou.qixiaobo.entities.weichat.request;

import org.liyou.qixiaobo.utils.MessageUtil;

/**
 * Created by Administrator on 14-3-2.
 */
public class ClickEvent extends PushEvent {
    public ClickEvent () {
        this.setEvent(MessageUtil.EVENT_TYPE_CLICK);
    }

    private String EventKey;

    public String getEventKey () {
        return EventKey;
    }

    public void setEventKey (String eventKey) {
        EventKey = eventKey;
    }

    @Override
    public String toString () {
        return "ClickEvent{" +
                "EventKey='" + EventKey + '\'' +
                "} " + super.toString();
    }
}

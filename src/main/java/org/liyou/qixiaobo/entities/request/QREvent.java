package org.liyou.qixiaobo.entities.request;

/**
 * Created by Administrator on 14-3-2.
 */
public class QREvent extends PushEvent {
    private String EventKey;
    private String Ticket;

    public String getEventKey () {
        return EventKey;
    }

    public void setEventKey (String eventKey) {
        EventKey = eventKey;
    }

    public String getTicket () {
        return Ticket;
    }

    public void setTicket (String ticket) {
        Ticket = ticket;
    }

    @Override
    public String toString () {
        return "QREvent{" +
                "EventKey='" + EventKey + '\'' +
                ", Ticket='" + Ticket + '\'' +
                "} " + super.toString ();
    }
}

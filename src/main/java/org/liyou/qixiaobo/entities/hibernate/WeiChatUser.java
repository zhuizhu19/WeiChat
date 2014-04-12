package org.liyou.qixiaobo.entities.hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Administrator on 14-4-12.
 */
@Entity
@Table(name = "t_weichat_users")
public class WeiChatUser {
    private String fromUserName;
    private String weiChatName;
    private boolean flag;
    private int category;
    private int stage;

    @Id
    public String getFromUserName () {
        return fromUserName;
    }

    public void setFromUserName (String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getWeiChatName () {
        return weiChatName;
    }

    public void setWeiChatName (String weiChatName) {
        this.weiChatName = weiChatName;
    }

    public boolean isFlag () {
        return flag;
    }

    public void setFlag (boolean flag) {
        this.flag = flag;
    }

    public int getCategory () {
        return category;
    }

    public void setCategory (int category) {
        this.category = category;
    }

    public int getStage () {
        return stage;
    }

    public void setStage (int stage) {
        this.stage = stage;
    }
}

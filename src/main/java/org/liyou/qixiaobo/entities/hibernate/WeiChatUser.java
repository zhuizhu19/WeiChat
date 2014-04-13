package org.liyou.qixiaobo.entities.hibernate;

import javax.persistence.*;

/**
 * Created by Administrator on 14-4-12.
 */
@Entity
@Table(name = "t_weichat_users")
public class WeiChatUser {
    private String fromUserName;
    private String weiChatName;
    private int flag;
    private Stage stage;

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

    @Column(nullable = false, length = 2)
    public int getFlag () {
        return flag;
    }

    public void setFlag (int flag) {
        this.flag = flag;
    }

    @OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    public Stage getStage () {
        return stage;
    }

    public void setStage (Stage stage) {
        this.stage = stage;
    }
}

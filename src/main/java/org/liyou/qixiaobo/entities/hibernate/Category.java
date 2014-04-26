package org.liyou.qixiaobo.entities.hibernate;

import javax.persistence.*;

/**
 * Created by Administrator on 14-4-12.
 */
@Entity
@Table(name = "t_weichat_category")
public class Category {
    private int id;
    private String des;
    private String key;

    @Id
    @GeneratedValue
    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    @Column(length = 65535, nullable = false)
    public String getDes () {
        return des;
    }

    public void setDes (String des) {
        this.des = des;
    }

    @Column(unique = true, name = "short_key")
    public String getKey () {
        return key;
    }

    public void setKey (String key) {
        this.key = key;
    }

}

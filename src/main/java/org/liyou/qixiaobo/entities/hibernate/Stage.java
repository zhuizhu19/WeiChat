package org.liyou.qixiaobo.entities.hibernate;

import javax.persistence.*;

/**
 * Created by Administrator on 14-4-12.
 */
@Entity
@Table(name = "t_weichat_stage")
public class Stage {
    private int id;
    private Category category;
    private String key;
    private String des;
    private String remark;

    @Id
    @GeneratedValue
    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    public Category getCategory () {
        return category;
    }

    public void setCategory (Category category) {
        this.category = category;
    }

    @Column(name = "short_key")
    public String getKey () {
        return key;
    }

    public void setKey (String key) {
        this.key = key;
    }

    @Column(nullable = false, length = 65535)
    public String getDes () {
        return des;
    }

    public void setDes (String des) {
        this.des = des;
    }

    @Column(length = 65535)
    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }
}

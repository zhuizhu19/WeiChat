package org.liyou.qixiaobo.entities.hibernate;

import javax.persistence.*;

/**
 * Created by Administrator on 14-5-4.
 */
@Entity
@Table(name = "evernote")
public class Evernote {
    private int id;
    private String title;
    private String url;
    private String remark;
    @Id
    @GeneratedValue
    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }
    @Column(length = 65535, nullable = false)
    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }
    @Column(length = 65535, nullable = false)
    public String getUrl () {
        return url;
    }

    public void setUrl (String url) {
        this.url = url;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }
}

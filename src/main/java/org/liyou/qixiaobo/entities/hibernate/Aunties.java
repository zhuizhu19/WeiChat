package org.liyou.qixiaobo.entities.hibernate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 14-4-15.
 */
@Entity
@Table(name = "t_yoyo_aunties")
public class Aunties {
    private int id;
    private Date auntDate;
    private int intervalDate;
    private String advice;
    private String remark;
    @Id
    @GeneratedValue
    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public Date getAuntDate () {
        return auntDate;
    }
    public void setAuntDate (Date auntDate) {
        this.auntDate = auntDate;
    }

    public int getIntervalDate () {
        return intervalDate;
    }

    public void setIntervalDate (int intervalDate) {
        this.intervalDate = intervalDate;
    }

    public String getAdvice () {
        return advice;
    }

    public void setAdvice (String advice) {
        this.advice = advice;
    }

    public String getRemark () {
        return remark;
    }

    public void setRemark (String remark) {
        this.remark = remark;
    }
}

package org.liyou.qixiaobo.entities.hibernate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 14-4-12.
 */
@Entity
@Table(name = "t_weichat_category")
public class Category {
    private int id;
    private String des;
    private String remark;
}

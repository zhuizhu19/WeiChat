package org.liyou.qixiaobo.entities.hibernate;

import javax.persistence.*;

/**
 * Created by Administrator on 14-3-8.
 */
@Entity
@Table(name = "t_skills")
public class Skill {
    private int id;
    private String skillName;
    private String skillUrl;
    private String skillImgUrl;
    private String skillDesc;

    @Id
    @GeneratedValue
    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    @Column(length = 65535)
    public String getSkillDesc () {
        return skillDesc;
    }

    public void setSkillDesc (String skillDesc) {
        this.skillDesc = skillDesc;
    }

    @Column(nullable = false, length = 30)
    public String getSkillName () {
        return skillName;
    }

    public void setSkillName (String skillName) {
        this.skillName = skillName;
    }

    public String getSkillUrl () {
        return skillUrl;
    }

    public void setSkillUrl (String skillUrl) {
        this.skillUrl = skillUrl;
    }

    public String getSkillImgUrl () {
        return skillImgUrl;
    }

    public void setSkillImgUrl (String skillImgUrl) {
        this.skillImgUrl = skillImgUrl;
    }

}

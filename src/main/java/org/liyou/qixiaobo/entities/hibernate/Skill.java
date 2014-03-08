package org.liyou.qixiaobo.entities.hibernate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 14-3-8.
 */
@Entity
@Table(name ="t_skills")
public class Skill {
    public Skill () {

    }

    public Skill (String skillName, String skillUrl, String skillImgUrl, String skillDesc) {
        this.skillName = skillName;
        this.skillUrl = skillUrl;
        this.skillImgUrl = skillImgUrl;
        this.skillDesc = skillDesc;
    }

    private String skillName;
    private String skillUrl;
    private String skillImgUrl;

    public String getSkillDesc () {
        return skillDesc;
    }

    public void setSkillDesc (String skillDesc) {
        this.skillDesc = skillDesc;
    }

    private String skillDesc;

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

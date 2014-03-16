package org.liyou.qixiaobo.entities.hibernate;


import javax.persistence.*;
import java.util.List;

/**
 * Created by Administrator on 14-3-8.
 */
@Entity
@Table(name = "t_heros")
public class Hero {
    private int id;
    private String name;
    private String shortName;
    private String imgUrl;
    private List<Skill> skills;
    private String url;
    private String des;
    private String initProps;
    private HeroDetail heroDetail;
    private String house;

    public Hero (int id, String name, String shortName, String imgUrl, String url, String des, String initProps, String house) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.imgUrl = imgUrl;
        this.url = url;
        this.des = des;
        this.initProps = initProps;
        this.house = house;
    }

    public Hero (int id, String name, String shortName, String imgUrl, String house) {
        this.id = id;
        this.shortName = shortName;
        this.name = name;
        this.imgUrl = imgUrl;
        this.house = house;
    }

    public Hero () {
    }

    @Id
    @GeneratedValue
    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    @Column(length = 65535)
    public String getDes () {
        return des;
    }

    public void setDes (String des) {
        this.des = des;
    }

    @Column(unique = true, nullable = false, length = 30)
    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    @Column(nullable = false, length = 30)
    public String getShortName () {
        return shortName;
    }

    public void setShortName (String shortName) {
        this.shortName = shortName;
    }

    public String getImgUrl () {
        return imgUrl;
    }

    public void setImgUrl (String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    public List<Skill> getSkills () {
        return skills;
    }

    public void setSkills (List<Skill> skills) {
        this.skills = skills;
    }

    public String getUrl () {
        return url;
    }

    public void setUrl (String url) {
        this.url = url;
    }

    public String getInitProps () {
        return initProps;
    }

    public void setInitProps (String initProps) {
        this.initProps = initProps;
    }

    @OneToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
    public HeroDetail getHeroDetail () {
        return heroDetail;
    }

    public void setHeroDetail (HeroDetail heroDetail) {
        this.heroDetail = heroDetail;
    }

    public String getHouse () {
        return house;
    }

    public void setHouse (String house) {
        this.house = house;
    }
}

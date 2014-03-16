package org.liyou.qixiaobo.entities.hibernate;

import javax.persistence.*;

/**
 * Created by Administrator on 14-3-16.
 */
@Entity
@Table(name = "t_heros_details")
public class HeroDetail {
    private int id;
    private String advantages;
    private String disAdvantages;
    private String buddies;
    private String enmies;
    private String good;
    private String better;
    private String best;
    private String reason;
    private String point;
    private String pointReason;

    @Id
    @GeneratedValue
    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    @Column(length = 65535)
    public String getAdvantages () {
        return advantages;
    }

    public void setAdvantages (String advantages) {
        this.advantages = advantages;
    }
    @Column(length = 65535)
    public String getDisAdvantages () {
        return disAdvantages;
    }

    public void setDisAdvantages (String disAdvantages) {
        this.disAdvantages = disAdvantages;
    }
    @Column(length = 65535)
    public String getBuddies () {
        return buddies;
    }

    public void setBuddies (String buddies) {
        this.buddies = buddies;
    }
    @Column(length = 65535)
    public String getEnmies () {
        return enmies;
    }

    public void setEnmies (String enmies) {
        this.enmies = enmies;
    }
    @Column(length = 65535)
    public String getGood () {
        return good;
    }

    public void setGood (String good) {
        this.good = good;
    }
    @Column(length = 65535)
    public String getBetter () {
        return better;
    }

    public void setBetter (String better) {
        this.better = better;
    }
    @Column(length = 65535)
    public String getBest () {
        return best;
    }

    public void setBest (String best) {
        this.best = best;
    }

    @Column(length = 65535)
    public String getReason () {
        return reason;
    }

    public void setReason (String reason) {
        this.reason = reason;
    }
    @Column(length = 65535)
    public String getPoint () {
        return point;
    }

    public void setPoint (String point) {
        this.point = point;
    }

    @Column(length = 65535)
    public String getPointReason () {
        return pointReason;
    }

    public void setPointReason (String pointReason) {
        this.pointReason = pointReason;
    }
}

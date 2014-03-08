package org.liyou.qixiaobo.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-3-3.
 */
public class DotaService {
    public static final String dota_website_url = "http://dota.db.766.com/index.php/index/";
    public static final String dota_heroes_url = dota_website_url + "herolist/0/0/";
    static List<DotaModel> models;
    static boolean complete = false;

    static {
        new Thread (new Runnable () {
            @Override
            public void run () {
                initModel ();
            }
        }).start ();
    }

    public static List<DotaModel> searchHeros (String name) {
        if (!complete || name == null || name.trim ().equals ("")) {
            return null;
        }
        List<DotaModel> heros = new ArrayList<DotaModel> (2);
        for (DotaModel model : models) {
            if (model.getName ().contains (name) || model.getShortName ().toLowerCase ().contains (name.toLowerCase ())) {
                heros.add (model);
            }
        }
        return heros;
    }

    public static void initModel () {
        if (models != null || complete) {
            //that means complete or running
            return;
        }
        if (models == null) {
            models = new ArrayList<DotaModel> (110);
        }
        for (int i = 1; i <= 6; i++) {
            try {
                Document doc = Jsoup.connect (dota_heroes_url + i).timeout (0).get ();
                Elements items = doc.select (".container");//dota info
                if (items != null && items.size () != 0) {
                    Element container = items.get (0);
                    Elements tables = container.getElementsByTag ("table");
                    if (tables != null && tables.size () != 0) {
                        Element table = tables.get (0);
                        Elements rows = table.getElementsByTag ("tr");
                        if (rows != null) {
                            for (Element row : rows) {
                                Elements tds = row.getElementsByTag ("td");
                                if (tds == null || tds.size () == 0) {
                                    continue;
                                }
                                Element icon = tds.get (0);
                                Element name = tds.get (1);
                                Element shortName = tds.get (2);
                                Element skills = tds.get (4);
                                DotaModel hero = new DotaModel ();
                                hero.setUrl (icon.child (0).attr ("href"));
                                hero.setImgUrl (icon.child (0).child (0).attr ("src"));
                                hero.setName (name.text ());
                                hero.setShortName (shortName.text ());
                                Document docTmp = Jsoup.connect (hero.getUrl ()).timeout (0).get ();
                                Elements eles = docTmp.getElementsByClass ("data");
                                if (eles != null && eles.size () != 0) {
                                    Element element = eles.get (0);
                                    String des = "";
                                    try {
                                        des = element.getElementsByTag ("p").get (0).text ();
                                    } catch (Exception ex) {

                                    }
                                    hero.setDes (des);
                                }
                                List<Skill> skillList = new ArrayList<Skill> (skills.childNodeSize ());
                                for (Element skillElement : skills.children ()) {
                                    Skill skill = new Skill ();
                                    skill.setSkillUrl (skillElement.attr ("href"));
                                    skill.setSkillName (skillElement.attr ("title"));
                                    skill.setSkillImgUrl (skillElement.child (0).attr ("src"));
                                    docTmp = Jsoup.connect (skill.getSkillUrl ()).timeout (0).get ();
                                    eles = docTmp.getElementsByClass ("data");
                                    if (eles != null && eles.size () != 0) {
                                        Element element = eles.get (0);
                                        String des = "";
                                        try {
                                            des = element.getElementsByTag ("p").get (0).text ();
                                        } catch (Exception ex) {

                                        }
                                        skill.setSkillDesc (des);
                                    }
                                    skillList.add (skill);
                                }
                                hero.setSkills (skillList);
                                models.add (hero);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }
        complete = true;
    }

    public static class DotaModel {
        private String name;
        private String shortName;
        private String imgUrl;
        private List<Skill> skills;
        private String url;
        private String des;

        public String getDes () {
            return des;
        }

        public void setDes (String des) {
            this.des = des;
        }

        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

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
    }

    public static class Skill {
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
}

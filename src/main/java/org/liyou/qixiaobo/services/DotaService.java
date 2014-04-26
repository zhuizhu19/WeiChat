package org.liyou.qixiaobo.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.liyou.qixiaobo.daos.HeroDao;
import org.liyou.qixiaobo.daos.HeroDetailDao;
import org.liyou.qixiaobo.daos.SkillDao;
import org.liyou.qixiaobo.entities.hibernate.Hero;
import org.liyou.qixiaobo.entities.hibernate.HeroDetail;
import org.liyou.qixiaobo.entities.hibernate.Skill;
import org.liyou.qixiaobo.utils.DomainTool;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-3-3.
 */
@Component
public class DotaService implements ApplicationListener<ContextRefreshedEvent> {
    public static final String dota_website_url = "http://dota.db.766.com/index.php/index/";
    public static final String dota_heroes_url = dota_website_url + "herolist/0/0/";
    public static final String dota_house_list_url = dota_website_url + "herolist";
    static boolean complete = false;
    static boolean needUpdate = false;
    boolean isDotaEnabled = false;
    @Resource
    private SkillDao skillDao;
    @Resource
    private HeroDao heroDao;
    @Resource
    private HeroDetailDao heroDetailDao;


    public List<Hero> searchHeros (String name) {
        if (!isDotaEnabled) {
            return null;
        }
        if (!complete || name == null || name.trim().equals("")) {
            return null;
        }
        List<Hero> heros = heroDao.queryByHeroNameOrShortname(name);

        return heros;
    }

    private void initModel () {
        if ((isDotaEnabled = Boolean.parseBoolean(DomainTool.info("dota"))) != true) {
            return;
        }
        if (!needUpdate && complete) {
            //that means complete or running
            return;
        }
        complete = false;
        if (!needUpdate) {
            if (heroDao.queryHeroNums() > 100) {
                complete = true;
                return;
            }
        }
        try {
            Document doc = Jsoup.connect(dota_website_url).timeout(0).get();
            Elements elements = doc.getElementsByAttributeValueContaining("href", "herolist");
            for (Element element : elements) {
                if (!element.text().contains("军团")) {
                    continue;
                } else {
                    String href = element.attr("href");
                    String houseName = element.text();
                    String temp = houseName.substring(0, 5);
                    String[] array = temp.split("\\(");
                    houseName = array[1] + array[0] + houseName.charAt(houseName.length() - 2);
                    getDotaHeros(href, houseName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        complete = true;
    }

    private boolean getDotaHeros (String url, String houseName) {
        try {
            Document doc = Jsoup.connect(url).timeout(0).get();
            Elements items = doc.select(".container");//dota info
            if (items != null && items.size() != 0) {
                Element container = items.get(0);
                Elements tables = container.getElementsByTag("table");
                if (tables != null && tables.size() != 0) {
                    Element table = tables.get(0);
                    Elements rows = table.getElementsByTag("tr");
                    if (rows != null) {
                        for (Element row : rows) {
                            Elements tds = row.getElementsByTag("td");
                            if (tds == null || tds.size() == 0) {
                                continue;
                            }
                            Element icon = tds.get(0);
                            Element name = tds.get(1);
                            Element shortName = tds.get(2);
                            Element skills = tds.get(4);
                            Hero hero = new Hero();
                            hero.setHouse(houseName);
                            String href = icon.child(0).attr("href");
                            String[] array = href.split("/");
                            hero.setUrl(array[array.length - 1]);
                            hero.setImgUrl(icon.child(0).child(0).attr("src"));
                            hero.setName(name.text());
                            hero.setShortName(shortName.text());
                            Document docTmp = Jsoup.connect(href).timeout(0).get();
                            Elements eles = docTmp.getElementsByClass("data");
                            if (eles != null && eles.size() != 0) {
                                Element element = eles.get(0);//des
                                String des = "";
                                try {
                                    des = element.getElementsByTag("p").get(0).text();
                                } catch (Exception ex) {

                                }
                                hero.setDes(des);
                                element = eles.get(0).getElementsByClass("line").get(0);//init
                                StringBuilder stringBuilder = new StringBuilder();
                                try {
                                    Elements elements = element.getElementsByTag("ul");
                                    for (Element element1 : elements) {
                                        stringBuilder.append(element1.text());
                                        stringBuilder.append("|");
                                        stringBuilder.append(element1.getElementsByTag("span").get(0).text());
                                        stringBuilder.append("|");
                                    }
                                    hero.setInitProps(stringBuilder.toString());
                                } catch (Exception ex) {

                                }
                            }
                            HeroDetail heroDetail = new HeroDetail();
                            Element element = docTmp.getElementById("ue_tab1");//advantages
                            if (element != null) {
                                eles = element.getElementsByTag("p");
                                heroDetail.setAdvantages(eles.get(0).text());
                                heroDetail.setDisAdvantages(eles.get(1).text());
                            }
                            element = docTmp.getElementById("ue_tab2");//heros
                            if (element != null) {
                                eles = element.getElementsByClass("piclist3");
                                StringBuilder stringBuilder = new StringBuilder();
                                for (Element element1 : eles.get(0).children()) {//buddies
                                    element = element1.child(0);
                                    href = element.attr("href");
                                    array = href.split("/");
                                    stringBuilder.append(array[array.length - 1]);
                                    stringBuilder.append("|");
                                    stringBuilder.append(element.html());
                                    stringBuilder.append("|");
                                }
                                heroDetail.setBuddies(stringBuilder.toString());
                                stringBuilder = new StringBuilder();
                                for (Element element1 : eles.get(1).children()) {//enmies
                                    element = element1.child(0);
                                    href = element.attr("href");
                                    array = href.split("/");
                                    stringBuilder.append(array[array.length - 1]);
                                    stringBuilder.append("|");
                                    stringBuilder.append(element.html());
                                    stringBuilder.append("|");
                                }
                                heroDetail.setEnmies(stringBuilder.toString());
                            }
                            element = docTmp.getElementById("ue_tab");//equ
                            if (element != null) {
                                eles = element.getElementsByClass("piclist3");
                                StringBuilder stringBuilder = new StringBuilder();
                                for (Element element1 : eles.get(0).children()) {//good
                                    element = element1.child(0);
                                    href = element.attr("href");
                                    array = href.split("/");
                                    stringBuilder.append(array[array.length - 1]);
                                    stringBuilder.append("|");
                                    stringBuilder.append(element.html());
                                    stringBuilder.append("|");
                                }
                                heroDetail.setGood(stringBuilder.toString());
                                stringBuilder = new StringBuilder();
                                for (Element element1 : eles.get(1).children()) {//better
                                    element = element1.child(0);
                                    href = element.attr("href");
                                    array = href.split("/");
                                    stringBuilder.append(array[array.length - 1]);
                                    stringBuilder.append("|");
                                    stringBuilder.append(element.html());
                                    stringBuilder.append("|");
                                }
                                heroDetail.setBetter(stringBuilder.toString());
                                stringBuilder = new StringBuilder();
                                for (Element element1 : eles.get(2).children()) {//best
                                    element = element1.child(0);
                                    href = element.attr("href");
                                    array = href.split("/");
                                    stringBuilder.append(array[array.length - 1]);
                                    stringBuilder.append("|");
                                    stringBuilder.append(element.html());
                                    stringBuilder.append("|");
                                }
                                heroDetail.setBest(stringBuilder.toString());
                                element = docTmp.getElementById("ue_tab");
                                heroDetail.setReason(element.getElementsByTag("p").get(0).text());
                            }
                            element = docTmp.getElementById("skill");
                            heroDetail.setPoint(element.getElementsByClass("piclist4").get(0).html());
                            heroDetail.setPointReason(element.getElementsByTag("p").get(0).text());
                            heroDetail = heroDetailDao.insert(heroDetail);
                            hero.setHeroDetail(heroDetail);
                            element = docTmp.getElementsByClass("tabtit").get(0);
                            List<Skill> skillList = new ArrayList<Skill>(skills.childNodeSize());
                            for (Element skillElement : skills.children()) {
                                Skill skill = new Skill();
                                skill.setSkillUrl(skillElement.attr("href"));
                                skill.setSkillName(skillElement.attr("title"));
                                skill.setSkillImgUrl(skillElement.child(0).attr("src"));
                                docTmp = Jsoup.connect(skill.getSkillUrl()).timeout(0).get();
                                eles = docTmp.getElementsByClass("data");
                                if (eles != null && eles.size() != 0) {
                                    element = eles.get(0);
                                    String des = "";
                                    try {
                                        des = element.getElementsByTag("p").get(0).text();
                                    } catch (Exception ex) {

                                    }
                                    skill.setSkillDesc(des);
                                    eles = element.getElementsByTag("tr");
                                    element = eles.get(1);//cd
                                    skill.setCd(element.textNodes().get(0).text());
                                    element = eles.get(2);//mp
                                    skill.setMpCost(element.textNodes().get(0).text());
                                    element = eles.get(3); //distance
                                    skill.setDistance(element.textNodes().get(0).text());
                                }
                                Skill skillTemp = skillDao.queryBySkillName(skill.getSkillName());
                                if (skillTemp != null) {
                                    if (needUpdate) {
                                        skill = skillDao.update(skillTemp);
                                    }
                                } else {
                                    skill = skillDao.insert(skill);
                                }
                                skillList.add(skill);
                            }
                            hero.setSkills(skillList);
                            Hero heroTemp = heroDao.queryByHeroName(hero.getName());
                            if (heroTemp != null) {
                                if (needUpdate) {
                                    hero = heroDao.update(hero);
                                }
                            } else {
                                hero = heroDao.insert(hero);
                            }

                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void onApplicationEvent (ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {//root application context
            new Thread(new Runnable() {
                @Override
                public void run () {
                    initModel();
                }
            }).start();
        }

    }

    public enum DotaHouse {
        近卫力量_1("近卫力量1", "酒馆"), 近卫力量_2("近卫力量2", "酒馆"), 天灾力量_1("天灾力量1", "酒馆"), 天灾力量_2("天灾力量2", "酒馆"),
        近卫敏捷_1("近卫敏捷1", "酒馆"), 近卫敏捷_2("近卫敏捷2", "酒馆"), 天灾敏捷_1("天灾敏捷1", "酒馆"), 天灾敏捷_2("天灾敏捷2", "酒馆"),
        近卫智力_1("近卫智力1", "酒馆"), 近卫智力_2("近卫智力2", "酒馆"), 天灾智力_1("天灾智力1", "酒馆"), 天灾智力_2("天灾智力2", "酒馆");


        String houseName;
        String realName;

        DotaHouse (String houseName, String realName) {
            this.houseName = houseName;
            this.realName = realName;
        }

        public String getHouseName () {
            return houseName;
        }

        public String getRealName () {
            return realName;
        }
    }
}

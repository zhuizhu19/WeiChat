package org.liyou.qixiaobo.controllers;

import org.liyou.qixiaobo.common.BaseController;
import org.liyou.qixiaobo.daos.HeroDao;
import org.liyou.qixiaobo.entities.hibernate.Hero;
import org.liyou.qixiaobo.execptions.Resource404Exception;
import org.liyou.qixiaobo.services.DotaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 14-3-15.
 */
@Controller
@RequestMapping("/dota")
public class DotaController extends BaseController {
    @Resource
    private HeroDao heroDao;

    @RequestMapping("/heros")
    public String allHeros (Model model) {
        Map<String, List<Hero>> map = new LinkedHashMap<String, List<Hero>>();
        List<Hero> heros = heroDao.queryAllHerosByHouse(DotaService.DotaHouse.近卫力量_1.getHouseName(), true);
        map.put(DotaService.DotaHouse.近卫力量_1.getHouseName(), heros);
        heros = heroDao.queryAllHerosByHouse(DotaService.DotaHouse.近卫敏捷_1.getHouseName(), true);
        map.put(DotaService.DotaHouse.近卫敏捷_1.getHouseName(), heros);
        heros = heroDao.queryAllHerosByHouse(DotaService.DotaHouse.近卫智力_1.getHouseName(), true);
        map.put(DotaService.DotaHouse.近卫智力_1.getHouseName(), heros);
        heros = heroDao.queryAllHerosByHouse(DotaService.DotaHouse.近卫力量_2.getHouseName(), true);
        map.put(DotaService.DotaHouse.近卫力量_2.getHouseName(), heros);
        heros = heroDao.queryAllHerosByHouse(DotaService.DotaHouse.近卫敏捷_2.getHouseName(), true);
        map.put(DotaService.DotaHouse.近卫敏捷_2.getHouseName(), heros);
        heros = heroDao.queryAllHerosByHouse(DotaService.DotaHouse.近卫智力_2.getHouseName(), true);
        map.put(DotaService.DotaHouse.近卫智力_2.getHouseName(), heros);
        heros = heroDao.queryAllHerosByHouse(DotaService.DotaHouse.天灾力量_1.getHouseName(), true);
        map.put(DotaService.DotaHouse.天灾力量_1.getHouseName(), heros);
        heros = heroDao.queryAllHerosByHouse(DotaService.DotaHouse.天灾敏捷_1.getHouseName(), true);
        map.put(DotaService.DotaHouse.天灾敏捷_1.getHouseName(), heros);
        heros = heroDao.queryAllHerosByHouse(DotaService.DotaHouse.天灾智力_1.getHouseName(), true);
        map.put(DotaService.DotaHouse.天灾智力_1.getHouseName(), heros);
        heros = heroDao.queryAllHerosByHouse(DotaService.DotaHouse.天灾力量_2.getHouseName(), true);
        map.put(DotaService.DotaHouse.天灾力量_2.getHouseName(), heros);
        heros = heroDao.queryAllHerosByHouse(DotaService.DotaHouse.天灾敏捷_2.getHouseName(), true);
        map.put(DotaService.DotaHouse.天灾敏捷_2.getHouseName(), heros);
        heros = heroDao.queryAllHerosByHouse(DotaService.DotaHouse.天灾智力_2.getHouseName(), true);
        map.put(DotaService.DotaHouse.天灾智力_2.getHouseName(), heros);
        model.addAttribute("heros", map);
        return "heros";
    }

    @RequestMapping("/heros/{id}")
    public String Hero (Model model, @PathVariable String id) throws Resource404Exception {
        if (id == null || id.trim().equals(""))
            throw new Resource404Exception("invaild id");
        try {
            int idInt = Integer.parseInt(id);
            Hero hero = heroDao.query(Hero.class, idInt);
            if (hero == null) {
                throw new Resource404Exception("invaild id");
            }
            model.addAttribute("hero", hero);
        } catch (NumberFormatException ex) {
            throw new Resource404Exception("invaild id");
        }
        return "hero";
    }
}

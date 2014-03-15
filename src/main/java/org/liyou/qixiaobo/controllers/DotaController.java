package org.liyou.qixiaobo.controllers;

import org.liyou.qixiaobo.common.BaseController;
import org.liyou.qixiaobo.daos.HeroDao;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by Administrator on 14-3-15.
 */
@Controller
@RequestMapping("/dota")
public class DotaController extends BaseController{
    @Resource
    private HeroDao heroDao;
    @RequestMapping("/all")
    public String allHeros(Model model){
        model.addAttribute ("heros",heroDao.queryAllHeros (false));
        return "heros";
    }
}

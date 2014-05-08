package org.liyou.qixiaobo.controllers;

import org.liyou.qixiaobo.common.BaseController;
import org.liyou.qixiaobo.daos.EvernoteDao;
import org.liyou.qixiaobo.entities.hibernate.Evernote;
import org.liyou.qixiaobo.services.CoreService;
import org.liyou.qixiaobo.utils.SignUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 14-3-1.
 */
@Controller
public class WeiChatController extends BaseController {
    @Resource
    private CoreService coreService;
    @Resource
    private EvernoteDao evernoteDao;

    /**
     * @param signature 随机字符串
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   回显字符串
     */
    @ResponseBody
    @RequestMapping(value = {"/yoyo"}, method = RequestMethod.GET)
    public String checkSignature (String signature, String timestamp, String nonce, String echostr) {

        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            return echostr;
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = {"/yoyo"}, method = RequestMethod.POST)
    public String processRequest (HttpServletRequest request) {
        String response = coreService.processRequest(request);
        System.out.println(response);
        return response;
    }

    @RequestMapping(value = {"/evernote"})
    public String evernote (Model model) {
        List<Evernote> notes = evernoteDao.queryAllNotes();
        model.addAttribute("evernotes",notes);
        return "evernote";
    }
    @RequestMapping(value="/")
    public String index(){
        return redirect("http://qixiaobo.github.io/WeiChat/");
    }

}

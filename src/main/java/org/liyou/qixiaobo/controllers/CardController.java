package org.liyou.qixiaobo.controllers;

import org.liyou.qixiaobo.common.BaseController;
import org.liyou.qixiaobo.common.StringUtils;
import org.liyou.qixiaobo.utils.IWeiChat;
import org.liyou.qixiaobo.utils.ImageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.ContextLoader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 14-3-9.
 */
@Controller
@RequestMapping(value = "/cards")
public class CardController extends BaseController {
    public static List<String> cards = null;
    private static final String CARD_FOLDER = "cards";
    private static final String DEFAULT_NAME = "李尤";

    static {
        cards = new ArrayList<String> (6);
        cards.add ("clean");
        cards.add ("cloth");
        cards.add ("dinner");
        cards.add ("joke");
        cards.add ("shopping");
        cards.add ("travel");
    }

    @RequestMapping(value = { "/{cardName}" })
    public void showCard (HttpServletRequest request, HttpServletResponse response, @PathVariable String cardName) {
        if (cardName == null) {
            cardName = cards.get (0);
        }
        cardName = cardName.trim ();
        if (!cards.contains (cardName)) {
            cardName = cards.get (0);
        }
        if (WEB_REAL_PATH == null) {
            WEB_REAL_PATH = ContextLoader.getCurrentWebApplicationContext ().getServletContext ().getRealPath ("/") + File.separatorChar + "WEB-INF";
        }
        String filePath = WEB_REAL_PATH + File.separatorChar + CARD_FOLDER + File.separatorChar + cardName + "card.jpg";
        File file = new File (filePath);

        FileInputStream is = null;
        try {
            is = new FileInputStream (file);
            byte[] abyte0 = new byte[(int) file.length ()];
            is.read (abyte0);
            showPicture (null, cardName + "card.jpg", abyte0, response, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    @RequestMapping(value = { "/loveuu/{cardName}/{name}" })
    public String showCard (Model model, @PathVariable String cardName, @PathVariable String name) {
        System.out.println ("showcard"+name);
        //if name contains chinese and so on,we may have a error
        try {
            name=java.net.URLEncoder.encode(name,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace ();
        }
        return redirect ("/cards/loveuu/" + cardName + "/" + name + "/" + System.currentTimeMillis ());
    }

    @RequestMapping(value = { "/loveuu/{cardName}/{name}/{time}" })
    public String showCard (Model model, @PathVariable String cardName, @PathVariable String name, @PathVariable String time) {
        return "showPicture";
    }

    @RequestMapping(value = { "/{cardName}/{name}/{time}" })
    public void showCard (HttpServletRequest request, HttpServletResponse response, @PathVariable String cardName, @PathVariable String name, @PathVariable String time) {
        if (cardName == null) {
            cardName = cards.get (0);
        }
        name = StringUtils.defaultIfEmpty (name, DEFAULT_NAME);
        if (name.length () > 6) {
            name = name.substring (0, 5);
        }
        time = StringUtils.defaultIfEmpty (time, "-1");
        long timeLong = -1;
        try {
            timeLong = Long.parseLong (time);
        } catch (NumberFormatException ex) {

        }
        if (timeLong == -1) {
            timeLong = System.currentTimeMillis ();
        }
        Date date = new Date (timeLong);
        String dateString = format (date);
        cardName = cardName.trim ();
//        ImageUtils.pressText ();
        if (!cards.contains (cardName)) {
            cardName = cards.get (0);
        }
        if (WEB_REAL_PATH == null) {
            WEB_REAL_PATH = ContextLoader.getCurrentWebApplicationContext ().getServletContext ().getRealPath ("/") + File.separatorChar + "WEB-INF";
        }
        String filePath = WEB_REAL_PATH + File.separatorChar + CARD_FOLDER + File.separatorChar + cardName + ".jpg";
        try {
            List<String> texts = new ArrayList<String> (2);
            List<Point> points = new ArrayList<Point> (2);
            texts.add (name);
            texts.add (dateString);
            points.add (new Point (IWeiChat.NAME_START_PX, IWeiChat.START_PX_Y));
            points.add (new Point (IWeiChat.DATA_START_PX, IWeiChat.START_PX_Y));
            ByteArrayOutputStream outputStream = ImageUtils.pressTextOutPutStream (texts, filePath, IWeiChat.FONT_NAME, 0, IWeiChat.FONT_COLOR, IWeiChat.FONT_SIZE, points, 1.0f);
            if (outputStream != null) {
                byte[] abyte0 = outputStream.toByteArray ();
                showPicture (null, cardName + ".jpg", abyte0, response, false);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    @RequestMapping(value = { "/{cardName}/{name}" })
    public String showCard (HttpServletRequest request, HttpServletResponse response, @PathVariable String cardName, @PathVariable String name) {
        return forward ("/cards/" + cardName + "/" + name + "/" + System.currentTimeMillis ());

    }


}

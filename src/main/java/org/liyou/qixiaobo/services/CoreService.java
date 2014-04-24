package org.liyou.qixiaobo.services;

import org.apache.log4j.Logger;
import org.liyou.qixiaobo.controllers.CardController;
import org.liyou.qixiaobo.daos.AuntiesDao;
import org.liyou.qixiaobo.daos.StageDao;
import org.liyou.qixiaobo.daos.UserDao;
import org.liyou.qixiaobo.entities.hibernate.*;
import org.liyou.qixiaobo.entities.weichat.request.*;
import org.liyou.qixiaobo.entities.weichat.response.Article;
import org.liyou.qixiaobo.entities.weichat.response.BaseResponseMessage;
import org.liyou.qixiaobo.entities.weichat.response.NewsResponseMessage;
import org.liyou.qixiaobo.entities.weichat.response.TextResponseMessage;
import org.liyou.qixiaobo.utils.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.liyou.qixiaobo.utils.MessageUtil.*;

/**
 * Created by Administrator on 14-3-1.
 */
@Component
public class CoreService {
    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */

    private static Article sArticle;
    private static Logger logger = Logger.getLogger (CoreService.class);
    @Resource
    private DotaService dotaService;
    @Resource
    private StageDao stageDao;
    @Resource
    private UserDao userDao;
    @Resource
    private AuntiesDao auntiesDao;
    private final Random random = new Random ();
    private static int isAuthed = 1;
    private static String WEATHER_INDEX = "http://m.weather.com.cn/data/";
    //http://www.mzwu.com/article.asp?id=3730
    private static int 德惠 = 101060103;
    private static int 南京 = 101190101;
    private static int 太原 = 101100101;
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat ("yyyy年MM月dd日");
    private String respContent = "请求处理异常，请稍候尝试！";
    private IChat iChat;

    static {
        sArticle = new Article ();
        sArticle.setTitle ("我的微博");
        sArticle.setDescription ("我从星星来，要到星星去！");
        sArticle.setPicUrl ("http://tp4.sinaimg.cn/2791610843/180/40043803902/1");
        sArticle.setUrl ("http://weibo.com/2791610843/profile?rightmod=1&wvr=5&mod=personinfo");
    }

    public String processRequest (HttpServletRequest request) {
        String respMessage = null;
        try {
            // 默认返回的文本消息内容
            BaseResponseMessage responseMessage = null;
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml (request);
            BaseEvent baseEvent = processMessage (requestMap);
            WeiChatUser weiChatUser = userDao.getWeiChatUserByFromUserName (baseEvent.getFromUserName ());
            if (weiChatUser == null) {
                weiChatUser = new WeiChatUser ();
                weiChatUser.setFromUserName (baseEvent.getFromUserName ());
                weiChatUser.setFlag (0);
                Stage stage = stageDao.query (Stage.class, 0);
                weiChatUser.setStage (stage);
                weiChatUser = userDao.insert (weiChatUser);
            }
            logger.info (baseEvent.toString ());
            if (baseEvent instanceof BaseRequestMessage) {
                if (baseEvent instanceof TextRequestMessage) {
                    TextRequestMessage textRequestMessage = (TextRequestMessage) baseEvent;
                    if (weiChatUser.getFlag () == isAuthed && isGodness (textRequestMessage.getContent ())) {
                        responseMessage = new NewsResponseMessage ();
                        List<Article> articles = new ArrayList<Article> ();
                        NewsResponseMessage newsResponseMessage = (NewsResponseMessage) responseMessage;
                        Article article = new Article ();
                        article.setTitle ("我的女神哎");
                        article.setPicUrl (YoYoUtil.PIC_PAGE_FACE);
                        article.setUrl (YoYoUtil.PIC_GODNESS);
                        article.setDescription ("我的女神哎");
                        articles.add (article);
                        newsResponseMessage.setArticles (articles);
                    } else if (weiChatUser.getFlag () == isAuthed && (responseMessage = processMenu (textRequestMessage.getContent (), weiChatUser)) != null) {
                        //donothing
                    } else {
                        responseMessage = new NewsResponseMessage ();
                        List<Article> articles = new ArrayList<Article> ();
                        NewsResponseMessage newsResponseMessage = (NewsResponseMessage) responseMessage;
                        List<Hero> models = dotaService.searchHeros (textRequestMessage.getContent ());
                        if (DotaService.complete && models != null && models.size () != 0) {
                            if (models.size () == 1) {
                                Hero model = models.get (0);
                                Article article = new Article ();
                                article.setTitle (model.getName ());
                                article.setPicUrl (model.getImgUrl ());
                                article.setUrl (YoYoUtil.WEBSITE_URL + "dota/heros/" + model.getId ());
                                article.setDescription (model.getDes ());
                                articles.add (article);
                                for (Skill skill : model.getSkills ()) {
                                    Article art = new Article ();
                                    art.setTitle (skill.getSkillName ());
                                    art.setUrl (YoYoUtil.WEBSITE_URL + "dota/heros/" + model.getId ());
                                    art.setPicUrl (skill.getSkillImgUrl ());
                                    art.setDescription (skill.getSkillDesc ());
                                    articles.add (art);
                                }
                            } else {
                                for (Hero model : models) {
                                    Article article = new Article ();
                                    article.setTitle (model.getName ());
                                    article.setPicUrl (model.getImgUrl ());
                                    article.setUrl (model.getUrl ());
                                    article.setDescription (model.getDes ());
                                    articles.add (article);
                                }
                            }
                            newsResponseMessage.setArticles (articles);
                        } else {
                            String content = textRequestMessage.getContent ();
                            content = content.trim ();
                            if (isLiYou (content)) {
                                weiChatUser = userDao.getWeiChatUserByFromUserName (baseEvent.getFromUserName ());
                                if (weiChatUser != null && weiChatUser.getFlag () != isAuthed) {
                                    weiChatUser.setFlag (isAuthed);
                                    userDao.update (weiChatUser);
                                } else if (weiChatUser == null) {
                                    //impossible
                                    weiChatUser = new WeiChatUser ();
                                    weiChatUser.setFromUserName (baseEvent.getFromUserName ());
                                    weiChatUser.setFlag (isAuthed);
                                    Stage stage = stageDao.query (Stage.class, 0);
                                    weiChatUser.setStage (stage);
                                    weiChatUser = userDao.insert (weiChatUser);
                                }
                                int size = CardController.cards.size ();
                                int randomNum = random.nextInt ();
                                randomNum = Math.abs (randomNum) % size;
                                String card = CardController.cards.get (randomNum);
                                Article article = new Article ();
                                article.setTitle (textRequestMessage.getContent () + "の专属" + card + "卡");
                                article.setPicUrl (YoYoUtil.WEBSITE_URL + "cards/" + card);
                                article.setUrl (YoYoUtil.WEBSITE_URL + "cards/loveuu/" + card + "/" + textRequestMessage.getContent () + "/" + System.currentTimeMillis ());
                                article.setDescription ("出示本卡可令祁麻麻" + getTitle (randomNum) + "一次。仅限" + content + "使用。有效期forever~");
                                articles.add (article);
                                newsResponseMessage.setArticles (articles);
                            } else {
                                TextResponseMessage textResponseMessage = new TextResponseMessage ();
                                if (weiChatUser.getFlag () == isAuthed) {
                                    textResponseMessage.setContent (getMainMenu (weiChatUser));
                                } else if (content.equals ("小尤")) {
                                    textResponseMessage.setContent ("木哈哈哈，你当我傻么，我不如直接告诉你得了……回覆我小尤的名字哦！！！");
                                } else {
                                    textResponseMessage.setContent ("拜託，你又不是我家小尤，我才不給你回照片呢~回覆我小尤的名字哦！！！");
                                }
                                responseMessage = textResponseMessage;
                            }
                        }
                    }


                } else if (baseEvent instanceof VoiceRequestMessage) {
                    responseMessage = new TextResponseMessage ();
                    VoiceRequestMessage voiceRequestMessage = (VoiceRequestMessage) baseEvent;
                    String result = voiceRequestMessage.getRecognition ();
                    synchronized (random) {
                        if (iChat == null) {
                            iChat = XiaoI.getInstance ();
                        }
                    }
                    String talk = iChat.chat (voiceRequestMessage.getFromUserName (), result, XiaoI.ASK, null);
                    ((TextResponseMessage) responseMessage).setContent (talk);
                } else {
                    responseMessage = new TextResponseMessage ();
                }
            } else if (baseEvent instanceof PushEvent) {
                responseMessage = new TextResponseMessage ();
            }
            responseMessage.setToUserName (baseEvent.getFromUserName ());
            responseMessage.setFromUserName (baseEvent.getToUserName ());
            responseMessage.setCreateTime (new Date ().getTime ());
            responseMessage.setFuncFlag (0);
            respMessage = MessageUtil.messageToXml (responseMessage);
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return respMessage;
    }

    public BaseResponseMessage processTextMessage (Map<String, String> requestMap) {
        BaseResponseMessage message = null;
        return message;
    }

    public static BaseEvent processMessage (Map<String, String> requestMap) {
        BaseEvent message = null;
        String msgType = requestMap.get ("MsgType");
        if (msgType.equals (REQ_MESSAGE_TYPE_TEXT)) {
            message = new TextRequestMessage ();
            message = reflectMessage (requestMap, message);
        } else if (msgType.equals (REQ_MESSAGE_TYPE_IMAGE)) {
            message = new ImageRequestMessage ();
            message = reflectMessage (requestMap, message);
        } else if (msgType.equals (REQ_MESSAGE_TYPE_LINK)) {
            message = new LinkRequestMessage ();
            message = reflectMessage (requestMap, message);
        } else if (msgType.equals (REQ_MESSAGE_TYPE_LOCATION)) {
            message = new LocationRequestMessage ();
            message = reflectMessage (requestMap, message);
        } else if (msgType.equals (REQ_MESSAGE_TYPE_VOICE)) {
            message = new VoiceRequestMessage ();
            message = reflectMessage (requestMap, message);
        } else if (msgType.equals (REQ_MESSAGE_TYPE_EVENT)) {
            String event = requestMap.get ("Event");
            if (event.equals (EVENT_TYPE_SUBSCRIBE)) {
                if (requestMap.get ("EventKey") == null) {
                    //关注事件
                    message = new BaseEvent ();
                } else {
                    //扫描二维码事件
                    message = new QREvent ();
                }
            } else if (event.equals (EVENT_TYPE_UNSUBSCRIBE)) {
                message = new BaseEvent ();
            } else if (event.equals (EVENT_TYPE_SCAN)) {
                message = new QREvent ();
            } else if (event.equals (EVENT_TYPE_LOCATION)) {
                message = new LocationEvent ();
            } else if (event.equals (EVENT_TYPE_CLICK)) {
                message = new ClickEvent ();
            } else {
                message = new BaseEvent ();
            }
            message = reflectMessage (requestMap, message);
        } else {
            message = new BaseRequestMessage ();
            message = reflectMessage (requestMap, message);
        }
        return message;
    }

    public static BaseEvent reflectMessage (Map<String, String> requestMap, BaseEvent message) {
        Class clazz = message.getClass ();
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields ()) {
                field.setAccessible (true);
                try {
                    Object value = null;
                    Class cla = field.getType ();
                    if (cla == long.class || cla == Long.class) {
                        value = Long.parseLong (requestMap.get (field.getName ()));
                    } else if (cla == byte.class || cla == Byte.class) {
                        value = Byte.parseByte (requestMap.get (field.getName ()));
                    } else if (cla == int.class || cla == Integer.class) {
                        value = Integer.parseInt (requestMap.get (field.getName ()));
                    } else if (cla == float.class || cla == Float.class) {
                        value = Float.parseFloat (requestMap.get (field.getName ()));
                    } else if (cla == double.class || cla == Double.class) {
                        value = Double.parseDouble (requestMap.get (field.getName ()));
                    } else {
                        value = requestMap.get (field.getName ());
                    }
                    field.set (message, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace ();
                }
            }
            clazz = clazz.getSuperclass ();
        }
        return message;
    }

    private boolean isGodness (String text) {
        if (text == null) {
            return false;
        }
        text = text.trim ().toLowerCase ();
        return (text.equals ("godness") || text.equals ("女神") || text.equals ("nvshen"));
    }

    private String getMainMenu (WeiChatUser user) {
        if (user == null || user.getFlag () != isAuthed) {
            return "";
        }
        StringBuffer buffer = new StringBuffer ();
        buffer.append ("こんにちは、僕は祁です。番号を選択してください。回復の女神がサプライズよ。回復?このメニュー表示。").append ("\n\r\n");
        List<Stage> stages = stageDao.getStagesByCategory (1);
        for (Stage stage : stages) {
            buffer.append ("【");
            buffer.append (stage.getKey ());
            buffer.append ("】 ");
            String des = stage.getDes ();
            des = des.trim ();
            buffer.append (des);
            buffer.append ("\ue417");
            buffer.append ("\n");
        }
        buffer.append ("\r\n");
        return buffer.toString ();
    }

    /**
     * emoji表情转换(hex -> utf-16)
     *
     * @param hexEmoji
     * @return
     */
    public static String emoji (int hexEmoji) {
        return String.valueOf (Character.toChars (hexEmoji));
    }

    private boolean isLiYou (String name) {
        if (name == null) {
            return false;
        }
        if ((name = name.toLowerCase ()).equals ("liyou")) {
            return true;
        } else if (name.equals ("李尤")) {
            return true;
        } else if (name.equals ("小尤尤")) {
            return true;
        } else if (name.equals ("yoyo")) {
            return true;
        } else if (name.equals ("yoyo_littlepig")) {
            return true;
        } else if (name.equals ("尤尤")) {
            return true;
        } else if (name.equals ("lu")) {
            return true;
        }
        return false;
    }

    private BaseResponseMessage processMenu (String content, WeiChatUser weiChatUser) {
        if (content == null) {
            return null;
        }
        content = content.trim ();
        System.out.println (content);
        TextResponseMessage textResponseMessage = new TextResponseMessage ();
        if (content.equals ("?") || content.equals ("？")) {
            textResponseMessage.setContent (getMainMenu (weiChatUser));
            return textResponseMessage;
        }
        Stage stage = weiChatUser.getStage ();
        if ((stage == null || stage.getId () == 1) && content.equals ("0")) {
            //we know we are in normal mode
            textResponseMessage.setContent ("哇哦，我们还没有选择模式哦，尤尤调皮啦……");
        } else {
            //we should handle the menu
            if (content.equals ("0")) {
                textResponseMessage.setContent ("HoHo,退出模式【" + stage.getDes () + "】\r\n" + getMainMenu (weiChatUser));
                stage = stageDao.getStagesByCategoryAndKey (1, "0");
                weiChatUser.setStage (stage);
                userDao.update (weiChatUser);
            } else {
                stage = stageDao.getStagesByCategoryAndKey (1, content);
                if (stage != null) {
                    textResponseMessage.setContent ("哇哦 尤尤选择了【" + stage.getDes () + "】，输入0退出?显示菜单。");
                    weiChatUser.setStage (stage);
                    userDao.update (weiChatUser);
                    String key = stage.getKey ();
                    if (key.equals ("1")) {
                        textResponseMessage.setContent ("小尤的亲戚……输入任意非命令字符显示周期哦");
                        //生理周期
                    } else if (key.equals ("2")) {
                        //运程
                        try {
                            Constellation constellation = new Constellation (2);
                            StringBuilder stringBuilder = new StringBuilder ();
                            List<Constellation.Item> items = constellation.getItems ();
                            if (items == null || items.size () == 0) {

                            } else {
                                stringBuilder.append (constellation.getConstellation ());
                                stringBuilder.append ("\r\n");
                                stringBuilder.append (constellation.getDate ());
                                stringBuilder.append ("\r\n");
                                for (Constellation.Item item : items) {
                                    stringBuilder.append (item.getTitle ());
                                    stringBuilder.append ("\r\n");
                                    if (item.getRank () == 0) {
                                        stringBuilder.append (item.getValue ());
                                    } else {
                                        stringBuilder.append (item.getRankString (item.getRank ()));
                                    }
                                    stringBuilder.append ("\r\n");
                                }
                            }
                            textResponseMessage.setContent (stringBuilder.toString ());
                        } catch (IOException e) {
                            System.err.println (e.toString ());
                            textResponseMessage.setContent (respContent);
                        }
                    } else if (key.equals ("3")) {
                        try {
                            List<Article> articles = new ArrayList<Article> (8);
                            Weather nanjing = new Weather ("南京");
                            List<Weather.WeatherInfo> infos = nanjing.getWeatherInfos ();
                            if (infos == null) {
                                Article article = new Article ();
                                article.setTitle (" 小尤の天气--南京   " + nanjing.getDate ());
                                article.setPicUrl (YoYoUtil.WEBSITE_URL + "cards/travel");
                                article.setDescription ("oops!");
                                article.setUrl (YoYoUtil.WEBSITE_URL + "cards/loveuu/travel/李尤/");
                                articles.add (article);
                            } else {
                                Article article = new Article ();
                                article.setTitle (" 小尤の天气--南京   ");
                                article.setPicUrl ("");
                                article.setDescription ("");
                                article.setUrl (YoYoUtil.WEBSITE_URL + "cards/loveuu/travel/李尤/");
                                articles.add (article);
                                for (Weather.WeatherInfo info : infos) {
                                    article = new Article ();
                                    article.setTitle (info.getDate () + "\r\n" + info.getWeather () + " " + info.getTemperature ());
                                    article.setPicUrl (info.getDayPictureUrl ());
                                    article.setDescription (info.getDate () + "\r\n" + info.getWeather () + " " + info.getTemperature ());
                                    article.setUrl (YoYoUtil.WEBSITE_URL + "cards/loveuu/travel/李尤/");
                                    articles.add (article);
                                }
                                Weather dehui = new Weather ("德惠");
                                infos = dehui.getWeatherInfos ();
                                article = new Article ();
                                article.setTitle (" 小尤の天气--德惠   ");
                                article.setPicUrl ("");
                                article.setDescription ("");
                                article.setUrl (YoYoUtil.WEBSITE_URL + "cards/loveuu/travel/李尤/");
                                articles.add (article);
                                for (Weather.WeatherInfo info : infos) {
                                    article = new Article ();
                                    article.setTitle (info.getDate () + "\r\n" + info.getWeather () + " " + info.getTemperature ());
                                    article.setPicUrl (info.getDayPictureUrl ());
                                    article.setDescription (info.getDate () + "\r\n" + info.getWeather () + " " + info.getTemperature ());
                                    article.setUrl (YoYoUtil.WEBSITE_URL + "cards/loveuu/travel/李尤/");
                                    articles.add (article);
                                }
                            }
                            NewsResponseMessage newsResponseMessage = new NewsResponseMessage ();
                            newsResponseMessage.setArticles (articles);
                            return newsResponseMessage;
                        } catch (IOException e) {
                            textResponseMessage.setContent (respContent);
                        }
                    } else if (key.equals ("4")) {
                        textResponseMessage.setContent ("输入任何人姓名获得各种卡哦！如李尤……");
                        return textResponseMessage;
                        //图片
                    } else if (key.equals ("5")) {
                        textResponseMessage.setContent ("查看小尤的资料哦！");
                        return textResponseMessage;
                        //资料
                    } else if (key.equals ("6")) {
                        textResponseMessage.setContent ("小尤的最可爱的一面！");
                        return textResponseMessage;
                        //可爱
                    } else if (key.equals ("7")) {
                        textResponseMessage.setContent ("小尤喜欢的东东……");
                        return textResponseMessage;
                        //喜欢
                    } else if (key.equals ("8")) {
                        textResponseMessage.setContent ("小尤的闺蜜啊……");
                        return textResponseMessage;
                        //朋友
                    } else if (key.equals ("9")) {
                        textResponseMessage.setContent ("小尤的家人啊……");
                        //的家
                    } else {
                        return null;
                    }
                } else {
                    stage = weiChatUser.getStage ();
                    if (stage == null || stage.getId () == 1) {
                        return null;
                    }
                    //handle the menu actually
                    String key = stage.getKey ();
                    if (key.equals ("1")) {
                        Date now = new Date ();
                        List<Aunties> auntiesList = auntiesDao.queryByTime (now, 3);
                        Date lastDate = null;
                        StringBuilder stringBuilder = new StringBuilder ();
                        int avag = 0;
                        int used = 0;
                        stringBuilder.append ("小尤近" + auntiesList.size () + "次亲戚来的时间哦：\r\n\r");
                        for (Aunties aunties : auntiesList) {
                            lastDate = aunties.getAuntDate ();
                            String dateStr = DATE_FORMAT.format (lastDate);
                            stringBuilder.append (dateStr);
                            if (aunties.getIntervalDate () != 0) {
                                stringBuilder.append ("----距离上次时间" + aunties.getIntervalDate () + "天");
                                used++;
                            }

                            avag += aunties.getIntervalDate ();
                            stringBuilder.append ("\r\n\r");
                        }
                        avag /= used;
                        long expire = (now.getTime () - lastDate.getTime ()) / 24 / 60 / 60 / 1000;
                        stringBuilder.append ("**********************\r\n\r");
                        stringBuilder.append ("距离上次已过去").append (expire).append ("天,距离下一次大概还有").append (avag - expire).append ("天\r\n");
                        if (expire >= avag - 5) {
                            stringBuilder.append ("小尤注意哦，亲戚要来了哦，注意身体哦！！！\r\n");
                        }
                        stringBuilder.append ("祁麻麻のTips:\r\n（1）给自己的身体加温，可以用热水袋暖自己的肚子，让肚子不受寒\r\n（2）可以用热水加红糖\r\n（3）把生姜、大枣放在锅中加水煮沸，然后加入鸡蛋喝下去也可以减缓疼痛\r\n\r");
                        stringBuilder.append ("<a href=\"http://www.dayima.com/?var=mobile\">大姨吗</a>");
                        textResponseMessage.setContent (stringBuilder.toString ());
                        return textResponseMessage;
                        //生理周期
                    } else if (key.equals ("2")) {
                        //运程
                        try {
                            int value = 2;
                            if (content.startsWith ("xz")) {
                                content = content.replace ("xz", "0");
                                try {
                                    value = Integer.parseInt (content);
                                } catch (Exception ex) {
                                    String response = Constellation.getConstellationString ();
                                    response = "请输入序号选择星座如xz2为我家小尤的双子座\r\n" + response;
                                    textResponseMessage.setContent (response);
                                    return textResponseMessage;
                                }
                                if (value < 0 || value > 11) {
                                    String response = Constellation.getConstellationString ();
                                    response = "请输入序号选择星座如xz2为我家小尤的双子座\r\n" + response;
                                    textResponseMessage.setContent (response);
                                    return textResponseMessage;
                                }
                            } else {
                                String response = Constellation.getConstellationString ();
                                response = "请输入序号选择星座如xz2为我家小尤的双子座\r\n" + response;
                                textResponseMessage.setContent (response);
                                return textResponseMessage;
                            }
                            Constellation constellation = new Constellation (value);
                            StringBuilder stringBuilder = new StringBuilder ();
                            List<Constellation.Item> items = constellation.getItems ();
                            if (items == null || items.size () == 0) {
                                String response = Constellation.getConstellationString ();
                                response = "请输入序号选择星座如xz2为我家小尤的双子座\r\n" + response;
                                textResponseMessage.setContent (response);
                                return textResponseMessage;
                            } else {
                                stringBuilder.append (constellation.getConstellation ());
                                stringBuilder.append ("\n");
                                stringBuilder.append (constellation.getDate ());
                                stringBuilder.append ("\n");
                                for (Constellation.Item item : items) {
                                    stringBuilder.append (item.getTitle ());
                                    stringBuilder.append ("\n");
                                    if (item.getRank () == 0) {
                                        stringBuilder.append (item.getValue ());
                                    } else {
                                        stringBuilder.append (item.getRankString (item.getRank ()));
                                    }
                                    stringBuilder.append ("\n");
                                }
                            }
                            textResponseMessage.setContent (stringBuilder.toString ());
                            return textResponseMessage;
                        } catch (IOException e) {
                            textResponseMessage.setContent (respContent);
                        }
                    } else if (key.equals ("3")) {
                        //天气
                        try {
                            List<Article> articles = new ArrayList<Article> (4);
                            Weather weather = new Weather (content);
                            List<Weather.WeatherInfo> infos = weather.getWeatherInfos ();
                            if (infos == null) {
                                textResponseMessage.setContent ("Sorry 哦，没找到尤要的城市哎！");
                                return textResponseMessage;
                            } else {
                                Article article = new Article ();
                                article.setTitle (" 小尤の天气--" + weather.getCity ());
                                article.setPicUrl ("");
                                article.setDescription ("");
                                article.setUrl (YoYoUtil.WEBSITE_URL + "cards/loveuu/travel/李尤/");
                                articles.add (article);
                                for (Weather.WeatherInfo info : infos) {
                                    article = new Article ();
                                    article.setTitle (info.getDate () + "\r\n" + info.getWeather () + " " + info.getTemperature ());
                                    article.setPicUrl (info.getDayPictureUrl ());
                                    article.setDescription (info.getDate () + "\r\n" + info.getWeather () + " " + info.getTemperature ());
                                    article.setUrl (YoYoUtil.WEBSITE_URL + "cards/loveuu/travel/李尤/");
                                    articles.add (article);
                                }
                            }
                            NewsResponseMessage newsResponseMessage = new NewsResponseMessage ();
                            newsResponseMessage.setArticles (articles);
                            return newsResponseMessage;
                        } catch (IOException e) {
                            textResponseMessage.setContent (respContent);

                        }
                    } else if (key.equals ("4")) {
                        //图片
                        int size = CardController.cards.size ();
                        int randomNum = random.nextInt ();
                        randomNum = Math.abs (randomNum) % size;
                        String card = CardController.cards.get (randomNum);
                        List<Article> articles = new ArrayList<Article> (1);
                        Article article = new Article ();
                        article.setTitle (content + "の专属" + card + "卡");
                        article.setPicUrl (YoYoUtil.WEBSITE_URL + "cards/" + card);
                        article.setUrl (YoYoUtil.WEBSITE_URL + "cards/loveuu/" + card + "/" + content + "/" + System.currentTimeMillis ());
                        article.setDescription ("出示本卡可令祁麻麻" + getTitle (randomNum) + "一次。仅限" + content + "使用。有效期forever~");
                        articles.add (article);
                        NewsResponseMessage newsResponseMessage = new NewsResponseMessage ();
                        newsResponseMessage.setArticles (articles);
                        return newsResponseMessage;
                    } else if (key.equals ("5")) {
                        //资料
                    } else if (key.equals ("6")) {
                        //可爱
                    } else if (key.equals ("7")) {
                        //喜欢
                    } else if (key.equals ("8")) {
                        //朋友
                    } else if (key.equals ("9")) {
                        //的家
                    } else {

                    }
                }
            }
        }
        return textResponseMessage;
    }

    public static String getTitle (int kind) {
        String title;
        switch (kind) {
            case 0:
                title = "打扫房间";
                break;
            case 1:
                title = "洗衣服";
                break;
            case 2:
                title = "做饭刷碗";
                break;
            case 3:
                title = "讲笑话";
                break;
            case 4:
                title = "陪逛街";
                break;
            case 5:
                title = "一起旅行";
                break;
            default:
                title = "各种玩";
                break;
        }
        return title;
    }

}

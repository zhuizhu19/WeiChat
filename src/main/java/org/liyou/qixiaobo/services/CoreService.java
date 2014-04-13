package org.liyou.qixiaobo.services;

import org.apache.log4j.Logger;
import org.liyou.qixiaobo.controllers.CardController;
import org.liyou.qixiaobo.daos.StageDao;
import org.liyou.qixiaobo.daos.UserDao;
import org.liyou.qixiaobo.entities.hibernate.Hero;
import org.liyou.qixiaobo.entities.hibernate.Skill;
import org.liyou.qixiaobo.entities.hibernate.Stage;
import org.liyou.qixiaobo.entities.hibernate.WeiChatUser;
import org.liyou.qixiaobo.entities.weichat.request.*;
import org.liyou.qixiaobo.entities.weichat.response.Article;
import org.liyou.qixiaobo.entities.weichat.response.BaseResponseMessage;
import org.liyou.qixiaobo.entities.weichat.response.NewsResponseMessage;
import org.liyou.qixiaobo.entities.weichat.response.TextResponseMessage;
import org.liyou.qixiaobo.utils.MessageUtil;
import org.liyou.qixiaobo.utils.YoYoUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
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
    private Random random = new Random ();
    private static int TEXT_LENGTH = 7;
    private static int isAuthed = 1;

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
            String respContent = "请求处理异常，请稍候尝试！";
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
                    if (weiChatUser.getFlag () == isAuthed && (responseMessage = processMenu (textRequestMessage.getContent (), weiChatUser)) != null) {

                    } else {
                        responseMessage = new NewsResponseMessage ();
                        List<Article> articles = new ArrayList<Article> ();
                        NewsResponseMessage newsResponseMessage = (NewsResponseMessage) responseMessage;
                        if (weiChatUser.getFlag () == isAuthed && isGodness (textRequestMessage.getContent ())) {
                            Article article = new Article ();
                            article.setTitle ("我的女神哎");
                            article.setPicUrl (YoYoUtil.PIC_PAGE_FACE);
                            article.setUrl (YoYoUtil.PIC_GODNESS);
                            article.setDescription ("我的女神哎");
                            articles.add (article);
                            newsResponseMessage.setArticles (articles);
                        } else {
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
                                    article.setTitle (textRequestMessage.getContent () + "の" + card + "卡");
                                    article.setPicUrl (YoYoUtil.WEBSITE_URL + "cards/" + card);
                                    article.setUrl (YoYoUtil.WEBSITE_URL + "cards/loveuu/" + card + "/" + textRequestMessage.getContent () + "/" + System.currentTimeMillis ());
                                    article.setDescription (textRequestMessage.getContent () + "の" + card + "卡");
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
                    }


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
            buffer.append (stage.getKey ());
            buffer.append ("　");//append 全角空格
            String des = stage.getDes ();
            if (des.length () <= TEXT_LENGTH) {
                for (int i = 0; i < TEXT_LENGTH - des.length (); i++) {
                    buffer.append (" ");
                }
            } else {
                des = des.substring (0, TEXT_LENGTH);
            }
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
        }
        return false;
    }

    private BaseResponseMessage processMenu (String content, WeiChatUser weiChatUser) {
        if (content == null) {
            return null;
        }
        content = content.trim ();
        TextResponseMessage textResponseMessage = new TextResponseMessage ();
        if (content.equals ("?")) {
            textResponseMessage.setContent (getMainMenu (weiChatUser));
        }
        Stage stage = weiChatUser.getStage ();
        if (stage == null || stage.getId () == 1) {
            //we know we are in normal mode
            if (content.equals ("0")) {
                textResponseMessage.setContent ("哇哦，我们还没有选择模式哦，尤尤调皮啦……");
            } else {
                stage = stageDao.getStagesByCategoryAndKey (1, content);
                if (stage != null) {
                    textResponseMessage.setContent ("哇哦 尤尤选择了【" + stage.getDes () + "】，输入0退出，?显示菜单。");
                    weiChatUser.setStage (stage);
                    userDao.update (weiChatUser);
                } else {
                    return null;
                }
            }
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
                    textResponseMessage.setContent ("哇哦 尤尤选择了【" + stage.getDes () + "】，输入0退出，?显示菜单。");
                } else {
                    //handle the menu actually
                    String key = stage.getKey ();
                    if (key.equals ("1")) {
                        //生理周期
                    } else if (key.equals ("2")) {
                        //运程
                    } else if (key.equals ("3")) {
                        //天气
                    } else if (key.equals ("4")) {
                        //图片
                        int size = CardController.cards.size ();
                        int randomNum = random.nextInt ();
                        randomNum = Math.abs (randomNum) % size;
                        String card = CardController.cards.get (randomNum);
                        List<Article> articles = new ArrayList<Article> (1);
                        Article article = new Article ();
                        article.setTitle (content + "の" + card + "卡");
                        article.setPicUrl (YoYoUtil.WEBSITE_URL + "cards/" + card);
                        article.setUrl (YoYoUtil.WEBSITE_URL + "cards/loveuu/" + card + "/" + content + "/" + System.currentTimeMillis ());
                        article.setDescription (content + "の" + card + "卡");
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
}

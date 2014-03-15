package org.liyou.qixiaobo.services;

import org.apache.log4j.Logger;
import org.liyou.qixiaobo.controllers.CardController;
import org.liyou.qixiaobo.entities.hibernate.Hero;
import org.liyou.qixiaobo.entities.hibernate.Skill;
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
    private Random random = new Random ();

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
            logger.info (baseEvent.toString ());
            if (baseEvent instanceof BaseRequestMessage) {
                if (baseEvent instanceof TextRequestMessage) {
                    TextRequestMessage textRequestMessage = (TextRequestMessage) baseEvent;
                    responseMessage = new NewsResponseMessage ();
                    List<Article> articles = new ArrayList<Article> ();
                    NewsResponseMessage newsResponseMessage = (NewsResponseMessage) responseMessage;
                    if (isGodness (textRequestMessage.getContent ())) {
                        Article article = new Article ();
                        article.setTitle ("我的女神哎");
                        article.setPicUrl (YoYoUtil.PIC_PAGE_FACE);
                        article.setUrl (YoYoUtil.PIC_GODNESS);
                        article.setDescription ("我的女神哎");
                        articles.add (article);
                    } else {
                        List<Hero> models = dotaService.searchHeros (textRequestMessage.getContent ());
                        if (DotaService.complete && models != null && models.size () != 0) {
                            if (models.size () == 1) {
                                Hero model = models.get (0);
                                Article article = new Article ();
                                article.setTitle (model.getName ());
                                article.setPicUrl (model.getImgUrl ());
                                article.setUrl (model.getUrl ());
                                article.setDescription (model.getDes ());
                                articles.add (article);
                                for (Skill skill : model.getSkills ()) {
                                    Article art = new Article ();
                                    art.setTitle (skill.getSkillName ());
                                    art.setUrl (skill.getSkillUrl ());
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
                        } else {
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
                        }
                    }

                    newsResponseMessage.setArticles (articles);
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
//            // 发送方帐号（open_id）
//            String fromUserName = requestMap.get ("FromUserName");
//            // 公众帐号
//            String toUserName = requestMap.get ("ToUserName");
//            // 消息类型
//            String msgType = requestMap.get ("MsgType");
//
//            // 回复消息
//            BaseResponseMessage message = null;
//
//            // 文本消息
//            if (msgType.equals (MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
//                String content = requestMap.get ("Content");
//                NewsResponseMessage newsMessage = new NewsResponseMessage ();
//                List<Article> articleList = new ArrayList<Article> ();
//                sArticle.setTitle ("您发送的是文本消息！");
//                articleList.add (sArticle);
//                // 设置图文消息个数
//                newsMessage.setArticleCount (articleList.size ());
//                // 设置图文消息包含的图文集合
//                newsMessage.setArticles (articleList);
//                // 将图文消息对象转换成xml字符串
//                respMessage = MessageUtil.newsMessageToXml (newsMessage);
//                message = newsMessage;
//            }
//            // 图片消息
//            else if (msgType.equals (MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
//                NewsResponseMessage newsMessage = new NewsResponseMessage ();
//                List<Article> articleList = new ArrayList<Article> ();
//                sArticle.setTitle ("您发送的是图片消息！");
//                articleList.add (sArticle);
//                // 设置图文消息个数
//                newsMessage.setArticleCount (articleList.size ());
//                // 设置图文消息包含的图文集合
//                newsMessage.setArticles (articleList);
//                // 将图文消息对象转换成xml字符串
//                respMessage = MessageUtil.newsMessageToXml (newsMessage);
//                message = newsMessage;
//                respContent = "您发送的是图片消息！";
//            }
//            // 地理位置消息
//            else if (msgType.equals (MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
//                NewsResponseMessage newsMessage = new NewsResponseMessage ();
//                List<Article> articleList = new ArrayList<Article> ();
//                sArticle.setTitle ("您发送的是地理位置消息！");
//                articleList.add (sArticle);
//                // 设置图文消息个数
//                newsMessage.setArticleCount (articleList.size ());
//                // 设置图文消息包含的图文集合
//                newsMessage.setArticles (articleList);
//                // 将图文消息对象转换成xml字符串
//                respMessage = MessageUtil.newsMessageToXml (newsMessage);
//                message = newsMessage;
//            }
//            // 链接消息
//            else if (msgType.equals (MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
//                NewsResponseMessage newsMessage = new NewsResponseMessage ();
//                List<Article> articleList = new ArrayList<Article> ();
//                sArticle.setTitle ("您发送的是链接消息！");
//                articleList.add (sArticle);
//                // 设置图文消息个数
//                newsMessage.setArticleCount (articleList.size ());
//                // 设置图文消息包含的图文集合
//                newsMessage.setArticles (articleList);
//                // 将图文消息对象转换成xml字符串
//                respMessage = MessageUtil.newsMessageToXml (newsMessage);
//                message = newsMessage;
//            }
//            // 音频消息
//            else if (msgType.equals (MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
//                NewsResponseMessage newsMessage = new NewsResponseMessage ();
//                List<Article> articleList = new ArrayList<Article> ();
//                sArticle.setTitle ("您发送的是音频消息！");
//                articleList.add (sArticle);
//                // 设置图文消息个数
//                newsMessage.setArticleCount (articleList.size ());
//                // 设置图文消息包含的图文集合
//                newsMessage.setArticles (articleList);
//                // 将图文消息对象转换成xml字符串
//                respMessage = MessageUtil.newsMessageToXml (newsMessage);
//                message = newsMessage;
//                respContent = "您发送的是音频消息！";
//            }
//            // 事件推送
//            else if (msgType.equals (MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
//                // 事件类型
//                String eventType = requestMap.get ("Event");
//                // 订阅
//                if (eventType.equals (MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
//
//                    NewsResponseMessage newsMessage = new NewsResponseMessage ();
//                    List<Article> articleList = new ArrayList<Article> ();
//                    sArticle.setTitle ("谢谢您的关注！");
//                    articleList.add (sArticle);
//                    // 设置图文消息个数
//                    newsMessage.setArticleCount (articleList.size ());
//                    // 设置图文消息包含的图文集合
//                    newsMessage.setArticles (articleList);
//                    // 将图文消息对象转换成xml字符串
//                    respMessage = MessageUtil.newsMessageToXml (newsMessage);
//                    message = newsMessage;
//                }
//                // 取消订阅
//                else if (eventType.equals (MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
//                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
//                }
//                // 自定义菜单点击事件
//                else if (eventType.equals (MessageUtil.EVENT_TYPE_CLICK)) {
//                    // TODO 自定义菜单权没有开放，暂不处理该类消息
//                }
//            }
//            message.setToUserName (fromUserName);
//            message.setFromUserName (toUserName);
//            message.setCreateTime (new Date ().getTime ());
//            message.setFuncFlag (0);
//            respMessage = MessageUtil.messageToXml (message);
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

}

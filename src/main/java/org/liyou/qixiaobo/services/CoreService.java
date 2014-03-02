package org.liyou.qixiaobo.services;

import org.liyou.qixiaobo.entities.response.Article;
import org.liyou.qixiaobo.entities.response.BaseMessage;
import org.liyou.qixiaobo.entities.response.NewsMessage;
import org.liyou.qixiaobo.utils.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 14-3-1.
 */
public class CoreService {
    /**
     * 处理微信发来的请求
     *
     * @param request
     * @return
     */

    static Article sArticle ;
    static{
        sArticle= new Article ();
        sArticle.setTitle ("我的微博");
        sArticle.setDescription ("我从星星来，要到星星去！");
        sArticle.setPicUrl ("http://tp4.sinaimg.cn/2791610843/180/40043803902/1");
        sArticle.setUrl ("http://weibo.com/2791610843/profile?rightmod=1&wvr=5&mod=personinfo");
    }
    public static String processRequest (HttpServletRequest request) {
        String respMessage = null;
        try {
            // 默认返回的文本消息内容
            String respContent = "请求处理异常，请稍候尝试！";

            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml (request);

            // 发送方帐号（open_id）
            String fromUserName = requestMap.get ("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get ("ToUserName");
            // 消息类型
            String msgType = requestMap.get ("MsgType");

            // 回复消息
            BaseMessage message = null;

            // 文本消息
            if (msgType.equals (MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {
                NewsMessage newsMessage = new NewsMessage ();
                List<Article> articleList = new ArrayList<Article> ();
                sArticle.setTitle ("您发送的是文本消息！");
                articleList.add (sArticle);
                // 设置图文消息个数
                newsMessage.setArticleCount (articleList.size ());
                // 设置图文消息包含的图文集合
                newsMessage.setArticles (articleList);
                // 将图文消息对象转换成xml字符串
                respMessage = MessageUtil.newsMessageToXml (newsMessage);
                message = newsMessage;
            }
            // 图片消息
            else if (msgType.equals (MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
                NewsMessage newsMessage = new NewsMessage ();
                List<Article> articleList = new ArrayList<Article> ();
                sArticle.setTitle ("您发送的是图片消息！");
                articleList.add (sArticle);
                // 设置图文消息个数
                newsMessage.setArticleCount (articleList.size ());
                // 设置图文消息包含的图文集合
                newsMessage.setArticles (articleList);
                // 将图文消息对象转换成xml字符串
                respMessage = MessageUtil.newsMessageToXml (newsMessage);
                message = newsMessage;
                respContent = "您发送的是图片消息！";
            }
            // 地理位置消息
            else if (msgType.equals (MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
                NewsMessage newsMessage = new NewsMessage ();
                List<Article> articleList = new ArrayList<Article> ();
                sArticle.setTitle ("您发送的是地理位置消息！");
                articleList.add (sArticle);
                // 设置图文消息个数
                newsMessage.setArticleCount (articleList.size ());
                // 设置图文消息包含的图文集合
                newsMessage.setArticles (articleList);
                // 将图文消息对象转换成xml字符串
                respMessage = MessageUtil.newsMessageToXml (newsMessage);
                message = newsMessage;
            }
            // 链接消息
            else if (msgType.equals (MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
                NewsMessage newsMessage = new NewsMessage ();
                List<Article> articleList = new ArrayList<Article> ();
                sArticle.setTitle ("您发送的是链接消息！");
                articleList.add (sArticle);
                // 设置图文消息个数
                newsMessage.setArticleCount (articleList.size ());
                // 设置图文消息包含的图文集合
                newsMessage.setArticles (articleList);
                // 将图文消息对象转换成xml字符串
                respMessage = MessageUtil.newsMessageToXml (newsMessage);
                message = newsMessage;
            }
            // 音频消息
            else if (msgType.equals (MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {
                NewsMessage newsMessage = new NewsMessage ();
                List<Article> articleList = new ArrayList<Article> ();
                sArticle.setTitle ("您发送的是音频消息！");
                articleList.add (sArticle);
                // 设置图文消息个数
                newsMessage.setArticleCount (articleList.size ());
                // 设置图文消息包含的图文集合
                newsMessage.setArticles (articleList);
                // 将图文消息对象转换成xml字符串
                respMessage = MessageUtil.newsMessageToXml (newsMessage);
                message = newsMessage;
                respContent = "您发送的是音频消息！";
            }
            // 事件推送
            else if (msgType.equals (MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
                // 事件类型
                String eventType = requestMap.get ("Event");
                // 订阅
                if (eventType.equals (MessageUtil.EVENT_TYPE_SUBSCRIBE)) {

                    NewsMessage newsMessage = new NewsMessage ();
                    List<Article> articleList = new ArrayList<Article> ();
                    sArticle.setTitle ("谢谢您的关注！");
                    articleList.add (sArticle);
                    // 设置图文消息个数
                    newsMessage.setArticleCount (articleList.size ());
                    // 设置图文消息包含的图文集合
                    newsMessage.setArticles (articleList);
                    // 将图文消息对象转换成xml字符串
                    respMessage = MessageUtil.newsMessageToXml (newsMessage);
                    message = newsMessage;
                }
                // 取消订阅
                else if (eventType.equals (MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                }
                // 自定义菜单点击事件
                else if (eventType.equals (MessageUtil.EVENT_TYPE_CLICK)) {
                    // TODO 自定义菜单权没有开放，暂不处理该类消息
                }
            }
            message.setToUserName (fromUserName);
            message.setFromUserName (toUserName);
            message.setCreateTime (new Date ().getTime ());
            message.setFuncFlag (0);
            respMessage = MessageUtil.messageToXml (message);
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return respMessage;
    }
}

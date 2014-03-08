package org.liyou.qixiaobo.entities.weichat.response;

import org.liyou.qixiaobo.utils.MessageUtil;

import java.util.List;

/**
 * Created by Administrator on 14-3-1.
 */
public class NewsResponseMessage extends BaseResponseMessage {
    public NewsResponseMessage () {
        this.setMsgType (MessageUtil.RESP_MESSAGE_TYPE_NEWS);
    }

    // 图文消息个数，限制为10条以内
    private int ArticleCount;
    // 多条图文消息信息，默认第一个item为大图
    private List<Article> Articles;

    public int getArticleCount () {
        return ArticleCount;
    }

    private NewsResponseMessage setArticleCount (int articleCount) {
        ArticleCount = articleCount;
        return this;
    }

    public List<Article> getArticles () {
        return Articles;
    }

    public NewsResponseMessage setArticles (List<Article> articles) {
        Articles = articles;
        setArticleCount (articles.size ());
        return this;
    }
}

package org.liyou.qixiaobo.entities.response;

import org.liyou.qixiaobo.utils.MessageUtil;

import java.util.List;

/**
 * Created by Administrator on 14-3-1.
 */
public class NewsMessage extends BaseMessage {
    public NewsMessage () {
        this.setMsgType (MessageUtil.RESP_MESSAGE_TYPE_NEWS);
    }

    // 图文消息个数，限制为10条以内
    private int ArticleCount;
    // 多条图文消息信息，默认第一个item为大图
    private List<Article> Articles;

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    public List<Article> getArticles() {
        return Articles;
    }

    public void setArticles(List<Article> articles) {
        Articles = articles;
    }
}

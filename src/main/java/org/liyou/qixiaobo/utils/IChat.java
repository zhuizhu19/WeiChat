package org.liyou.qixiaobo.utils;

/**
 * Created by Administrator on 14-4-22.
 */
public interface IChat {
    public String chat (String fromUserId, String chatContent, int type, java.util.Map<String, Object> entities);
}

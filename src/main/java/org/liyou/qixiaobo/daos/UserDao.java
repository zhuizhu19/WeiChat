package org.liyou.qixiaobo.daos;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.liyou.qixiaobo.entities.hibernate.WeiChatUser;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 14-4-12.
 */
@Service
@Transactional
public class UserDao extends BaseDao<WeiChatUser> {
    @Transactional
    public WeiChatUser getWeiChatUserByFromUserName (String fromUserName) {
        Session session = sessionFacotry.getCurrentSession ();
        Transaction tx = session.beginTransaction ();
        Criteria criteria = session.createCriteria (WeiChatUser.class);
        criteria.add (Restrictions.eq ("fromUserName", fromUserName));
        criteria.setMaxResults (1);
        List<WeiChatUser> weiChatUsers = criteria.list ();
        tx.commit ();
        if (weiChatUsers == null || weiChatUsers.size () == 0)
            return null;
        return weiChatUsers.get (0);
    }
}

package org.liyou.qixiaobo.daos;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.liyou.qixiaobo.entities.hibernate.Evernote;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 14-5-4.
 */
@Service
@Transactional
public class EvernoteDao extends BaseDao<Evernote> {
    public List<Evernote> queryAllNotes () {
        List<Evernote> evernotes = null;
        Session session = sessionFacotry.getCurrentSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Evernote.class);
        criteria.addOrder(Order.asc("id"));
        evernotes = criteria.list();
        tx.commit();
        return evernotes;
    }
}

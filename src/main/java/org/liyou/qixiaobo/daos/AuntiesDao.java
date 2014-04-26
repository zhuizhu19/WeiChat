package org.liyou.qixiaobo.daos;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.liyou.qixiaobo.entities.hibernate.Aunties;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 14-4-15.
 */
@Service
@Transactional
public class AuntiesDao extends BaseDao<Aunties> {
    @Transactional
    public List<Aunties> queryByTime (Date date, int nums) {
        Session session = sessionFacotry.getCurrentSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Aunties.class);
        criteria.add(Restrictions.le("auntDate", date));
        criteria.setMaxResults(nums);
        List<Aunties> aunties = criteria.list();
        tx.commit();
        return aunties;
    }
}

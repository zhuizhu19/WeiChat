package org.liyou.qixiaobo.daos;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.liyou.qixiaobo.entities.hibernate.Stage;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 14-4-12.
 */
@Service
@Transactional
public class StageDao extends BaseDao<Stage> {
    @Transactional
    public List<Stage> getStagesByCategory (Integer categoryId) {
        Session session = sessionFacotry.getCurrentSession ();
        Transaction tx = session.beginTransaction ();
        Criteria criteria = session.createCriteria (Stage.class);
//        criteria.add (Restrictions.eq ("category", categoryId));
        List<Stage> stages = criteria.list ();
        tx.commit ();
        return stages;
    }
    @Transactional
    public Stage getStagesByCategoryAndKey (Integer categoryId,String key) {
        Session session = sessionFacotry.getCurrentSession ();
        Transaction tx = session.beginTransaction ();
        Criteria criteria = session.createCriteria (Stage.class);
//        criteria.add (Restrictions.eq ("category", categoryId));
        criteria.add (Restrictions.eq ("key",key));
        List<Stage> stages = criteria.list ();
        tx.commit ();
        if (stages == null || stages.size () == 0)
            return null;
        return stages.get (0);
    }
}

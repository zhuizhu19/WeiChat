package org.liyou.qixiaobo.daos;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.liyou.qixiaobo.entities.hibernate.Hero;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 14-3-8.
 */
@Service
@Transactional
public class HeroDao extends BaseDao<Hero> {
    @Transactional
    public Hero queryByHeroName (String name) {
        Session session = sessionFacotry.getCurrentSession ();
        Transaction tx = session.beginTransaction ();
        Criteria criteria = session.createCriteria (Hero.class);
        criteria.add (Restrictions.eq ("name", name));
        criteria.setMaxResults (1);
        List<Hero> heros = criteria.list ();
        tx.commit ();
        if (heros == null || heros.size () == 0)
            return null;
        return heros.get (0);
    }

    @Transactional
    public List<Hero> queryByHeroNameOrShortname (String name) {
        Session session = sessionFacotry.openSession ();
        Criteria criteria = session.createCriteria (Hero.class);
        criteria.add (Restrictions.or (Restrictions.like ("name", matchAnyWhere (name), MatchMode.EXACT),
                (Restrictions.like ("shortName", name, MatchMode.ANYWHERE))));
        criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Hero> heros = criteria.list ();
        session.close ();
        if (heros == null || heros.size () == 0)
            return null;
        return heros;
    }

    public int queryHeroNums () {
        return queryNums (Hero.class, null);
    }


}

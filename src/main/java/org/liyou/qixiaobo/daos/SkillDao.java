package org.liyou.qixiaobo.daos;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.liyou.qixiaobo.entities.hibernate.Skill;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Administrator on 14-3-8.
 */
@Service
@Transactional
public class SkillDao extends BaseDao<Skill> {
    @Transactional
    public Skill queryBySkillName (String name) {
        Session session = sessionFacotry.getCurrentSession();
        Transaction tx = session.beginTransaction();
        Criteria criteria = session.createCriteria(Skill.class);
        criteria.add(Restrictions.eq("skillName", name));
        criteria.setMaxResults(1);
        List<Skill> skills = criteria.list();
        tx.commit();
        if (skills == null || skills.size() == 0)
            return null;
        return skills.get(0);
    }
}

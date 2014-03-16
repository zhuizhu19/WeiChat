package org.liyou.qixiaobo.daos;

import org.liyou.qixiaobo.entities.hibernate.HeroDetail;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * Created by Administrator on 14-3-16.
 */
@Service
@Transactional
public class HeroDetailDao extends BaseDao<HeroDetail>{
}

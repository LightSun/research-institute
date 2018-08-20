package com.heaven7.java.springboot.dao;

import com.heaven7.java.springboot.entity.Weibo;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 最原始的
 */
@Repository
public class WeiboDao {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional()
    public List<Weibo> searchWeiboByEm(String username, String weiboText, Date startDate, Date endDate, int pageNo, int pageSize) {
        StringBuffer jpql = new StringBuffer("select w from Weibo w join fetch w.user u left join fetch w.comments c where 1=1 ");
        Map<String,Object> paramMap = new HashMap<>();
        if(!StringUtils.isEmpty(username)){
            jpql.append(" and u.username = :username");
            paramMap.put("username",username);
        }
        if(!StringUtils.isEmpty(weiboText)){
            jpql.append(" and w.weiboText like :weiboText");
            paramMap.put("weiboText","%"+weiboText+"%");
        }
        if(startDate!=null){
            jpql.append(" and w.createDate >= :startDate");
            paramMap.put("startDate",startDate);
        }
        if(endDate != null){
            jpql.append(" and w.createDate <= :endDate");
            paramMap.put("endDate",endDate);
        }

        Query query = entityManager.createQuery(jpql.toString());
        Set<String> keys = paramMap.keySet();
        for (String keyItem : keys) {
            query.setParameter(keyItem,paramMap.get(keyItem));
        }
        return query.setFirstResult(pageNo*pageSize).setMaxResults(pageSize).getResultList();
    }

}




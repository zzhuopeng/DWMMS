package com.cqupt.dao;

import com.cqupt.constant.BaseConstant;
import com.cqupt.constant.CriteriaConstant;
import com.cqupt.constant.FieldConstant;
import com.cqupt.map.FilterMap;
import com.cqupt.map.OrderMap;
import com.cqupt.pagination.Page;
import com.cqupt.util.GenericUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 参考资料：dispatcher
 * HibernateDaoSupport中sessionFactory是通过配置文件注入的。
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class BaseHibernateDao<T> extends HibernateDaoSupport {

    public boolean cacheable=false;

    public void init(boolean cacheable){
        this.cacheable = cacheable;
    }

    /**
     * Autowired为自动装配，到IOC容易中查找并返回该属性
     * @param sessionFactory
     */
    @Autowired
    public void setSF(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }

    protected Class entityClass;

    protected Class getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class clazz) {
        this.entityClass = clazz;
    }

    public BaseHibernateDao() {
        this.entityClass = GenericUtil.getGenericClass(getClass());
    }

    /**
     * 新增实例
     *
     * @param entity
     * @return
     */
    public Serializable save(T entity) {
        return super.getHibernateTemplate().save(entity);
    }

    /**
     * 新增或更新实例
     *
     * @param entity
     * @return
     */
    public void saveOrUpdate(T entity) {
        super.getHibernateTemplate().saveOrUpdate(entity);
    }

    /**
     * 合并实例
     *
     * @param entity
     * @return
     */
    public void merge(T entity) {
        super.getHibernateTemplate().merge(entity);
    }

    /**
     * 批量新增实例
     *
     * @param entities
     */
//    public void saveAll(Collection<T> entities) {
//        super.getHibernateTemplate().saveOrUpdateAll(entities);
//    }

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    public T get(Serializable id) {
        return (T) super.getHibernateTemplate().get(entityClass, id);
    }

    /**
     * 通过queryString查询
     *
     * @param queryString
     * @return
     */
    public List find(String queryString) {
        super.getHibernateTemplate().setCacheQueries(cacheable);
        return super.getHibernateTemplate().find(queryString);
    }

    /**
     * 通过queryString查询
     *
     * @param queryString
     * @param values
     * @return
     */
    public List find(String queryString, Object... values) {
        super.getHibernateTemplate().setCacheQueries(cacheable);
        return super.getHibernateTemplate().find(queryString, values);
    }

    /**
     * 通过Criteria查询
     *
     * @param filterMap
     * @param orderMap
     * @return
     */
    public List<T> find(FilterMap filterMap, OrderMap orderMap) {
        // 获取session
        Session session = super.getHibernateTemplate().getSessionFactory().openSession();

        // 定义criteria
        Criteria criteria = session.createCriteria(entityClass);

        // 获取记录
        this.addCriterion(criteria, filterMap, orderMap);

        criteria.setCacheable(cacheable);

        List<T> list = criteria.list();

        // 释放session
        super.releaseSession(session);

        // 返回
        return list;
    }

    /**
     * 查询所有实例
     *
     * @return
     */
    public List<T> findAll() {
        super.getHibernateTemplate().setCacheQueries(cacheable);
        return super.getHibernateTemplate().loadAll(entityClass);
    }

    /**
     * 通过queryString分页
     *
     * @param select
     * @param from
     * @param where
     * @param order
     * @param start
     * @param limit
     * @return
     */
    public Page page(String select, String from, String where, String order, int start, int limit) {
        // 获取session
        Session session = super.getHibernateTemplate().getSessionFactory().openSession();

        // 定义query1
        Query query1 = session.createQuery("select count(*) " + from + where);
        // 获取记录总数
        query1.setFirstResult(0);
        int totalProperty = Integer.valueOf(String.valueOf(query1.uniqueResult()));

        // 定义query2
        Query query2 = session.createQuery(select + from + where + order);
        // 获取分页记录
        query2.setFirstResult(start);
        query2.setMaxResults(limit);
        List root = query2.list();

        // 释放session
        super.releaseSession(session);

        // 返回
        return new Page(root, totalProperty);
    }

    /**
     * 通过Criteria分页
     *
     * @param filterMap
     * @param orderMap
     * @param start
     * @param limit
     * @return
     */
    public Page page(FilterMap filterMap, OrderMap orderMap, int start, int limit) {
        // 获取session
        Session session = super.getHibernateTemplate().getSessionFactory().openSession();

        // 定义criteria
        Criteria criteria = session.createCriteria(entityClass);

        // 获取记录总数
        this.addCriterion(criteria, filterMap, null);
        criteria.setFirstResult(0);
        criteria.setProjection(Projections.projectionList().add(Projections.rowCount()));
        int totalProperty = Integer.valueOf(String.valueOf(criteria.uniqueResult()));

        // 获取分页记录
        this.addCriterion(criteria, null, orderMap);
        criteria.setFirstResult(start);
        criteria.setMaxResults(limit);
        criteria.setProjection(null);
        List root = criteria.list();

        // 释放session
        super.releaseSession(session);

        // 返回
        return new Page(root, totalProperty);
    }

    /**
     * 修改实例
     *
     * @param entity
     */
    public void update(T entity) {
        super.getHibernateTemplate().update(entity);
    }

    /**
     * 批量修改实例
     *
     * @param entities
     */
//    public void updateAll(Collection<T> entities) {
//        super.getHibernateTemplate().saveOrUpdateAll(entities);
//    }

    /**
     * 删除实例
     *
     * @param entity
     */
    public void delete(T entity) {
        super.getHibernateTemplate().delete(entity);
    }

    /**
     * 通过主键删除实例
     *
     * @param id
     */
    public void delete(Serializable id) {
        this.delete(this.get(id));
    }

    /**
     * 批量删除实例
     *
     * @param entities
     */
    public void deleteAll(Collection<T> entities) {
        super.getHibernateTemplate().deleteAll(entities);
    }

    /**
     * 通过主键批量删除实例
     *
     * @param ids
     */
    public void deleteAll(Serializable[] ids) {
        FilterMap filterMap = new FilterMap();
        filterMap.in(FieldConstant.ID, ids);
        List<T> list = this.find(filterMap, null);
        this.deleteAll(list);
    }

    /**
     * 通过queryString批量删除实例
     *
     * @param queryString
     * @param values
     */
    public void deleteAll(String queryString, Object... values) {
        List list = this.find(queryString, values);
        this.deleteAll(list);
    }

    /**
     * 生成criteria
     *
     * @param criteria
     * @param filterMap
     * @param orderMap
     */
    protected void addCriterion(Criteria criteria, FilterMap filterMap, OrderMap orderMap) {
        if (filterMap != null && !filterMap.isEmpty()) {
            for (String key : filterMap.keySet()) {
                String[] pt = key.split(BaseConstant.SPLIT);
                String propertyName = pt[0];
                String type = pt[1];
                Object value = filterMap.get(key);
                if (type.equals(CriteriaConstant.EQ)) {
                    criteria.add(Restrictions.eq(propertyName, value));
                } else if (type.equals(CriteriaConstant.NE)) {
                    criteria.add(Restrictions.ne(propertyName, value));
                } else if (type.equals(CriteriaConstant.GT)) {
                    criteria.add(Restrictions.gt(propertyName, value));
                } else if (type.equals(CriteriaConstant.GE)) {
                    criteria.add(Restrictions.ge(propertyName, value));
                } else if (type.equals(CriteriaConstant.LT)) {
                    criteria.add(Restrictions.lt(propertyName, value));
                } else if (type.equals(CriteriaConstant.LE)) {
                    criteria.add(Restrictions.le(propertyName, value));
                } else if (type.equals(CriteriaConstant.BETWEEN)) {
                    Map<String, Object> map = (Map<String, Object>) value;
                    Object lo = map.get(CriteriaConstant.LO);
                    Object hi = map.get(CriteriaConstant.HI);
                    criteria.add(Restrictions.between(propertyName, lo, hi));
                } else if (type.equals(CriteriaConstant.LIKE)) {
                    criteria.add(Restrictions.like(propertyName, (String) value, MatchMode.ANYWHERE));
                } else if (type.equals(CriteriaConstant.IN)) {
                    criteria.add(Restrictions.in(propertyName, (Object[]) value));
                } else if (type.equals(CriteriaConstant.ISNULL)) {
                    criteria.add(Restrictions.isNull(propertyName));
                } else if (type.equals(CriteriaConstant.ISNOTNULL)) {
                    criteria.add(Restrictions.isNotNull(propertyName));
                } else if (type.equals(CriteriaConstant.ISEMPTY)) {
                    criteria.add(Restrictions.isEmpty(propertyName));
                } else if (type.equals(CriteriaConstant.ISNOTEMPTY)) {
                    criteria.add(Restrictions.isNotEmpty(propertyName));
                } else if (type.equals(CriteriaConstant.SQL)) {
                    criteria.add(Restrictions.sqlRestriction((String) value));
                }
            }
        }
        if (orderMap != null && !orderMap.isEmpty()) {
            for (String propertyName : orderMap.keySet()) {
                String type = orderMap.get(propertyName);
                if (type.equals(CriteriaConstant.ASC)) {
                    criteria.addOrder(Order.asc(propertyName));
                } else if (type.equals(CriteriaConstant.DESC)) {
                    criteria.addOrder(Order.desc(propertyName));
                }
            }
        }
    }
}

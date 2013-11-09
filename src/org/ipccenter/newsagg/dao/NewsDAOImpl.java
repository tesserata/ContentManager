package org.ipccenter.newsagg.dao;

/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 10.11.13
 * Time: 2:33
 * To change this template use File | Settings | File Templates.
 */

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.ipccenter.newsagg.HibernateUtil;
import org.ipccenter.newsagg.entity.News;

import java.util.List;

public class NewsDAOImpl implements NewsDAO {

    @Override
    public List<News> getProducts() {
        List<News> result = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Criteria criteria = session.createCriteria(News.class);
            result = (List<News>) criteria.list();

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
        return result;
    }

}

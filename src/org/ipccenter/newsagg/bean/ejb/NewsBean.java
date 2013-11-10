package org.ipccenter.newsagg.bean.ejb;

import org.ipccenter.newsagg.entity.News;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;

/**
 * Created with IntelliJ IDEA. User: darya Date: 10.11.13 Time: 20:04 To change
 * this template use File | Settings | File Templates.
 */

@Stateless
public class NewsBean {

    private static final Logger LOG = LoggerFactory.getLogger(NewsBean.class);

    @PersistenceUnit()
    EntityManagerFactory emf;

    public List getAllNews() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            return em.createQuery("select n from News n").getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void persist(News news) {
        LOG.info("try to persist({})", news);
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.persist(news);
            em.flush();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

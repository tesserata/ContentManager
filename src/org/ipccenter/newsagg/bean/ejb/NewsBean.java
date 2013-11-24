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

    public List<News> getAllNews() {
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

    public List<News> getPostedNews(){
        EntityManager em = null;
        try{
            em = emf.createEntityManager();
            return em.createQuery("select n from News n where n.status = 1").getResultList();
        }
        finally {
            {
                if (em != null)
                    em.close();
            }
        }
    }

    public List<News> getNewNews(){
        EntityManager em = null;
        try{
            em = emf.createEntityManager();
            return em.createQuery("select n from News n where n.status=0").getResultList();
        }
        finally {
            {
                if (em != null)
                    em.close();
            }
        }
    }

    public List<News> getIgnoredNews(){
        EntityManager em = null;
        try{
            em = emf.createEntityManager();
            return em.createQuery("select n from News n where n.status=-1").getResultList();
        }
        finally {
            {
                if (em != null)
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
    
    public News merge(News news) {
        LOG.info("try to persist({})", news);
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            News updated = em.merge(news);
            em.flush();
            return updated;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void clearDataBase(){
        EntityManager em = null;
        try{
            em = emf.createEntityManager();
            for (Object news: em.createQuery("select n from News n").getResultList()){
                em.remove(news);
            }
            em.flush();
        }
        finally{
            if (em != null){
                em.close();
            }
        }
    }
    
    public void deleteNews(News news){
        EntityManager em = null;
        try{
            em = emf.createEntityManager();
            Object deleted = em.createQuery("select n from News n where n.id=" + news.getId().toString()).getSingleResult();
            em.remove(deleted);
            em.flush();
        }
        finally{
            if (em != null){
                em.close();
            }
        }
    }
}

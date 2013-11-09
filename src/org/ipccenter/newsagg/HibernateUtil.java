package org.ipccenter.newsagg;

/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 10.11.13
 * Time: 2:22
 * To change this template use File | Settings | File Templates.
 */

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.ipccenter.newsagg.entity.News;


public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            AnnotationConfiguration annotationConfiguration = new AnnotationConfiguration();
            annotationConfiguration.addAnnotatedClass(News.class);
            sessionFactory = annotationConfiguration.configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}


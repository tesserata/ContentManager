package org.ipccenter.newsagg.bean.ejb;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TimerService;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.ipccenter.newsagg.bean.jsf.VkPullerConfiguratorManagedBean;
import org.ipccenter.newsagg.entity.InterfaceConfig;
import org.ipccenter.newsagg.entity.News;
import org.ipccenter.newsagg.entity.Parameter;
import org.ipccenter.newsagg.interfaces.ExternalInteraction;
import org.ipccenter.newsagg.interfaces.Puller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author spitty
 */
@Singleton
@Named("configurator")
public class ConfiguratorBean {

    private static final Logger LOG = LoggerFactory.getLogger(ConfiguratorBean.class);

    @PersistenceUnit()
    EntityManagerFactory emf;

    @Resource
    TimerService timerService;
    
    @EJB
    NewsBean ejbNews;
    
    @EJB
    VkPullerConfiguratorManagedBean vkConfig;

    private final Set<Puller> pullers = new HashSet<>();
    
    @PostConstruct
    public void restoreConfigurations() {
        List<InterfaceConfig> configurations = getConfigurations();
        LOG.info("Restore objects from configurations {}", configurations);
        for (InterfaceConfig configuration : configurations) {
            try {
                Class clazz = Class.forName(configuration.getClassName());
                if(clazz.isAssignableFrom(ExternalInteraction.class)) {
                    LOG.warn("Configuration {} tries to create instance of {} which is not a subtype of ExternalInteraction",
                            configuration, clazz);
                    continue;
                }
                
                Constructor constructor = clazz.getConstructor();
                ExternalInteraction interfaceObject = (ExternalInteraction)constructor.newInstance();
                interfaceObject.restoreConfiguration(configuration);
                if(interfaceObject instanceof Puller) {
                    registerPuller((Puller)interfaceObject);
                } else {
                    LOG.warn("Created object {} is not regitered! Please specify rules for storing objects of this type", interfaceObject);
                }
                
            } catch (Exception ex) {
                LOG.error("Cant instancied an entity from configuration {}", configuration, ex);
            }
        }
    }

    @Schedule(minute = "*", second = "*/15", hour = "*", persistent = false)
    public void myTimer() {
        LOG.debug("Timer event: {}", new Date());
        LOG.debug("Pullers to be called: {}", pullers);
        for (Puller puller : pullers) {
            try {
                List<News> news = puller.getNews();
                LOG.debug("News obtained from Puller {}: {}", puller, news);
                ejbNews.persist((News) news);
            } catch (Exception ex) {
                LOG.error("Cant get news from puller {}", puller, ex);
            }
        }
    }

    public void registerPuller(Puller puller) {
        if (!pullers.contains(puller)) {
            LOG.warn("Puller {} already registered", puller);
        }
        persist(puller.saveConfiguration());
        pullers.add(puller);
    }

    public Collection<Puller> getPullers() {
        return Collections.unmodifiableCollection(pullers);
    }

    public List<InterfaceConfig> getConfigurations() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            return em.createQuery("select n from InterfaceConfig n").getResultList();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void deleteConfiguration(InterfaceConfig config) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Object toRemove = em.merge(config);
            em.remove(toRemove);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public void selectConfiguration(InterfaceConfig config){
        if (config.getClassName().equals("vk.com")) vkConfig.selectVKPuller(config);
    }

    public void persist(InterfaceConfig config) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            em.persist(config);
            for (Parameter p : config.getParameters()) {
                em.persist(p);
            }
            LOG.debug("Configuration has been saved: {}", config);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.bean.jsf;

import org.ipccenter.newsagg.bean.ejb.NewsBean;
import org.ipccenter.newsagg.entity.News;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author darya
 */
@ManagedBean
@RequestScoped
public class NewsManagedBean {

    private static final Logger LOG = LoggerFactory.getLogger(NewsManagedBean.class);
    @EJB
    NewsBean ejbNews;

    /**
     * Creates a new instance of NewsManagedBean
     */
    public NewsManagedBean() {
    }

    public List<News> getNews() {
//        Date date = new Date();
//        return Arrays.asList(new News("Some content", "http://vk.com/frtk_mipt", "vkontakte", String.valueOf(date.getTime())));
        return ejbNews.getAllNews();
    }

    public void generateNews() {
        LOG.info("generateNews() has been invoked");
        Calendar cal = new GregorianCalendar();
        String date = cal.toString();
        News news = new News("Some content", "some url", "editor", date);
        ejbNews.persist(news);
    }

    public int getNewsCount() {
        return ejbNews.getAllNews().size();
    }
}

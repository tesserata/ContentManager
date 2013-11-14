/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.bean.jsf;


import org.ipccenter.newsagg.bean.ejb.NewsBean;
import org.ipccenter.newsagg.entity.News;
import org.ipccenter.newsagg.impl.vkapi.VKPuller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author darya
 */
@ManagedBean
@RequestScoped
public class NewsManagedBean {

    private String generatedContent;

    private static final Logger LOG = LoggerFactory.getLogger(NewsManagedBean.class);
    @EJB
    NewsBean ejbNews;

    /**
     * Creates a new instance of NewsManagedBean
     */
    public NewsManagedBean() {
    }

    public void setGeneratedContent(String generatedContent) {
        this.generatedContent = generatedContent;
    }

    public String getGeneratedContent() {
        return generatedContent;
    }

    public List<News> getNews() {
        return ejbNews.getAllNews();
    }

    public void generateNews() {
        LOG.info("generateNews() has been invoked");
        Date now = new Date();
        DateFormat ftm = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String date = ftm.format(now);
        News news = new News(generatedContent, "some url", "editor", date);
        ejbNews.persist(news);
    }

    public int getNewsCount() {
        return ejbNews.getAllNews().size();
    }

    public String checkStatus(News news) {
        switch (news.getStatus()) {
            case (0):
                return "New";
            case (1):
                return "Posted";
            case (-1):
                return "Ignored";
        }
        return null;
    }

    public void updateVkFeed() throws IOException {
        VKPuller vk = new VKPuller();
        vk.checkFeed();
        vk.findPosts();
        for (News news : vk.getPostsList()) {
            ejbNews.persist(news);
        }

    }
}

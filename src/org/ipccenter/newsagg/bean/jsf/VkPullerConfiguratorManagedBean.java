/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.bean.jsf;

import java.io.IOException;
import java.util.ArrayList;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.ipccenter.newsagg.bean.ejb.ConfiguratorBean;
import org.ipccenter.newsagg.entity.InterfaceConfig;
import org.ipccenter.newsagg.entity.Parameter;
import org.ipccenter.newsagg.impl.vkapi.VKInteractionPuller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author spitty
 */
@ManagedBean
@RequestScoped
@Named("vkPullerConfig")
public class VkPullerConfiguratorManagedBean {
    
    private static final Logger LOG = LoggerFactory.getLogger(VkPullerConfiguratorManagedBean.class);
    
    @Inject
    ConfiguratorBean timerBean;
    
    private String accessToken;
    private String userID;
    
    /**
     * Creates a new instance of VkPullerConfiguratorManagedBean
     */
    public VkPullerConfiguratorManagedBean() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    
    public void createVKPuller() throws IOException {
        LOG.debug("create VK puller with parameters ({}, {})", accessToken, userID);
        VKInteractionPuller puller = new VKInteractionPuller();
        puller.initialize();
        puller.setAccessToken(accessToken);
        puller.setUserID(userID);
        timerBean.persist(puller.saveConfiguration());
        
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "/faces/config/configurator.xhtml");
    }
    
    public void selectVKPuller(InterfaceConfig config){
        VKInteractionPuller puller = new VKInteractionPuller();
        puller.initialize();
        ArrayList<Parameter> params = (ArrayList<Parameter>) config.getParameters();
        puller.setAccessToken(params.get(0).getValue());
        puller.setUserID(params.get(1).getValue());
        timerBean.persist(puller.saveConfiguration());
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    }
}

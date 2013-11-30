/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ipccenter.newsagg.entity;

/**
 *
 * @author darya
 */

import javax.persistence.*;
import java.text.DateFormat;
import java.util.Date;

@Entity
@Table(name = "PostedNews")

public class PostedNews {

    @Id
    @Column(name = "ID", unique = true)
    @TableGenerator(name = "newsid", table = "newspktb", pkColumnName = "idNews", pkColumnValue = "idNewsValue")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "newsid")
    private Integer id;


    @Column(name = "SOURCE")
    private String source;

    @Column(name = "STATUS")
    private int status;

    @Column(name = "POSTDATE")
    private String date;

    @Column(name = "URL")
    private String url;

    @Column(name = "CONTENT", length = 2000)
    private String content;

    @Column(name = "DESTINATION")
    private String destination;

    @Column(name = "DESTINATIONURL")
    private String destinationURL;

    public PostedNews() {
    }

    public PostedNews(String content, String url, String source, Date date, String destination, String destinationURL) {
        this.content = content;
        this.url = url;
        this.source = source;
        DateFormat ftm = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String formatedDate = ftm.format(date);
        this.date = formatedDate;
        this.destination = destination;
        this.destinationURL = destinationURL;
        this.status = 1;
    }

    public Integer getId() {
        return this.id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {
        DateFormat ftm = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String formatedDate = ftm.format(date);
        this.date = formatedDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationURL() {
        return destinationURL;
    }

    public void setDestinationURL(String destinationURL) {
        this.destinationURL = destinationURL;
    }

    @Override
    public String toString() {
        return "PostedNews{" + "id=" + id + ", source=" + source + ", status=" + status + ", date=" + date + ", url=" + url + ", content=" + content + ", destination=" + destination + ", destinationURL=" + destinationURL + '}';
    }

}

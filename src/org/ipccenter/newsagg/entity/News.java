package org.ipccenter.newsagg.entity; /**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 24.10.13
 * Time: 8:46
 * To change this template use File | Settings | File Templates.
 */


import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

@Entity
@Table(name = "NEWS")
@SequenceGenerator(name="seq", initialValue=1, allocationSize=1000)

public class News implements Serializable {
    @Id
    @Column(name = "ID", unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="seq")
    private Integer id;


    @Column(name = "SOURCE")
    private String source;

    @Column(name = "STATUS")
    private int status;    // -1 = ignored; 0 = new; 1 = posted

    @Column(name = "DATE")
    private String date;

    @Column(name = "URL")
    private String url;

    @Column(name = "CONTENT", length = 2000)
    private String content;

    public News() {
    }

    public News(String content, String url, String source, Date date) {
        this.content = content;
        this.url = url;
        this.source = source;
        DateFormat ftm = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String formatedDate = ftm.format(date);
        this.date = formatedDate;
        this.status = 0;
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

    @Override
    public String toString() {
        return "News{" + "id=" + id + ", source=" + source + ", date=" + date + ", url=" + url + ", content=" + content + ", status=" + status + '}';
    }
}

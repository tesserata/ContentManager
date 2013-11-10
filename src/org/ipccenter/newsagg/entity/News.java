package org.ipccenter.newsagg.entity; /**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 24.10.13
 * Time: 8:46
 * To change this template use File | Settings | File Templates.
 */


import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "NEWS")

public class News implements Serializable {
    @Id
    @Column(name = "ID")
    @TableGenerator(name = "newsid", table = "newspktb", pkColumnName = "idNews", pkColumnValue = "idNewsValue")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "newsid")
    private Integer id;
    @Size(max = 150)


    @Column(name = "SOURCE")
    private String source;

    @Column(name = "STATUS")
    private int status;    // -1 = ignored; 0 = new; 1 = posted

    @Column(name = "DATE")
    private String date;

    @Column(name = "URL")
    private String url;

    @Column(name = "CONTENT")
    private String content;

    public News() {
    }

    public News(String content, String url, String source, String date) {
        this.content = content;
        this.url = url;
        this.source = source;
        this.date = date;
        this.status = 0;
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

    public void setDate(String date) {
        this.date = date;
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
        return "News{" + "id=" + id + ", source=" + source + ", date=" + date + ", url=" + url + ", content=" + content + '}';
    }
}

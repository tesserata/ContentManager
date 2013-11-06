package org.ipccenter.newsagg; /**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 24.10.13
 * Time: 8:46
 * To change this template use File | Settings | File Templates.
 */


import java.util.ArrayList;

public class News {

    public ArrayList<News> origins = new ArrayList<News>();

    public enum Sources {VKONTAKTE, TWITTER, EDITORS}

    public Sources source;   //from Sources
    public int status;    // -1 = ignored; 0 = new; 1 = posted
    public String date;
    public String url;
    public String content;

    public News(){
    }

    public News(String content, String url, Sources source, String date){
        this.content = content;
        this.url = url;
        this.source = source;
        this.date = date;
        this.status = 0;
    }
}

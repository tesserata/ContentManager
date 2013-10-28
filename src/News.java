
/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 24.10.13
 * Time: 8:46
 * To change this template use File | Settings | File Templates.
 */

import java.util.Date;
import java.util.GregorianCalendar;

public class News {

    News(){
        date = new GregorianCalendar().getTime();
    }

    News(String content, String url, String source, Date date){
        this.content = content;
        this.url = url;
        this.source = source;
        this.date = date;
        this.status = 0;
    }
    enum Sources {VKONTAKTE, TWITTER, EDITORS}
    String source;   //from Sources
    int status;    // -1 = ignored; 0 = new; 1 = posted
    Date date;
    String url;
    String content;
}

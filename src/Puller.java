import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 24.10.13
 * Time: 21:41
 * To change this template use File | Settings | File Templates.
 */
public interface Puller {
    enum KEYWORDS {ФРТК, МФТИ, Физтех, РТ, паяльник, радио};

    ArrayList<String> posts = new ArrayList<String>();   //array of URLs

    String findPost();          // finds posts with keywords, returns URL to parsing
    void parsePost(String url); //made new DB element, set up all the fields by parsing html(?)
    void makeList(Date date);   //make ArrayList 'posts' with posts from 'date'

}

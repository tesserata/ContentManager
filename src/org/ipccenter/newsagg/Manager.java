package org.ipccenter.newsagg;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 07.11.13
 * Time: 1:19
 * To change this template use File | Settings | File Templates.
 */
public class Manager {

    private Manager(){}
    private static class ManagerHolder{
        private static final Manager INSTANCE = new Manager();
    }
    private static Manager getInstance(){
        return ManagerHolder.INSTANCE;
    }

    ArrayList<News> getNews(){return null;}

    void update() {}
    void addPuller(Puller p){
        return;
    }


}

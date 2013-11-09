package org.ipccenter.newsagg.dao;

import org.ipccenter.newsagg.entity.News;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: darya
 * Date: 10.11.13
 * Time: 2:32
 * To change this template use File | Settings | File Templates.
 */
public interface NewsDAO {
    NewsDAO INSTANCE_NEWS = new NewsDAOImpl();

    List<News> getProducts();
}

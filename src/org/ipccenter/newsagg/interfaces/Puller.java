package org.ipccenter.newsagg.interfaces;

import java.util.Date;
import java.util.List;
import org.ipccenter.newsagg.entity.News;

/**
 *
 * @author spitty
 */
public interface Puller extends ExternalInteraction {
    List<News> getNews() throws Exception;
    Date getLastUpdatedTime();
}

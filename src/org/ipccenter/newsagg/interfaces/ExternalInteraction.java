package org.ipccenter.newsagg.interfaces;

import org.ipccenter.newsagg.entity.InterfaceConfig;

/**
 *
 * @author spitty
 */
public interface ExternalInteraction {
    void initialize();
    InterfaceConfig saveConfiguration();
    void restoreConfiguration(InterfaceConfig config);
}

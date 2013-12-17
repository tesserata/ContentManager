package org.ipccenter.newsagg.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author spitty
 */
@Entity
@Table(name = "INTERFACE_CONFIG")
public class InterfaceConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "CLASS_NAME")
    private String className;
    
    @OneToMany(mappedBy = "config", cascade = CascadeType.REMOVE)
    private Collection<Parameter> parameters;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="name") 
    @CollectionTable(name="config_attributes")
    @Column(name="value")
    Map<String, String> attributes = new HashMap<>(); // maps from attribute name to value
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    } 

    public String getClassName() {
        if (className.equals("org.ipccenter.newsagg.impl.vkapi.VKInteractionPuller")) return "vk.com";
        if (className.equals("org.ipccenter.newsagg.impl.vkapi.TwitterInteractionPuller")) return "Twitter";
        return "Not defined";
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Collection<Parameter> getParameters() {
        if(parameters == null) {
            parameters = new ArrayList<>();
        }
        return parameters;
    }

    public void setParameters(Collection<Parameter> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InterfaceConfig)) {
            return false;
        }
        InterfaceConfig other = (InterfaceConfig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InterfaceConfig{" + "id=" + id + ", active=" + ", className=" + className + ", parameters=" + parameters + '}';
    }

}

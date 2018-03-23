package com.chapter04.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SimpleObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column
    String keyValue;
    @Column
    Long value;
    public SimpleObject() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return keyValue;
    }

    public void setKey(String keyValue) {
        this.keyValue = keyValue;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleObject)) return false;
        SimpleObject that = (SimpleObject) o;
        // we prefer the method versions of accessors,
        // because of Hibernate's proxies.
        if (getId() != null ?
                !getId().equals(that.getId()) : that.getId() != null)
            return false;
        if (getKey() != null ?
                !getKey().equals(that.getKey()) : that.getKey() != null)
            return false;
        if (getValue() != null ?
                !getValue().equals(that.getValue()) : that.getValue() != null)
            return false;
        return true;
    }
    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getKey() != null ? getKey().hashCode() : 0);
        result = 31 * result + (getValue() != null?getValue().hashCode() : 0);
        return result;
    }
}

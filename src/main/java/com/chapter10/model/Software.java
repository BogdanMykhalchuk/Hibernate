package com.chapter10.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity(name = "Software_2")
public class Software extends Product implements Serializable {
    @Column
    @NotNull
    String version;

    public Software() {

    }

    public Software(String version) {
        this.version = version;
    }

    public Software(Supplier supplier, String name, String description, Double price, String version) {
        super(supplier, name, description, price);
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

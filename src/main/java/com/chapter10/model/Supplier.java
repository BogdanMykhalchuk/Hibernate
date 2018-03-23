package com.chapter10.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Supplier_3")
@Table(name = "supplier_3")
@NamedQueries({
        @NamedQuery(name = "supplier_3.findAll", query = "from Supplier_3 s"),
        @NamedQuery(name = "supplier_3.findByName",
                query = "from Supplier_3 s where s.name=:name"),
})
public class Supplier implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    @NotNull
    private String name;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true,
            mappedBy = "supplier", targetEntity = Product.class)
    private List<Product> products = new ArrayList<>();

    public Supplier() {
    }

    public Supplier(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}

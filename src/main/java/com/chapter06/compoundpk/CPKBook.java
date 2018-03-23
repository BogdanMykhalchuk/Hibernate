package com.chapter06.compoundpk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "CPKBook_1")
@Table(name = "cpk_book_1")
public class CPKBook {

    @Id
    ISBN id;

    @Column
    String name;


    public CPKBook() {
    }

    public ISBN getId() {
        return id;
    }
    public void setId(ISBN id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String title) {
        this.name = title;
    }
}

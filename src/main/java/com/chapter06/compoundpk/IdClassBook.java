package com.chapter06.compoundpk;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "IdClassBook_1")
@Table(name = "id_class_book_1")
@IdClass(IdClassBook.EmbeddedISBN.class)
public class IdClassBook {

    @Id
    int group;

    @Id
    int publisher;

    @Id
    int title;

    @Id
    int checkDigit;

    String name;


    public IdClassBook() {
    }


    public int getGroup() {
        return group;
    }
    public void setGroup(int group) {
        this.group = group;
    }
    public int getPublisher() {
        return publisher;
    }
    public void setPublisher(int publisher) {
        this.publisher = publisher;
    }
    public int getTitle() {
        return title;
    }
    public void setTitle(int title) {
        this.title = title;
    }

    public int getCheckDigit() {
        return checkDigit;
    }

    public void setCheckDigit(int checkDigit) {
        this.checkDigit = checkDigit;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    static class EmbeddedISBN implements Serializable {

        @Column(name="group_number") // because “group” is an invalid column name for SQL
        int group;

        int publisher;

        int title;

        @Column(name="check_digit") // because we want to escape camelCase “checkDigit”
        int checkDigit;


        public EmbeddedISBN() {
        }


        public int getGroup() {
            return group;
        }

        public void setGroup(int group) {
            this.group = group;
        }
        public int getPublisher() {
            return publisher;
        }
        public void setPublisher(int publisher) {
            this.publisher = publisher;
        }
        public int getTitle() {
            return title;
        }
        public void setTitle(int title) {
            this.title = title;
        }

        public int getCheckDigit() {
            return checkDigit;
        }

        public void setCheckDigit(int checkDigit) {
            this.checkDigit = checkDigit;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EmbeddedISBN)) return false;
            EmbeddedISBN isbn = (EmbeddedISBN) o;
            if (checkDigit != isbn.checkDigit) return false;
            if (group != isbn.group) return false;
            if (publisher != isbn.publisher) return false;
            if (title != isbn.title) return false;
            return true;
        }
        @Override
        public int hashCode() {
            int result = group;
            result = 31 * result + publisher;
            result = 31 * result + title;
            result = 31 * result + checkDigit;
            return result;
        }
    }
}

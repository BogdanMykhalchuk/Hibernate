package com.chapter06.compoundpk;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "EmbeddedPKBook_1")
@Table(name = "embeded_pk_book_1")
public class EmbeddedPKBook {

    @EmbeddedId
    EmbeddedISBN id;

    @Column
    String name;

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
            if (!(o instanceof IdClassBook.EmbeddedISBN)) return false;
            IdClassBook.EmbeddedISBN isbn = (IdClassBook.EmbeddedISBN) o;
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

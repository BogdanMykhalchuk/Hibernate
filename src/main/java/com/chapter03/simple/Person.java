package com.chapter03.simple;

import com.autumncode.hibernate.util.SessionUtil;
import lombok.Data;
import org.hibernate.PessimisticLockException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.testng.annotations.Test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;

@Entity
@Data
@Table(name = "table_1")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column
    String name;

    @Test
    public void showDeadlock() throws InterruptedException{
        Long personAId;
        Long personBId;
        // clear out the old data and populate tables
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("delete from Person").executeUpdate();
            Person person = new Person();
            person.setName("A");
            session.save(person);
            personAId=person.getId();
            person = new Person();
            person.setName("B");
            session.save(person);
            personBId=person.getId();
            tx.commit();
        }
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> updatePersons("session1", personAId, personBId));
        executor.submit(() -> updatePersons("session2", personBId, personAId));
        executor.shutdown();
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            executor.shutdownNow();
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.out.println("Executor did not terminate");
            }
        }
        try (Session session = SessionUtil.getSession()) {
            Query<Person> query = session.createQuery("from Person p order by p.name",
                    Person.class);
            String result=query
                    .list()
                    .stream()
                    .map(Person::getName)
                    .collect(Collectors.joining(","));
            assertEquals(result, "A,B");
        }
    }
    private void updatePersons(String prefix, Long... ids){
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            for(Long id:ids) {
                Thread.sleep(100);
                Person person = session
                        .byId(Person.class)
                        .load(id);
                person.setName(prefix+" "+person.getName());
            }
            tx.commit();
        } catch (InterruptedException | PessimisticLockException e) {
            e.printStackTrace();
        }
    }
}

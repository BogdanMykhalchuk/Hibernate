package com.chapter06.naturalid;

import com.autumncode.hibernate.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.NaturalId;
import org.testng.annotations.Test;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

@Entity
public class SimpleNaturalIdEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NaturalId
    Long badge;

    String name;

    public SimpleNaturalIdEmployee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBadge() {
        return badge;
    }

    public void setBadge(Long badge) {
        this.badge = badge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Test
    public void testSimpleNaturalId() {
        Long id = createSimpleEmployee("Sorhed", 5401).getId();
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            SimpleNaturalIdEmployee employee =
                    session
                            .byId(SimpleNaturalIdEmployee.class)
                            .load(id);
            assertNotNull(employee);
            SimpleNaturalIdEmployee badgedEmployee =
                    session
                            .bySimpleNaturalId(SimpleNaturalIdEmployee.class)
                            .load(5401);
            assertEquals(badgedEmployee, employee);
            tx.commit();
        }
    }
    private SimpleNaturalIdEmployee createSimpleEmployee(String name, long badge) {
        SimpleNaturalIdEmployee employee = new SimpleNaturalIdEmployee();
        employee.setName(name);
        employee.setBadge(badge);
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(employee);
            tx.commit();
        }
        return employee;
    }
}

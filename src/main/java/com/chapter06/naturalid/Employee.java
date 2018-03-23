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
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @NaturalId
    Long section;

    @NaturalId
    Long department;

    String name;

    public Employee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSection() {
        return section;
    }

    public void setSection(Long section) {
        this.section = section;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Test
    public void testLoadByNaturalId() {
        Long id = createEmployee("Arrowroot", 11, 291).getId();
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            Employee initial = session
                    .byId(Employee.class)
                    .load(id);
            Employee arrowroot = session
                    .byNaturalId(Employee.class)
                    .using("section", 11)
                    .using("department", 291)
                    .load();
            assertNotNull(arrowroot);
            assertEquals(initial, arrowroot);
            tx.commit();
        }
    }
    private Employee createEmployee(String name, long section, long department) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setDepartment(department);
        employee.setSection(section);
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.save(employee);
            tx.commit();
        }
        return employee;
    }

    @Test
    public void testGetByNaturalId() {
        Employee initial = createEmployee("Eorwax", 11, 292);
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            Employee eorwax = session
                    .byNaturalId(Employee.class)
                    .using("section", 11)
                    .using("department", 292)
                    .getReference();
            System.out.println(initial.equals(eorwax));
            assertEquals(initial, eorwax);
            tx.commit();
        }
    }
}

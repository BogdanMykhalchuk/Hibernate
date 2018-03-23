package com.chapter09.model;

import com.autumncode.hibernate.util.SessionUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;

public class QueryTest {
    Session session;
    Transaction tx;
    @BeforeMethod
    public void populateData() {
        try(Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            Supplier supplier = new Supplier("Hardware, Inc.");
            supplier.getProducts().add(
                    new Product(supplier, "Optical Wheel Mouse", "Mouse", 5.00));
            supplier.getProducts().add(
                    new Product(supplier, "Trackball Mouse", "Mouse", 22.00));
            session.save(supplier);
            supplier = new Supplier("Supplier 2");
            supplier.getProducts().add(
                    new Software(supplier, "SuperDetect", "Antivirus", 14.95, "1.0"));
            supplier.getProducts().add(
                    new Software(supplier, "Wildcat", "Browser", 19.95, "2.2"));
            supplier.getProducts().add(
                    new Product(supplier, "AxeGrinder", "Gaming Mouse", 42.00));
            session.save(supplier);
            tx.commit();
        }
        this.session = SessionUtil.getSession();
        this.tx = this.session.beginTransaction();
    }
    @AfterMethod
    public void closeSession() {
        session.createQuery("delete from Product_1 ").executeUpdate();
        session.createQuery("delete from Supplier_2").executeUpdate();
        if (tx.isActive()) {
            tx.commit();
        }
        if (session.isOpen()) {
            session.close();
        }
    }
    @Test
    public void testNamedQuery() {
        Query<Supplier> query = session.getNamedQuery("supplier_2.findAll");
        List<Supplier> suppliers = query.list();
        assertEquals(suppliers.size(), 2);
    }

    @Test
    public void testSimpleQuery() {
        Query<Product> query = session.createQuery("from Product_1", Product.class);
        query.setComment("This is only a query for product");
        List<Product> products = query.list();
        assertEquals(products.size(), 5);
    }

    @Test
    public void testPagination() {
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("delete from Product_1 ").executeUpdate();
            session.createQuery("delete from Supplier_2").executeUpdate();
            for (int i = 0; i < 30; i++) {
                Supplier supplier = new Supplier();
                supplier.setName(String.format("supplier %02d", i));
                session.save(supplier);
            }

            tx.commit();
        }
        try (Session session = SessionUtil.getSession()) {
            Query<String> query = session.createQuery(
                    "select s.name from Supplier_2 s order by s.name", String.class);
            query.setFirstResult(5);
            query.setMaxResults(5);
            List<String> suppliers = query.list();
            String list = suppliers
                    .stream()
                    .collect(Collectors.joining(","));
            assertEquals(list,
                    "supplier 05,supplier 06,supplier 07,supplier 08,supplier 09");
        }
    }
}

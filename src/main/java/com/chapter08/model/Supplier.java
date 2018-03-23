package com.chapter08.model;

import com.autumncode.hibernate.util.SessionUtil;
import lombok.Data;
import org.hibernate.PessimisticLockException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.testng.annotations.Test;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Entity(name = "Supplier_1")
@Table(name = "supplier_1")
@Data
public class Supplier implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(unique = true)
    String name;
    
    @Test
    private void getDifferentSupliersFromSecondLevelCache() throws InterruptedException{
        Long supplierAId;
        // clear out the old data and populate tables
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("delete from Supplier_1").executeUpdate();
            Supplier supplier = new Supplier();
            supplier.setName("A");
            session.save(supplier);
            supplierAId=supplier.getId();
            tx.commit();
        }
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> updateSuppliers(supplierAId));
        Thread.sleep(500);
        executor.submit(() -> updateSuppliers(supplierAId));
        executor.shutdown();
        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
            executor.shutdownNow();
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                System.out.println("Executor did not terminate");
            }
        }
    }

    private void updateSuppliers(Long id){
        try (Session session = SessionUtil.getSession()) {
            Transaction tx = session.beginTransaction();
            Supplier supplier = session.byId(Supplier.class).load(id);
            System.out.println(supplier.toString());
            tx.commit();
        } catch (PessimisticLockException e) {
            e.printStackTrace();
        }
    }
}

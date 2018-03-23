package com.chapter10.model;

import com.autumncode.hibernate.util.JPASessionUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.function.Consumer;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.FileAssert.fail;

public class QueryTest {
    private void doWithEntityManager(Consumer<EntityManager> command) {
        EntityManager em = JPASessionUtil.getEntityManager("chapter10");
        em.getTransaction().begin();
        command.accept(em);
        if (em.getTransaction().isActive() &&
                !em.getTransaction().getRollbackOnly()) {
            em.getTransaction().commit();
        } else {
            em.getTransaction().rollback();
        }
        em.close();
    }

    @BeforeMethod
    public void populateData() {
        doWithEntityManager((em) -> {
            Supplier supplier = new Supplier("Hardware, Inc.");
            supplier.getProducts().add(
                    new Product(supplier, "Optical Wheel Mouse", "Mouse", 5.00));
            supplier.getProducts().add(
                    new Product(supplier, "Trackball Mouse", "Mouse", 22.00));
            em.persist(supplier);
            supplier = new Supplier("Hardware Are We");
            supplier.getProducts().add(
                    new Software(supplier, "SuperDetect", "Antivirus", 14.95, "1.0"));
            supplier.getProducts().add(
                    new Software(supplier, "Wildcat", "Browser", 19.95, "2.2"));
            supplier.getProducts().add(
                    new Product(supplier, "AxeGrinder", "Gaming Mouse", 42.00));
            supplier.getProducts().add(
                    new Product(supplier, "I5 Tablet", "Computer", 849.99));
            supplier.getProducts().add(
                    new Product(supplier, "I7 Desktop", "Computer", 1599.99));
            em.persist(supplier);
        });
    }

    @AfterMethod
    public void cleanup() {
        doWithEntityManager((em) -> {
            em.createQuery("delete from Software_2 ").executeUpdate();
            em.createQuery("delete from Product_2 ").executeUpdate();
            em.createQuery("delete from Supplier_3 ").executeUpdate();
        });
    }

    @Test
    public void testSimpleCriteriaQuery() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Root<Product> root = criteria.from(Product.class);
            criteria.select(root);
            List<Product> list = em.createQuery(criteria).getResultList();
            for(Product product : list) {
                if(product instanceof Software) {
                    System.out.println("It is software Product");
                    System.out.println(((Software) product).getVersion());
                } else {
                    System.out.println("It is just Product");
                }
            }
            assertEquals(list.size(), 7);
        });
    }

    @Test
    public void testSimpleEQQuery() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Metamodel m = em.getMetamodel();
            EntityType<Product> product = m.entity(Product.class);
            Root<Product> root = criteria.from(product);
            criteria.select(root);
            criteria.where(builder.equal(
                    root.get(Product_.description),
                    builder.parameter(String.class, "description")
                    )
            );
            criteria.select(root);
            assertEquals(em
                    .createQuery(criteria)
                    .setParameter("description", "Mouse")
                    .getResultList()
                    .size(), 2);
        });
    }

    @Test
    public void testSimpleNEQuery() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Metamodel m = em.getMetamodel();
            EntityType<Product> product = m.entity(Product.class);
            Root<Product> root = criteria.from(product);
            criteria.select(root);
            criteria.where(
                    builder.notEqual(
                            root.get(Product_.description),
                            builder.parameter(String.class, "description")
                    )
            );
            criteria.select(root);
            assertEquals(em.createQuery(criteria)
                    .setParameter("description", "Mouse")
                    .getResultList().size(), 5);
        });
    }

    @Test
    public void testSimpleLikeQuery() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Metamodel m = em.getMetamodel();
            EntityType<Product> product = m.entity(Product.class);
            Root<Product> root = criteria.from(product);
            criteria.select(root);
            criteria.where(builder.like(
                    root.get(Product_.description),
                    builder.parameter(String.class, "description")));
            criteria.select(root);
            assertEquals(em.createQuery(criteria)
                    .setParameter("description", "%mOUse")
                    .getResultList().size(), 3);
        });
    }

    @Test
    public void testSimpleLikeIgnoreCaseQuery() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Metamodel m = em.getMetamodel();
            EntityType<Product> product = m.entity(Product.class);
            Root<Product> root = criteria.from(product);
            criteria.select(root);
            criteria.where(builder.like(
                    builder.lower(root.get(Product_.description)),
                    builder.lower(builder.parameter(String.class, "description")))
            );
            criteria.select(root);
            assertEquals(em.createQuery(criteria)
                    .setParameter("description", "%mOUse")
                    .getResultList().size(), 3);
        });
    }

    @Test
    public void testNotNullQuery() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Metamodel m = em.getMetamodel();
            EntityType<Product> product = m.entity(Product.class);
            Root<Product> root = criteria.from(product);
            criteria.select(root);
            criteria.where(builder.isNull(
                    builder.lower(root.get(Product_.description))));
            criteria.select(root);
            assertEquals(em.createQuery(criteria).getResultList().size(), 0);
        });
    }

    @Test
    public void testGTQuery() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Metamodel m = em.getMetamodel();
            EntityType<Product> product = m.entity(Product.class);
            Root<Product> root = criteria.from(product);
            criteria.select(root);
            criteria.where(builder.greaterThan(root.get(Product_.price),
                    builder.parameter(Double.class, "price")));
            criteria.select(root);
            assertEquals(em.createQuery(criteria)
                            .setParameter("price", 25.0)
                            .getResultList().size(), 3);
        });
    }

    @Test
    public void testLTEQuery() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Metamodel m = em.getMetamodel();
            EntityType<Product> product = m.entity(Product.class);
            Root<Product> root = criteria.from(product);
            criteria.select(root);
            criteria.where(builder.lessThanOrEqualTo(root.get(Product_.price),
                    builder.parameter(Double.class, "price")));
            criteria.select(root);
            assertEquals(em.createQuery(criteria)
                    .setParameter("price", 25.0)
                    .getResultList().size(), 4);
        });
    }

    @Test
    public void testANDQuery() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Metamodel m = em.getMetamodel();
            EntityType<Product> product = m.entity(Product.class);
            Root<Product> root = criteria.from(product);
            criteria.select(root);
            criteria.where(
                    builder.and(
                            builder.lessThanOrEqualTo(
                                    root.get(Product_.price),
                                    builder.parameter(Double.class, "price")),
                            builder.like(
                                    builder.lower(
                                            root.get(Product_.description)),
                                    builder.lower(
                                            builder.parameter(String.class, "description"))
                            )
                    )
            );
            criteria.select(root);
            assertEquals(em.createQuery(criteria)
                    .setParameter("price", 10.0)
                    .setParameter("description", "%mOUse")
                    .getResultList().size(), 1);
        });
    }

    @Test
    public void testORQuery() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Metamodel m = em.getMetamodel();
            EntityType<Product> product = m.entity(Product.class);
            Root<Product> root = criteria.from(product);
            criteria.select(root);
            criteria.where(
                    builder.or(
                            builder.lessThanOrEqualTo(
                                    root.get(Product_.price),
                                    builder.parameter(Double.class, "price")),
                            builder.like(
                                    builder.lower(
                                            root.get(Product_.description)),
                                    builder.lower(
                                            builder.parameter(String.class, "description"))
                            )
                    )
            );
            criteria.select(root);
            assertEquals(em.createQuery(criteria)
                            .setParameter("price", 10.0)
                            .setParameter("description", "%mOUse")
                            .getResultList().size(), 3);
        });
    }

    @Test
    public void testDisjunction() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Metamodel m = em.getMetamodel();
            EntityType<Product> product = m.entity(Product.class);
            Root<Product> root = criteria.from(product);
            criteria.select(root);
            criteria.where(
                    builder.or(
                            builder.and(
                                    builder.lessThanOrEqualTo(
                                            root.get(Product_.price),
                                            builder.parameter(Double.class, "price_1")),
                                    builder.like(
                                            builder.lower(
                                                    root.get(Product_.description)),
                                            builder.lower(
                                                    builder.parameter(String.class, "desc_1")))),
                            builder.and(
                                    builder.greaterThan(
                                            root.get(Product_.price),
                                            builder.parameter(Double.class, "price_2")),
                                    builder.like(
                                            builder.lower(
                                                    root.get(Product_.description)
                                            ),
                                            builder.lower(
                                                    builder.parameter(String.class, "desc_2"))))));
            criteria.select(root);
            assertEquals(em.createQuery(criteria)
                    .setParameter("price_1", 25.00)
                    .setParameter("desc_1", "%mOUse")
                    .setParameter("price_2", 999.0)
                    .setParameter("desc_2", "Computer")
                    .getResultList().size(), 3);
        });
    }

    @Test
    public void testPagination() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Root<Product> root = criteria.from(Product.class);
            criteria.select(root);
            TypedQuery<Product> query=em.createQuery(criteria);
            query.setFirstResult(2);
            query.setMaxResults(2);
            assertEquals(query.getResultList().size(), 2);
        });
    }

    @Test
    public void testUniqueResult() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Root<Product> root = criteria.from(Product.class);
            criteria.select(root);
            criteria.where(builder.equal(root.get(Product_.price),
                    builder.parameter(Double.class, "price")));
            assertEquals(em.createQuery(criteria)
                    .setParameter("price", 14.95)
                    .getSingleResult().getName(), "SuperDetect");
        });
    }
    @Test(expectedExceptions = NonUniqueResultException.class)
    public void testUniqueResultWithException() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Root<Product> root = criteria.from(Product.class);
            criteria.select(root);
            criteria.where(builder.greaterThan(root.get(Product_.price),
                    builder.parameter(Double.class, "price")));
            Product p = em.createQuery(criteria)
                    .setParameter("price", 14.95)
                    .getSingleResult();
            fail("Should have thrown an exception");
        });
    }

    @Test
    public void testOrderedQuery() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Root<Product> root = criteria.from(Product.class);
            criteria.select(root);
            criteria.orderBy(builder.asc(root.get(Product_.price)), builder.desc(root.get(Product_.name)));
            List<Product> p = em.createQuery(criteria).getResultList();
            p.forEach(a -> System.out.println(a.getPrice() + "\n" + a.getDescription()));
            assertEquals(p.size(), 7);
            assertTrue(p.get(0).getPrice() < p.get(1).getPrice());
        });
    }

    @Test
    public void testGetProductsForSupplierFromProduct() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Root<Product> root = criteria.from(Product.class);
            criteria.select(root);
            criteria.where(builder.equal(
                    root.join(Product_.supplier).get(Supplier_.name),
                    builder.parameter(String.class, "supplier_name"))
            );
            List<Product> p = em.createQuery(criteria)
                    .setParameter("supplier_name", "Hardware Are We")
                    .getResultList();
            assertEquals(p.size(), 5);
        });
    }

    @Test
    public void testGetProductsForSupplierFromSupplier() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
            Root<Supplier> root = criteria.from(Supplier.class);
            criteria.select(root.join(Supplier_.products));
            criteria.where(builder.equal(
                    root.get(Supplier_.name),
                    builder.parameter(String.class, "supplier_name"))
            );
            TypedQuery<Product> query = em.createQuery(criteria);
            query.setParameter("supplier_name", "Hardware Are We");
            List<Product> p = query.getResultList();
            assertEquals(p.size(), 5);
        });
    }

    @Test
    public void testGetSuppliersWithProductUnder7() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Supplier> criteria = builder.createQuery(Supplier.class);
            Root<Product> root = criteria.from(Product.class);
            criteria.select(root.get(Product_.supplier))
                    .distinct(true);
            criteria.where(builder.lessThanOrEqualTo(root.get(Product_.price),
                    builder.parameter(Double.class, "price")));
            assertEquals(em.createQuery(criteria)
                    .setParameter("price", 7.0)
                    .getResultList().size(), 1);
        });
    }
}

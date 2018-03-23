package com.chapter06;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Entity
@Data
public class Article {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String content;
    public Article(){}
    public Article(String content) {
        this.content = content;
    }

    private static EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("chapter07");

    public static void main(String[] args) throws Exception {
        ExecutorService es = Executors.newFixedThreadPool(3);
        try {
            persistArticle();
            es.execute(() -> {
                updateArticle();
            });
            es.execute(() -> {
                //simulating other user by using different thread
                readArticle();
            });
            es.shutdown();
            es.awaitTermination(3, TimeUnit.MINUTES);
        } finally {
            entityManagerFactory.close();
        }
    }

    private static void updateArticle() {
        log("updating Article entity");
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Article article = em.find(Article.class, 1L, LockModeType.PESSIMISTIC_WRITE);
        try {//sleep for long enough to block read thread lock for long time
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        article.setContent("updated content .. ");
        log("committing in write thread.");
        em.getTransaction().commit();
        em.close();
        log("Article updated", article);
    }

    private static void readArticle() {
        try {//some delay before reading
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log("before acquiring read lock");
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Article article = em.find(Article.class, 1L, LockModeType.PESSIMISTIC_READ);
        log("After acquiring read lock", article);
        em.getTransaction().commit();
        em.close();
        log("Article after read commit", article);
    }

    public static void persistArticle() {
        log("persisting article");
        Article article = new Article("test article");
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.persist(article);
        em.getTransaction().commit();
        em.close();
        log("Article persisted", article);
    }

    private static void log(Object... msgs) {
        System.out.println(LocalTime.now() + " - " + Thread.currentThread().getName() +
                " - " + Arrays.toString(msgs));
    }
}

package com.mytest;

import com.autumncode.hibernate.util.JPASessionUtil;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.testng.Assert.assertTrue;

public class MyTest {
    private void doWithEntityManager(Consumer<EntityManager> command) {
        EntityManager em = JPASessionUtil.getEntityManager("mytest");
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
            Post post = new Post();
            post.setTitle("Post1");
            PostDetails postDetails1 = new PostDetails();
            postDetails1.setCreatedBy("Bogdan");
            postDetails1.setPost(post);
            em.persist(post);
            em.persist(postDetails1);

            post = new Post();
            post.setTitle("Post2");
            postDetails1 = new PostDetails();
            postDetails1.setCreatedBy("Petro");
            postDetails1.setPost(post);
            em.persist(post);
            em.persist(postDetails1);

            post = new Post();
            post.setTitle("Post3");
            em.persist(post);

            postDetails1 = new PostDetails();
            postDetails1.setCreatedBy("Vitia");
            em.persist(postDetails1);
        });
    }

    @AfterMethod
    public void cleanup() {
        doWithEntityManager((em) -> {
            em.createQuery("delete from PostDetails").executeUpdate();
            em.createQuery("delete from Post").executeUpdate();
        });
    }

    @Test
    public void testSimpleCriteriaQuery() {
        doWithEntityManager((em) -> {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<Post> criteria = builder.createQuery(Post.class);
            Root<Post> root = criteria.from(Post.class);
            criteria.select(root);
            List<Post> posts = em.createQuery(criteria).getResultList();
            assertTrue(posts.size() == 3);
//            for(Post post : posts) {
//                PostDetails postDetails = post.getDetails();
//                if(nonNull(postDetails)) {
//                    details.add(postDetails);
//                }
//                System.out.println(post.getDetails());
//            }
//            assertTrue(details.size() == 2);
            Post post = posts.remove(0);

//            CriteriaBuilder builder1 = em.getCriteriaBuilder();
//            CriteriaQuery<PostDetails> criteria1 = builder1.createQuery(PostDetails.class);
//            Root<PostDetails> root1 = criteria1.from(PostDetails.class);
//            criteria1.select(root1);
//            List<PostDetails> postDetails = em.createQuery(criteria1).getResultList();
//            for(PostDetails postDetail : postDetails) {
//                postDetail.setPost(null);
//            }
            em.remove(post);
        });
    }
}

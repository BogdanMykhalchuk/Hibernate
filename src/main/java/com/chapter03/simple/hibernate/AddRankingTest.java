package com.chapter03.simple.hibernate;

import com.chapter03.application.HibernateRankingService;
import com.chapter03.application.RankingService;
import com.chapter03.simple.Person;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

public class AddRankingTest {
    RankingService service=new HibernateRankingService();
    @Test
    public void addRanking() {
        service.addRanking("J. C. Smell", "Drew Lombardo", "Mule", 8);
        assertEquals(service.getRankingFor("J. C. Smell", "Mule"), 8);
    }

    @Test
    public void updateExistingRanking() {
        service.addRanking("Gene Showrama", "Scottball Most", "Ceylon", 6);
        assertEquals(service.getRankingFor("Gene Showrama", "Ceylon"), 6);
        service.updateRanking("Gene Showrama", "Scottball Most", "Ceylon", 7);
        assertEquals(service.getRankingFor("Gene Showrama", "Ceylon"), 7);
    }
    @Test
    public void updateNonexistentRanking() {
        assertEquals(service.getRankingFor("Scottball Most", "Ceylon"), 0);
        service.updateRanking("Scottball Most", "Gene Showrama", "Ceylon", 7);
        assertEquals(service.getRankingFor("Scottball Most", "Ceylon"), 7);
    }

    @Test
    public void removeRanking() {
        service.addRanking("R1", "R2", "RS1", 8);
        assertEquals(service.getRankingFor("R1", "RS1"), 8);
        service.removeRanking("R1", "R2", "RS1");
        assertEquals(service.getRankingFor("R1", "RS1"), 0);
    }
    @Test
    public void removeNonexistentRanking() {
        service.removeRanking("R3", "R4", "RS2");
    }

    @Test
    public void findAllRankingsEmptySet() {
        assertEquals(service.getRankingFor("Nobody", "Java"),0);
        assertEquals(service.getRankingFor("Nobody", "Python"),0);
        Map<String, Integer> rankings=service.findRankingsFor("Nobody");

        // make sure our dataset size is what we expect: empty
        assertEquals(rankings.size(), 0);
    }
    @Test
    public void findAllRankings() {
        assertEquals(service.getRankingFor("Somebody", "Java"),0);
        assertEquals(service.getRankingFor("Somebody", "Python"),0);
        service.addRanking("Somebody", "Nobody", "Java", 9);
        service.addRanking("Somebody", "Nobody", "Java", 7);
        service.addRanking("Somebody", "Nobody", "Python", 7);
        service.addRanking("Somebody", "Nobody", "Python", 5);
        Map<String, Integer> rankings=service.findRankingsFor("Somebody");
        for(Map.Entry entry : rankings.entrySet()) {
            System.out.println(String.format("The rating for %s is %d", entry.getKey(), entry.getValue()));
        }
        assertEquals(rankings.size(), 2);
        assertNotNull(rankings.get("Java"));
        assertEquals(rankings.get("Java"), new Integer(8));
        assertNotNull(rankings.get("Python"));
        assertEquals(rankings.get("Python"), new Integer(6));
    }

    @Test
    public void findBestForNonexistentSkill() {
        Person p = service.findBestPersonFor("no skill");
        assertNull(p);
    }
    @Test
    public void findBestForSkill() {
        service.addRanking("S1", "O1", "Sk1", 6);
        service.addRanking("S1", "O2", "Sk1", 8);
        service.addRanking("S2", "O1", "Sk1", 5);
        service.addRanking("S2", "O2", "Sk1", 7);
        service.addRanking("S3", "O1", "Sk1", 7);
        service.addRanking("S3", "O2", "Sk1", 9);
        // data that should not factor in!
        service.addRanking("S3", "O1", "Sk2", 2);
        Person p = service.findBestPersonFor("Sk1");
        System.out.println(p.toString());
        assertEquals(p.getName(), "S3");
    }
}

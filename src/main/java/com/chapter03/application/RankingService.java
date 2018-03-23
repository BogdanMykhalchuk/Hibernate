package com.chapter03.application;

import com.chapter03.simple.Person;

import java.util.Map;

public interface RankingService {
    int getRankingFor(String subject, String skill);
    void addRanking(String subject, String observer, String skill, int ranking);
    void updateRanking(String subject, String observer, String skill, int rank);
    void removeRanking(String subject, String observer, String skill);
    Map<String, Integer> findRankingsFor(String object);
    Person findBestPersonFor(String skill);
}

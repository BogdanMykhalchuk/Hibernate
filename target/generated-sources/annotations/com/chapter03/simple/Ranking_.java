package com.chapter03.simple;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ranking.class)
public abstract class Ranking_ {

	public static volatile SingularAttribute<Ranking, Person> observer;
	public static volatile SingularAttribute<Ranking, Person> subject;
	public static volatile SingularAttribute<Ranking, Skill> skill;
	public static volatile SingularAttribute<Ranking, Integer> ranking;
	public static volatile SingularAttribute<Ranking, Long> id;

}


package com.chapter06.compoundpk;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ISBN.class)
public abstract class ISBN_ {

	public static volatile SingularAttribute<ISBN, Integer> publisher;
	public static volatile SingularAttribute<ISBN, Integer> checkdigit;
	public static volatile SingularAttribute<ISBN, Integer> title;
	public static volatile SingularAttribute<ISBN, Integer> group;

}


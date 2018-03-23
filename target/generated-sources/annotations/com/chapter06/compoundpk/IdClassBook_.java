package com.chapter06.compoundpk;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(IdClassBook.class)
public abstract class IdClassBook_ {

	public static volatile SingularAttribute<IdClassBook, String> name;
	public static volatile SingularAttribute<IdClassBook, Integer> publisher;
	public static volatile SingularAttribute<IdClassBook, Integer> title;
	public static volatile SingularAttribute<IdClassBook, Integer> checkDigit;
	public static volatile SingularAttribute<IdClassBook, Integer> group;

}


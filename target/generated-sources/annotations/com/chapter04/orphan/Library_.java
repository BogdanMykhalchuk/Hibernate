package com.chapter04.orphan;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Library.class)
public abstract class Library_ {

	public static volatile ListAttribute<Library, Book> books;
	public static volatile SingularAttribute<Library, String> name;
	public static volatile SingularAttribute<Library, Long> id;

}


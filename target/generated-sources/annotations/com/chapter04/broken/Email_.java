package com.chapter04.broken;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Email.class)
public abstract class Email_ {

	public static volatile SingularAttribute<Email, String> subject;
	public static volatile SingularAttribute<Email, Long> id;
	public static volatile SingularAttribute<Email, Message> message;

}


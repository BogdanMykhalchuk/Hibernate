package com.chapter04.broken;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Message.class)
public abstract class Message_ {

	public static volatile SingularAttribute<Message, Long> id;
	public static volatile SingularAttribute<Message, String> content;
	public static volatile SingularAttribute<Message, Email> email;

}


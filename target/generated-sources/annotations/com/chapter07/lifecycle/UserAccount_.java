package com.chapter07.lifecycle;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(UserAccount.class)
public abstract class UserAccount_ {

	public static volatile SingularAttribute<UserAccount, Integer> salt;
	public static volatile SingularAttribute<UserAccount, String> name;
	public static volatile SingularAttribute<UserAccount, Long> id;
	public static volatile SingularAttribute<UserAccount, Integer> passwordHash;

}


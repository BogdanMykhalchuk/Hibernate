package com.chapter07.validated;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ValidatedSimplePerson.class)
public abstract class ValidatedSimplePerson_ {

	public static volatile SingularAttribute<ValidatedSimplePerson, String> fname;
	public static volatile SingularAttribute<ValidatedSimplePerson, String> lname;
	public static volatile SingularAttribute<ValidatedSimplePerson, Long> id;
	public static volatile SingularAttribute<ValidatedSimplePerson, Integer> age;

}


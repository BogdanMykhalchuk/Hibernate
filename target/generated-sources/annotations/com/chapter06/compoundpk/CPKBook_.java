package com.chapter06.compoundpk;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CPKBook.class)
public abstract class CPKBook_ {

	public static volatile SingularAttribute<CPKBook, String> name;
	public static volatile SingularAttribute<CPKBook, ISBN> id;

}


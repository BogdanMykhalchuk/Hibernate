package com.chapter06.compoundpk;

import com.chapter06.compoundpk.EmbeddedPKBook.EmbeddedISBN;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(EmbeddedPKBook.class)
public abstract class EmbeddedPKBook_ {

	public static volatile SingularAttribute<EmbeddedPKBook, String> name;
	public static volatile SingularAttribute<EmbeddedPKBook, EmbeddedISBN> id;

}


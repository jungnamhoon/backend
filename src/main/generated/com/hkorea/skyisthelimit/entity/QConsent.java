package com.hkorea.skyisthelimit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QConsent is a Querydsl query type for Consent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QConsent extends EntityPathBase<Consent> {

    private static final long serialVersionUID = -1111665081L;

    public static final QConsent consent = new QConsent("consent");

    public final StringPath authorities = createString("authorities");

    public final StringPath principalName = createString("principalName");

    public final StringPath registeredClientId = createString("registeredClientId");

    public QConsent(String variable) {
        super(Consent.class, forVariable(variable));
    }

    public QConsent(Path<? extends Consent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QConsent(PathMetadata metadata) {
        super(Consent.class, metadata);
    }

}


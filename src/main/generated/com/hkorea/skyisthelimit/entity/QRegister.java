package com.hkorea.skyisthelimit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRegister is a Querydsl query type for Register
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRegister extends EntityPathBase<Register> {

    private static final long serialVersionUID = -223846890L;

    public static final QRegister register = new QRegister("register");

    public final StringPath authorizationGrantTypes = createString("authorizationGrantTypes");

    public final StringPath clientAuthenticationMethods = createString("clientAuthenticationMethods");

    public final StringPath clientId = createString("clientId");

    public final DateTimePath<java.time.Instant> clientIdIssuedAt = createDateTime("clientIdIssuedAt", java.time.Instant.class);

    public final StringPath clientName = createString("clientName");

    public final StringPath clientSecret = createString("clientSecret");

    public final DateTimePath<java.time.Instant> clientSecretExpiresAt = createDateTime("clientSecretExpiresAt", java.time.Instant.class);

    public final StringPath clientSettings = createString("clientSettings");

    public final StringPath id = createString("id");

    public final StringPath postLogoutRedirectUris = createString("postLogoutRedirectUris");

    public final StringPath redirectUris = createString("redirectUris");

    public final StringPath scopes = createString("scopes");

    public final StringPath tokenSettings = createString("tokenSettings");

    public QRegister(String variable) {
        super(Register.class, forVariable(variable));
    }

    public QRegister(Path<? extends Register> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRegister(PathMetadata metadata) {
        super(Register.class, metadata);
    }

}


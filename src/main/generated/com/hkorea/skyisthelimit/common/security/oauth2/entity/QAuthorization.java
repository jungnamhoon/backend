package com.hkorea.skyisthelimit.common.security.oauth2.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAuthorization is a Querydsl query type for Authorization
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAuthorization extends EntityPathBase<Authorization> {

    private static final long serialVersionUID = 1087558462L;

    public static final QAuthorization authorization = new QAuthorization("authorization");

    public final DateTimePath<java.time.Instant> accessTokenExpiresAt = createDateTime("accessTokenExpiresAt", java.time.Instant.class);

    public final DateTimePath<java.time.Instant> accessTokenIssuedAt = createDateTime("accessTokenIssuedAt", java.time.Instant.class);

    public final StringPath accessTokenMetadata = createString("accessTokenMetadata");

    public final StringPath accessTokenScopes = createString("accessTokenScopes");

    public final StringPath accessTokenType = createString("accessTokenType");

    public final StringPath accessTokenValue = createString("accessTokenValue");

    public final StringPath attributes = createString("attributes");

    public final DateTimePath<java.time.Instant> authorizationCodeExpiresAt = createDateTime("authorizationCodeExpiresAt", java.time.Instant.class);

    public final DateTimePath<java.time.Instant> authorizationCodeIssuedAt = createDateTime("authorizationCodeIssuedAt", java.time.Instant.class);

    public final StringPath authorizationCodeMetadata = createString("authorizationCodeMetadata");

    public final StringPath authorizationCodeValue = createString("authorizationCodeValue");

    public final StringPath authorizationGrantType = createString("authorizationGrantType");

    public final StringPath authorizedScopes = createString("authorizedScopes");

    public final DateTimePath<java.time.Instant> deviceCodeExpiresAt = createDateTime("deviceCodeExpiresAt", java.time.Instant.class);

    public final DateTimePath<java.time.Instant> deviceCodeIssuedAt = createDateTime("deviceCodeIssuedAt", java.time.Instant.class);

    public final StringPath deviceCodeMetadata = createString("deviceCodeMetadata");

    public final StringPath deviceCodeValue = createString("deviceCodeValue");

    public final StringPath id = createString("id");

    public final StringPath oidcIdTokenClaims = createString("oidcIdTokenClaims");

    public final DateTimePath<java.time.Instant> oidcIdTokenExpiresAt = createDateTime("oidcIdTokenExpiresAt", java.time.Instant.class);

    public final DateTimePath<java.time.Instant> oidcIdTokenIssuedAt = createDateTime("oidcIdTokenIssuedAt", java.time.Instant.class);

    public final StringPath oidcIdTokenMetadata = createString("oidcIdTokenMetadata");

    public final StringPath oidcIdTokenValue = createString("oidcIdTokenValue");

    public final StringPath principalName = createString("principalName");

    public final DateTimePath<java.time.Instant> refreshTokenExpiresAt = createDateTime("refreshTokenExpiresAt", java.time.Instant.class);

    public final DateTimePath<java.time.Instant> refreshTokenIssuedAt = createDateTime("refreshTokenIssuedAt", java.time.Instant.class);

    public final StringPath refreshTokenMetadata = createString("refreshTokenMetadata");

    public final StringPath refreshTokenValue = createString("refreshTokenValue");

    public final StringPath registeredClientId = createString("registeredClientId");

    public final StringPath state = createString("state");

    public final DateTimePath<java.time.Instant> userCodeExpiresAt = createDateTime("userCodeExpiresAt", java.time.Instant.class);

    public final DateTimePath<java.time.Instant> userCodeIssuedAt = createDateTime("userCodeIssuedAt", java.time.Instant.class);

    public final StringPath userCodeMetadata = createString("userCodeMetadata");

    public final StringPath userCodeValue = createString("userCodeValue");

    public QAuthorization(String variable) {
        super(Authorization.class, forVariable(variable));
    }

    public QAuthorization(Path<? extends Authorization> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAuthorization(PathMetadata metadata) {
        super(Authorization.class, metadata);
    }

}


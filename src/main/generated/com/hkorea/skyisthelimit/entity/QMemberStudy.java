package com.hkorea.skyisthelimit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberStudy is a Querydsl query type for MemberStudy
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberStudy extends EntityPathBase<MemberStudy> {

    private static final long serialVersionUID = 672914172L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberStudy memberStudy = new QMemberStudy("memberStudy");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QMember member;

    public final QMember sender;

    public final EnumPath<com.hkorea.skyisthelimit.entity.enums.MemberStudyStatus> status = createEnum("status", com.hkorea.skyisthelimit.entity.enums.MemberStudyStatus.class);

    public final QStudy study;

    public QMemberStudy(String variable) {
        this(MemberStudy.class, forVariable(variable), INITS);
    }

    public QMemberStudy(Path<? extends MemberStudy> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberStudy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberStudy(PathMetadata metadata, PathInits inits) {
        this(MemberStudy.class, metadata, inits);
    }

    public QMemberStudy(Class<? extends MemberStudy> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.sender = inits.isInitialized("sender") ? new QMember(forProperty("sender")) : null;
        this.study = inits.isInitialized("study") ? new QStudy(forProperty("study"), inits.get("study")) : null;
    }

}


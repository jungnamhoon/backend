package com.hkorea.skyisthelimit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberProblem is a Querydsl query type for MemberProblem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberProblem extends EntityPathBase<MemberProblem> {

    private static final long serialVersionUID = -299954414L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberProblem memberProblem = new QMemberProblem("memberProblem");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> lastSubmitId = createNumber("lastSubmitId", Long.class);

    public final QMember member;

    public final StringPath note = createString("note");

    public final BooleanPath noteWritten = createBoolean("noteWritten");

    public final QProblem problem;

    public final NumberPath<Integer> solvedCount = createNumber("solvedCount", Integer.class);

    public final DatePath<java.time.LocalDate> solvedDate = createDate("solvedDate", java.time.LocalDate.class);

    public final EnumPath<com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus> status = createEnum("status", com.hkorea.skyisthelimit.entity.enums.MemberProblemStatus.class);

    public QMemberProblem(String variable) {
        this(MemberProblem.class, forVariable(variable), INITS);
    }

    public QMemberProblem(Path<? extends MemberProblem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberProblem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberProblem(PathMetadata metadata, PathInits inits) {
        this(MemberProblem.class, metadata, inits);
    }

    public QMemberProblem(Class<? extends MemberProblem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.problem = inits.isInitialized("problem") ? new QProblem(forProperty("problem")) : null;
    }

}


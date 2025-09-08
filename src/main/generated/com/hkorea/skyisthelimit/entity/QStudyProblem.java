package com.hkorea.skyisthelimit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudyProblem is a Querydsl query type for StudyProblem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudyProblem extends EntityPathBase<StudyProblem> {

    private static final long serialVersionUID = -648075639L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudyProblem studyProblem = new QStudyProblem("studyProblem");

    public final DatePath<java.time.LocalDate> assignedDate = createDate("assignedDate", java.time.LocalDate.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QProblem problem;

    public final DatePath<java.time.LocalDate> solvedDate = createDate("solvedDate", java.time.LocalDate.class);

    public final EnumPath<com.hkorea.skyisthelimit.entity.enums.StudyProblemStatus> status = createEnum("status", com.hkorea.skyisthelimit.entity.enums.StudyProblemStatus.class);

    public final QStudy study;

    public QStudyProblem(String variable) {
        this(StudyProblem.class, forVariable(variable), INITS);
    }

    public QStudyProblem(Path<? extends StudyProblem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudyProblem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudyProblem(PathMetadata metadata, PathInits inits) {
        this(StudyProblem.class, metadata, inits);
    }

    public QStudyProblem(Class<? extends StudyProblem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.problem = inits.isInitialized("problem") ? new QProblem(forProperty("problem")) : null;
        this.study = inits.isInitialized("study") ? new QStudy(forProperty("study"), inits.get("study")) : null;
    }

}


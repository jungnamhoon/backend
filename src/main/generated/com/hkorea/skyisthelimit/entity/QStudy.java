package com.hkorea.skyisthelimit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudy is a Querydsl query type for Study
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudy extends EntityPathBase<Study> {

    private static final long serialVersionUID = 313215798L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudy study = new QStudy("study");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final QMember creator;

    public final NumberPath<Integer> currentMemberCount = createNumber("currentMemberCount", Integer.class);

    public final NumberPath<Integer> dailyProblemCount = createNumber("dailyProblemCount", Integer.class);

    public final StringPath description = createString("description");

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DatePath<java.time.LocalDate> lastSubmittedDate = createDate("lastSubmittedDate", java.time.LocalDate.class);

    public final NumberPath<Integer> maxLevel = createNumber("maxLevel", Integer.class);

    public final NumberPath<Integer> maxMemberCount = createNumber("maxMemberCount", Integer.class);

    public final EnumPath<com.hkorea.skyisthelimit.entity.enums.ProblemRank> maxRank = createEnum("maxRank", com.hkorea.skyisthelimit.entity.enums.ProblemRank.class);

    public final ListPath<MemberStudy, QMemberStudy> memberStudies = this.<MemberStudy, QMemberStudy>createList("memberStudies", MemberStudy.class, QMemberStudy.class, PathInits.DIRECT2);

    public final NumberPath<Integer> minLevel = createNumber("minLevel", Integer.class);

    public final EnumPath<com.hkorea.skyisthelimit.entity.enums.ProblemRank> minRank = createEnum("minRank", com.hkorea.skyisthelimit.entity.enums.ProblemRank.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> problemSetterIdx = createNumber("problemSetterIdx", Integer.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final EnumPath<com.hkorea.skyisthelimit.entity.enums.StudyStatus> status = createEnum("status", com.hkorea.skyisthelimit.entity.enums.StudyStatus.class);

    public final NumberPath<Integer> streak = createNumber("streak", Integer.class);

    public final ListPath<StudyProblem, QStudyProblem> studyProblems = this.<StudyProblem, QStudyProblem>createList("studyProblems", StudyProblem.class, QStudyProblem.class, PathInits.DIRECT2);

    public final StringPath thumbnailUrl = createString("thumbnailUrl");

    public final NumberPath<Integer> totalSolvedProblemsCount = createNumber("totalSolvedProblemsCount", Integer.class);

    public QStudy(String variable) {
        this(Study.class, forVariable(variable), INITS);
    }

    public QStudy(Path<? extends Study> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudy(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudy(PathMetadata metadata, PathInits inits) {
        this(Study.class, metadata, inits);
    }

    public QStudy(Class<? extends Study> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.creator = inits.isInitialized("creator") ? new QMember(forProperty("creator")) : null;
    }

}


package com.hkorea.skyisthelimit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProblem is a Querydsl query type for Problem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProblem extends EntityPathBase<Problem> {

    private static final long serialVersionUID = 1922259148L;

    public static final QProblem problem = new QProblem("problem");

    public final NumberPath<Integer> baekjoonId = createNumber("baekjoonId", Integer.class);

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final SetPath<MemberProblem, QMemberProblem> memberProblems = this.<MemberProblem, QMemberProblem>createSet("memberProblems", MemberProblem.class, QMemberProblem.class, PathInits.DIRECT2);

    public final ListPath<com.hkorea.skyisthelimit.entity.embeddable.ProblemTag, com.hkorea.skyisthelimit.entity.embeddable.QProblemTag> problemTagList = this.<com.hkorea.skyisthelimit.entity.embeddable.ProblemTag, com.hkorea.skyisthelimit.entity.embeddable.QProblemTag>createList("problemTagList", com.hkorea.skyisthelimit.entity.embeddable.ProblemTag.class, com.hkorea.skyisthelimit.entity.embeddable.QProblemTag.class, PathInits.DIRECT2);

    public final EnumPath<com.hkorea.skyisthelimit.entity.enums.ProblemRank> rank = createEnum("rank", com.hkorea.skyisthelimit.entity.enums.ProblemRank.class);

    public final StringPath title = createString("title");

    public final StringPath url = createString("url");

    public QProblem(String variable) {
        super(Problem.class, forVariable(variable));
    }

    public QProblem(Path<? extends Problem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProblem(PathMetadata metadata) {
        super(Problem.class, metadata);
    }

}


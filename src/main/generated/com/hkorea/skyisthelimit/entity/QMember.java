package com.hkorea.skyisthelimit.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 933886669L;

    public static final QMember member = new QMember("member1");

    public final StringPath email = createString("email");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DatePath<java.time.LocalDate> lastSolvedDate = createDate("lastSolvedDate", java.time.LocalDate.class);

    public final SetPath<MemberProblem, QMemberProblem> memberProblems = this.<MemberProblem, QMemberProblem>createSet("memberProblems", MemberProblem.class, QMemberProblem.class, PathInits.DIRECT2);

    public final SetPath<MemberStudy, QMemberStudy> memberStudies = this.<MemberStudy, QMemberStudy>createSet("memberStudies", MemberStudy.class, QMemberStudy.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath oauth2Username = createString("oauth2Username");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    public final NumberPath<Integer> ranking = createNumber("ranking", Integer.class);

    public final StringPath realName = createString("realName");

    public final StringPath role = createString("role");

    public final NumberPath<Integer> score = createNumber("score", Integer.class);

    public final NumberPath<Integer> streak = createNumber("streak", Integer.class);

    public final NumberPath<Integer> totalReviewNotes = createNumber("totalReviewNotes", Integer.class);

    public final NumberPath<Integer> totalSolvedProblems = createNumber("totalSolvedProblems", Integer.class);

    public final StringPath username = createString("username");

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}


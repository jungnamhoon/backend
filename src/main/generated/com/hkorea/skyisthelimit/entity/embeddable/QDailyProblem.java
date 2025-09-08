package com.hkorea.skyisthelimit.entity.embeddable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDailyProblem is a Querydsl query type for DailyProblem
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QDailyProblem extends BeanPath<DailyProblem> {

    private static final long serialVersionUID = 7851388L;

    public static final QDailyProblem dailyProblem = new QDailyProblem("dailyProblem");

    public final DatePath<java.time.LocalDate> assignedDate = createDate("assignedDate", java.time.LocalDate.class);

    public final NumberPath<Integer> problemId = createNumber("problemId", Integer.class);

    public final StringPath problemTitle = createString("problemTitle");

    public QDailyProblem(String variable) {
        super(DailyProblem.class, forVariable(variable));
    }

    public QDailyProblem(Path<? extends DailyProblem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDailyProblem(PathMetadata metadata) {
        super(DailyProblem.class, metadata);
    }

}


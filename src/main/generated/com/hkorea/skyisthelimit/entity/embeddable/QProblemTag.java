package com.hkorea.skyisthelimit.entity.embeddable;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProblemTag is a Querydsl query type for ProblemTag
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QProblemTag extends BeanPath<ProblemTag> {

    private static final long serialVersionUID = 76910897L;

    public static final QProblemTag problemTag = new QProblemTag("problemTag");

    public final StringPath enName = createString("enName");

    public final StringPath koName = createString("koName");

    public QProblemTag(String variable) {
        super(ProblemTag.class, forVariable(variable));
    }

    public QProblemTag(Path<? extends ProblemTag> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProblemTag(PathMetadata metadata) {
        super(ProblemTag.class, metadata);
    }

}


package com.hkorea.skyisthelimit.dto.notification.internal;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMessageContent is a Querydsl query type for MessageContent
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMessageContent extends BeanPath<MessageContent> {

    private static final long serialVersionUID = 843857895L;

    public static final QMessageContent messageContent = new QMessageContent("messageContent");

    public final StringPath fromEmail = createString("fromEmail");

    public final NumberPath<Integer> fromMemberId = createNumber("fromMemberId", Integer.class);

    public final StringPath fromNickname = createString("fromNickname");

    public final StringPath fromRealName = createString("fromRealName");

    public final StringPath fromUsername = createString("fromUsername");

    public final EnumPath<com.hkorea.skyisthelimit.entity.enums.MessageType> messageType = createEnum("messageType", com.hkorea.skyisthelimit.entity.enums.MessageType.class);

    public final NumberPath<Integer> studyId = createNumber("studyId", Integer.class);

    public QMessageContent(String variable) {
        super(MessageContent.class, forVariable(variable));
    }

    public QMessageContent(Path<? extends MessageContent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMessageContent(PathMetadata metadata) {
        super(MessageContent.class, metadata);
    }

}


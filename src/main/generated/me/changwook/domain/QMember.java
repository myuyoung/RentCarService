package me.changwook.domain;

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

    private static final long serialVersionUID = -579512535L;

    public static final QMember member = new QMember("member1");

    public final DateTimePath<java.time.LocalDateTime> accountLockedUntil = createDateTime("accountLockedUntil", java.time.LocalDateTime.class);

    public final StringPath address = createString("address");

    public final StringPath email = createString("email");

    public final NumberPath<Integer> failedLoginAttempts = createNumber("failedLoginAttempts", Integer.class);

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final BooleanPath licence = createBoolean("licence");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final ListPath<Rent, QRent> rent = this.<Rent, QRent>createList("rent", Rent.class, QRent.class, PathInits.DIRECT2);

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


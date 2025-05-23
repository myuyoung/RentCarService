package me.changwook.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRent is a Querydsl query type for Rent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRent extends EntityPathBase<Rent> {

    private static final long serialVersionUID = 1711275912L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRent rent = new QRent("rent");

    public final NumberPath<Integer> duration = createNumber("duration", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> endDate = createDateTime("endDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    public final QRentCars rentCars;

    public final DateTimePath<java.time.LocalDateTime> rentDate = createDateTime("rentDate", java.time.LocalDateTime.class);

    public QRent(String variable) {
        this(Rent.class, forVariable(variable), INITS);
    }

    public QRent(Path<? extends Rent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRent(PathMetadata metadata, PathInits inits) {
        this(Rent.class, metadata, inits);
    }

    public QRent(Class<? extends Rent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member")) : null;
        this.rentCars = inits.isInitialized("rentCars") ? new QRentCars(forProperty("rentCars"), inits.get("rentCars")) : null;
    }

}


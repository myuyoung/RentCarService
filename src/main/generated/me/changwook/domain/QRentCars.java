package me.changwook.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRentCars is a Querydsl query type for RentCars
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRentCars extends EntityPathBase<RentCars> {

    private static final long serialVersionUID = 1602546375L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRentCars rentCars = new QRentCars("rentCars");

    public final BooleanPath available = createBoolean("available");

    public final QCategory category;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final NumberPath<Long> recommend = createNumber("recommend", Long.class);

    public final StringPath rentCarNumber = createString("rentCarNumber");

    public final NumberPath<Integer> rentPrice = createNumber("rentPrice", Integer.class);

    public final NumberPath<Integer> totalDistance = createNumber("totalDistance", Integer.class);

    public QRentCars(String variable) {
        this(RentCars.class, forVariable(variable), INITS);
    }

    public QRentCars(Path<? extends RentCars> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRentCars(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRentCars(PathMetadata metadata, PathInits inits) {
        this(RentCars.class, metadata, inits);
    }

    public QRentCars(Class<? extends RentCars> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new QCategory(forProperty("category")) : null;
    }

}


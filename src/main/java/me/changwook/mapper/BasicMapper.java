package me.changwook.mapper;

public interface BasicMapper <E,D>{
    E toDto(D dto);

    D toEntity(E entity);

}

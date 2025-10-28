package me.changwook.common;

import java.util.List;

public interface BasicService<T> {

    void save(T entity);

    void update(T entity);

    void delete(T entity);

    List<T> findAll(T entity);

}

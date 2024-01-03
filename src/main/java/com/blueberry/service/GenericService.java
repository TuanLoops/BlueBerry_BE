package com.blueberry.service;

import java.util.Optional;

public interface GenericService <T>{
    Iterable<T> findAll();
    Optional<T> findById(Long id);
    T save(T t);
    void delete(Long id);
}

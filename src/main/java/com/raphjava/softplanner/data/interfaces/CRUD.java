package com.raphjava.softplanner.data.interfaces;

import java.util.function.Consumer;

public interface CRUD
{
    <T > CRUD add(T entity);
    <T > CRUD add(T entity, Consumer<EagerLoader<T>> extrasLoaderAction);
    <T > CRUD update(T entity);
    <T > CRUD update(T entity, Consumer<EagerLoader<T>> cascadeAction);
    <T > CRUD updateSelect(T entity, Consumer<EagerLoader<T>> cascadeAction);
    <T > CRUD remove(T entity);
    <T > CRUD deRelate(T entity, Consumer<EagerLoader<T>> cascadeAction);

    CRUD flush();

    CRUD commit();

    CRUD onSuccess(Runnable onSuccessAction);

    CRUD onFailure(Runnable onSuccessAction);
}

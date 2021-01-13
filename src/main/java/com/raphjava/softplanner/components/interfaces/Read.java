package com.raphjava.softplanner.components.interfaces;

import com.raphjava.softplanner.data.models.EntityBase;

import javax.persistence.Tuple;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Read<ExtrasGetter>
{
    <T extends EntityBase> T get(Class<T> entityClass, int ID, boolean withAllNavigationalProperties, Consumer<ExtrasGetter> extrasLoaderAction);

    <T extends EntityBase> List<T> get(Class<T> entityClass, Predicate<T> predicate, boolean withAllNavigationalProperties, Consumer<ExtrasGetter> extrasLoaderAction);

    <T extends EntityBase> List<T> get(Class<T> entityClass, Consumer<CriteriaBuilder<T>> criteriaBuilderConsumer, boolean withAllNavigationalProperties, Consumer<ExtrasGetter> extrasLoaderAction);

    <T extends EntityBase> List<Tuple> getAndSelect(Class<T> entityClass, List<String> propertyNames, Consumer<CriteriaBuilder> criteriaBuilderConsumer);

    <T extends EntityBase> List<T> getAll(Class<T> entityClass, boolean withAllNavigationalProperties, Consumer<ExtrasGetter> extrasLoaderAction);

}

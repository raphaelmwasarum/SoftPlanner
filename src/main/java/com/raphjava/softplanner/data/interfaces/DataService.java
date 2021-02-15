package com.raphjava.softplanner.data.interfaces;



import com.raphjava.softplanner.data.models.EntityBase;
import net.raphjava.qumbuqa.read.interfaces.FluentCriteriaBuilder;

import java.util.Collection;
import java.util.function.Consumer;

public interface DataService
{

    interface Output<T>
    {

        Output<T> onSuccess(Consumer<T> outputAction);
        Output<T> onFailure(Runnable onFailureAction);

    }

    interface ReadAction
    {
        <T> EntityReadAction<T> get(Class<T> entityClass, int ID);
        <T> CollectionReadAction<T> get(Class<T> entityClass, Consumer<FluentCriteriaBuilder<T>> criteriaBuilderAction);
        <T> CollectionReadAction<T> getAll(Class<T> entityClass);
    }

    interface EntityReadAction<T> extends Output<T>
    {

        EntityReadAction<T> withRelationships();
        EntityReadAction<T> eagerLoad(Consumer<EagerLoader<T>> extrasLoaderAction);

    }

    <T extends EntityBase> T newProxy(T entity);

    void write(Consumer<CRUD> creationUpdatingDeletionAction);

    void read(Consumer<ReadAction> readAction);

    interface CollectionReadAction<T> extends Output<Collection<T>>
    {
        CollectionReadAction<T> withRelationships();
        CollectionReadAction<T> eagerLoad(Consumer<EagerLoader<T>> extrasLoaderAction);
        CollectionReadAction<T> get(Class<T> entityClass, Consumer<FluentCriteriaBuilder<T>> criteriaBuilderAction);


    }
}

package com.raphjava.softplanner.interfaces;//package net.raphjava.studeeconsole.interfaces;
//
//
//import net.raphjava.studeeconsole.components.interfaces.CRUD;
//import net.raphjava.studeeconsole.components.interfaces.EagerLoader;
//
//import java.util.Collection;
//import java.util.function.Consumer;
//
//public interface DataService
//{
//
//    interface Output<T>
//    {
//
//        Output<T> onSuccess(Consumer<T> outputAction);
//        Output<T> onFailure(Runnable onFailureAction);
//
//    }
//
//    interface ReadAction
//    {
//        <T> EntityReadAction<T> get(Class<T> entityClass, int ID);
//        <T> CollectionReadAction<T> getAll(Class<T> entityClass);
//    }
//
//    interface EntityReadAction<T> extends Output<T>
//    {
//
//        EntityReadAction<T> withRelationships();
//        EntityReadAction<T> eagerLoad(Consumer<EagerLoader<T>> extrasLoaderAction);
//
//    }
//
//    void write(Consumer<CRUD> creationUpdatingDeletionAction);
//
//    void read(Consumer<ReadAction> readAction);
//
//    interface CollectionReadAction<T> extends Output<Collection<T>>
//    {
//        CollectionReadAction<T> withRelationships();
//        CollectionReadAction<T> eagerLoad(Consumer<EagerLoader<T>> extrasLoaderAction);
//
//    }
//}

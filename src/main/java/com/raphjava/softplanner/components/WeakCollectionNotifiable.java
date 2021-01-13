package com.raphjava.softplanner.components;

import net.raphjava.raphtility.collectionmanipulation.interfaces.NotifyingCollection;
import net.raphjava.raphtility.collectionmanipulation.interfaces.NotifyingCollection.CollectionNotifiable;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

public class WeakCollectionNotifiable<T> implements CollectionNotifiable<T>
{
    private final Consumer<CollectionNotifiable> unRegisteringAction;
    private WeakReference<CollectionNotifiable<T>> weakReference;

    public WeakCollectionNotifiable(CollectionNotifiable<T> collectionNotifiable, Consumer<CollectionNotifiable> unRegisteringAction)
    {
        weakReference = new WeakReference<CollectionNotifiable<T>>(collectionNotifiable);
        this.unRegisteringAction = unRegisteringAction;
    }


    @Override
    public void collectionChanged(NotifyingCollection<T> notifyingCollection, Change<T> change)
    {
        CollectionNotifiable<T> nc = weakReference.get();
        if(nc == null) unRegisteringAction.accept(nc);
        else nc.collectionChanged(notifyingCollection, change);

    }
}

package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import net.raphjava.raphtility.collectionmanipulation.ArrayList;
//import net.raphjava.raphtility.collectionmanipulation.interfaces.Explorable;
//import net.raphjava.raphtility.collectionmanipulation.interfaces.NotifyingCollection.CollectionNotifiable;
//
//import java.util.Collection;
//import java.util.function.Predicate;
//
//
//public class NotifyingCollection<T> extends ArrayList<T> implements net.raphjava.raphtility.collectionmanipulation.interfaces.NotifyingCollection<T>
//{
//    private Explorable<CollectionNotifiable<T>> collectionNotifiables = new ArrayList<>();
//
//
//    @Override
//    public boolean add(T item)
//    {
//        var b = super.add(item);
//        if(b) informNotifiables(item, CollectionNotifiable.ChangeType.Addition);
//        return b;
//    }
//
//    @Override
//    public void add(int index, T item)
//    {
//        super.add(index, item);
//        informNotifiables(item, CollectionNotifiable.ChangeType.Addition);
//    }
//
//    @Override
//    public boolean addAll(int index, Collection<? extends T> c)
//    {
//        var r = super.addAll(index, c);
//        if(r) for(var i : c) informNotifiables(i, CollectionNotifiable.ChangeType.Addition);
//        return r;
//    }
//
//    @Override
//    public boolean addAll(Collection<? extends T> collection)
//    {
//        var b = super.addAll(collection);
//        if(b) for(var i : collection) informNotifiables(i, CollectionNotifiable.ChangeType.Addition);
//        return b;
//    }
//
//    private void informNotifiables(T item, CollectionNotifiable.ChangeType changeType)
//    {
//        var ch = new CollectionNotifiable.Change<>(item, changeType);
//        informNotifiables(ch);
//    }
//
//    @Override
//    public void clear()
//    {
//        super.clear();
//        informNotifiables(null, CollectionNotifiable.ChangeType.Clearance);
//    }
//
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public boolean remove(Object item)
//    {
//        var b = super.remove(item);
//        if(b) informNotifiables((T) item, CollectionNotifiable.ChangeType.Removal);
//        return b;
//    }
//
//    @Override
//    public T remove(int index)
//    {
//        var i = super.remove(index);
//        informNotifiables(i, CollectionNotifiable.ChangeType.Removal);
//        return i;
//    }
//
//    @Override
//    public boolean removeIf(Predicate<? super T> predicate)
//    {
//        var removed = new ArrayList<T>();
//        var r = super.removeIf(i ->
//        {
//            var b = predicate.test(i);
//            if(b) removed.add(i);
//            return b;
//        });
//
//        if(r) for(var i : removed) informNotifiables(i, CollectionNotifiable.ChangeType.Removal);
//        return r;
//    }
//
//    @Override
//    public boolean removeAll(Collection<?> collection)
//    {
//        var s = size();
//        removeIf(collection::contains);
//        return s == size();
//    }
//
//
//    @Override
//    public boolean retainAll(Collection<?> collection)
//    {
//        var s = size();
//        removeIf(i -> !collection.contains(i));
//        return s == size();
//    }
//
//    private void informNotifiables(CollectionNotifiable.Change<T> change)
//    {
//        for(var notifiable : collectionNotifiables) notifiable.collectionChanged(this, change);
//    }
//
//
//    @Override
//    public void addNotifiable(CollectionNotifiable<T> collectionNotifiable)
//    {
//        collectionNotifiables.add(collectionNotifiable);
//    }
//
//    @Override
//    public void addWeakNotifiable(CollectionNotifiable<T> collectionNotifiable)
//    {
//        collectionNotifiables.add(new WeakCollectionNotifiable<>(collectionNotifiable, this::removeNotifiable));
//
//    }
//
//    @Override
//    public boolean removeNotifiable(CollectionNotifiable<T> collectionNotifiable)
//    {
//        return collectionNotifiables.remove(collectionNotifiable);
//    }
//
//    @Override
//    public void removeNotifiables()
//    {
//        collectionNotifiables.clear();
//    }
//}

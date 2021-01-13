package com.raphjava.softplanner.components.binding.interfaces;

import net.raphjava.raphtility.collectionmanipulation.interfaces.NotifyingCollection;

public interface CollectionBinding/* extends net.raphjava.studee.views.components.binding.interfaces.CollectionBinding*/
{
    <T> CollectionBinding source(NotifyingCollection<T> source);

    <T> CollectionBinding target(NotifyingCollection<T> source);

//    <T> CollectionBinding target(ObservableList<T> target);
}
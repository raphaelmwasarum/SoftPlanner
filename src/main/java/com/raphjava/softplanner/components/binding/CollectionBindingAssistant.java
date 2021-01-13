package com.raphjava.softplanner.components.binding;

import com.raphjava.softplanner.components.binding.interfaces.CollectionBinding;
import javafx.collections.ObservableList;
import net.raphjava.raphtility.collectionmanipulation.interfaces.NotifyingCollection;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Objects;

public class CollectionBindingAssistant implements CollectionBinding
{

    private NotifyingCollection.CollectionNotifiable<?> sourceListener;

    private Updater sourceUpdater;

    private Updater targetUpdater;

    public CollectionBindingAssistant()
    {

    }

    private void updateTarget(NotifyingCollection<?> source, NotifyingCollection.CollectionNotifiable.Change<?> change)
    {
        targetUpdater.update(change);
    }

    public <T> void createSourceUpdater(NotifyingCollection<?> source)
    {
        sourceUpdater = getNewUpdater(source);
    }

    public <T> void createTargetUpdater(NotifyingCollection<T> target)
    {
        targetUpdater = getNewUpdater(target);
    }

    private <T> void createTargetUpdater(ObservableList<T> target)
    {
        targetUpdater = getNewUpdater(target);
    }

    private <T> Updater getNewUpdater(ObservableList<T> target)
    {
        return new Updater(target);
    }

    private Updater getNewUpdater(NotifyingCollection<?> target)
    {
        return new Updater(target);
    }

    public void setupBinding()
    {
        Objects.requireNonNull(sourceUpdater.nvWeakReference.get()).addWeakNotifiable(getSourceListener());
    }

    private <T> NotifyingCollection.CollectionNotifiable<T> getSourceListener()
    {
        NotifyingCollection.CollectionNotifiable<T> l = this::updateTarget;
        sourceListener = l;
        return l;
    }


    @Override
    public <T> CollectionBinding source(NotifyingCollection<T> source)
    {
        createSourceUpdater(source);
        return this;
    }

    @Override
    public <T> CollectionBinding target(NotifyingCollection<T> target)
    {
        createTargetUpdater(target);
        return this;
    }

//    @Override
//    public <T> CollectionBinding target(ObservableList<T> target)
//    {
//        createTargetUpdater(target);
//        return this;
//    }


    private class Updater
    {
        private WeakReference<ObservableList<?>> ovWeakReference;

        private WeakReference<NotifyingCollection<?>> nvWeakReference;

        public Updater(NotifyingCollection<?> target)
        {
            nvWeakReference = new WeakReference<NotifyingCollection<?>>(target);
        }

        public <T> Updater(ObservableList<T> target)
        {
            ovWeakReference = new WeakReference<ObservableList<?>>(target);
        }

        @SuppressWarnings("unchecked")
        public void update(NotifyingCollection.CollectionNotifiable.Change<?> change)
        {
            Collection c = getUpdateAdapter();
            if (c == null) return;
            switch (change.getChangeType())
            {
                case Addition:
                    c.add(change.getItem());
                    break;
                case Clearance:
                    c.clear();
                    break;
                case Removal:
                    c.remove(change.getItem());
                    break;
            }
        }

        @SuppressWarnings("unchecked")
        private Collection getUpdateAdapter()
        {
            return nvWeakReference != null? nvWeakReference.get() : ovWeakReference.get();

        }

    }

}
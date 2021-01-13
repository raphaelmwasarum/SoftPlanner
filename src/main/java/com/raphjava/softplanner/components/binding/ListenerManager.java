package com.raphjava.softplanner.components.binding;


import net.raphjava.raphtility.collectionmanipulation.interfaces.NotifyingCollection;
import net.raphjava.raphtility.interfaces.Notifiable;
import net.raphjava.raphtility.interfaces.NotifyingValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Holds references of listeners so they don't get GC'd when you observe through weak references.
 */
@Component
public class ListenerManager
{

    protected Collection<CollectionListener> collectionListeners = new ArrayList<CollectionListener>();

    protected Collection<PropertyListener> propertyListeners = new ArrayList<PropertyListener>();

    private Collection<NotifyingObjectListener> notifyingObjectListeners = new ArrayList<NotifyingObjectListener>();

    /** Returns a new CollectionListener with a reference to it maintained by this listener manager.
     * @param <T>
     * @return a new CollectionListener instance.
     */
    public <T> CollectionListener<T> collectionListener()
    {
        CollectionListener<T> cl = new CollectionListener<T>();
        collectionListeners.add(cl);
        return cl;
    }

    public <T> PropertyListener<T> propertyListener()
    {
        PropertyListener<T> pl = new PropertyListener<T>();
        propertyListeners.add(pl);
        return pl;
    }

    public NotifyingObjectListener notifyingObjectListener()
    {
        NotifyingObjectListener pl = new NotifyingObjectListener();
        notifyingObjectListeners.add(pl);
        return pl;
    }

    public class PropertyListener<T> implements Notifiable<T>
    {

        private Consumer<T> newValueCallback;
        private BiConsumer<T, T> oldValueNewValueCallback;

        @Override
        public void valueChanged(NotifyingValue<T> notifyingValue, T oldValue, T newValue)
        {
            if(newValueCallback != null) newValueCallback.accept(newValue);
            if(oldValueNewValueCallback != null) oldValueNewValueCallback.accept(oldValue, newValue);

        }

        public PropertyListener<T> onChange(Consumer<T> newValueAction)
        {
            newValueCallback = newValueAction;
            return this;
        }

        public PropertyListener<T> onChange(BiConsumer<T, T> newValueAction)
        {
            oldValueNewValueCallback = newValueAction;
            return this;
        }


    }

    public class NotifyingObjectListener implements Runnable
    {

        private Runnable onBroadcast;

        public NotifyingObjectListener onBroadcast(Runnable onBroadcast)
        {
            this.onBroadcast = onBroadcast;
            return this;
        }

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run()
        {
            if(onBroadcast != null) onBroadcast.run();

        }
    }




    public class CollectionListener<T> implements NotifyingCollection.CollectionNotifiable<T>
    {

        private Consumer<T> onAddCallback;
        private Consumer<T> onRemoveCallback;
        private Consumer<T> onClearanceCallback;

        private Consumer<Change<T>> onChangeCallback;

        @Override
        public void collectionChanged(NotifyingCollection<T> notifyingCollection, Change<T> change)
        {
            switch (change.getChangeType())
            {

                case Addition:
                    if (onAddCallback != null) onAddCallback.accept(change.getItem());
                    if (onChangeCallback != null) onChangeCallback.accept(change);
                    break;
                case Clearance:
                    if (onClearanceCallback != null) onClearanceCallback.accept(change.getItem());
                    if (onChangeCallback != null) onChangeCallback.accept(change);
                    break;
                case Removal:
                    if (onChangeCallback != null) onChangeCallback.accept(change);
                    if (onRemoveCallback != null) onRemoveCallback.accept(change.getItem());
                    break;

            }

        }


        public CollectionListener<T> onAdd(Consumer<T> addAction)
        {
            onAddCallback = addAction;
            return this;
        }

        public CollectionListener<T> onRemove(Consumer<T> removeAction)
        {
            onRemoveCallback = removeAction;
            return this;
        }

        public CollectionListener<T> onClearance(Consumer<T> onClearanceAction)
        {
            onClearanceCallback = onClearanceAction;
            return this;
        }

        public CollectionListener<T> onChange(Consumer<Change<T>> onChangeAction)
        {
            onChangeCallback = onChangeAction;
            return this;
        }


    }


}

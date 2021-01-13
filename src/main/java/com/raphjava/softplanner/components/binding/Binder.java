package com.raphjava.softplanner.components.binding;

import com.raphjava.softplanner.components.NotifyingObject;
import com.raphjava.softplanner.components.binding.interfaces.Binding;
import com.raphjava.softplanner.components.binding.interfaces.CollectionBinding;
import com.raphjava.softplanner.components.interfaces.ValueConverter;
import net.raphjava.raphtility.collectionmanipulation.ArrayList;
import net.raphjava.raphtility.collectionmanipulation.interfaces.NotifyingCollection;
import net.raphjava.raphtility.interfaceImplementations.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Consumer;

@Component
public class Binder
{
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Collection<BindingAssistant> bindingAssistants = new ArrayList<BindingAssistant>();


    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Collection<CollectionBindingAssistant> collectionBindingAssistants = new ArrayList<CollectionBindingAssistant>();/*Just for maintaining strong references during
    the lifetime of this JavaFxBinder.*/


    private ListenerManager listenerManager;

    @Autowired
    public Binder( ListenerManager listenerManager)
    {
        this.listenerManager = listenerManager;
    }

    public ListenerManager getListenerManager()
    {
        return listenerManager;
    }

    public <T> ListenerManager.PropertyListener<T> bind(Property<T> property)
    {
        ListenerManager.PropertyListener<T> pl = listenerManager.propertyListener();
        property.addWeakNotifiable(pl);
        return pl;
    }

    public ListenerManager.NotifyingObjectListener bind(NotifyingObject property)
    {
        ListenerManager.NotifyingObjectListener pl = listenerManager.notifyingObjectListener();
        property.addWeakNotifiable(pl);
        return pl;
    }

    public <S, T> void bind(Property<S> source, Property<T> target, ValueConverter<S, T> converter, Binding.Type... bindingType)
    {
        bind(ba ->
        {
            ba.source(source).target(target).converter(converter);
            if (bindingType.length != 0) ba.bindingType(bindingType[0]);
            else ba.bindingType(Binding.Type.OneWay);

        });
    }


    /**
     * Adds a weak listener to this collection that will call all the necessary callbacks when the collection
     * changes.
     *
     * @param source source collection.
     * @param <T>    collection item type.
     * @return a CollectionListener.
     */
    public <T> ListenerManager.CollectionListener<T> bind(NotifyingCollection<T> source)
    {
        ListenerManager.CollectionListener<T> cl = listenerManager.collectionListener();
        source.addWeakNotifiable(cl);
        return cl;
    }

    public <T> void bind(NotifyingCollection<T> source, NotifyingCollection<T> target)
    {
        bindCollections(ba -> ba.source(source).target(target));
    }

//    public <T> void bind(NotifyingCollection<T> source, ObservableList<T> target)
//    {
//        bindCollections(ba -> ba.source(source).target(target));
//
//    }

    public <T> void bind(Property<T> source, Property<T> target, Binding.Type... bindingType)
    {
        bind(ba ->
        {
            ba.source(source).target(target);
            if (bindingType.length != 0) ba.bindingType(bindingType[0]);
            else ba.bindingType(Binding.Type.OneWay);

        });
    }


    @SuppressWarnings("unchecked")
    public <T extends Binding> void bind(Consumer<T> assistantAction)
    {
        BindingAssistant ba = new BindingAssistant();
        assistantAction.accept((T) ba);
        ba.setupBinding();
        bindingAssistants.add(ba);
    }

    private void bindCollections(Consumer<CollectionBinding> assistantAction)
    {
        CollectionBindingAssistant ba = new CollectionBindingAssistant();
        assistantAction.accept(ba);
        ba.setupBinding();
        collectionBindingAssistants.add(ba);
    }

    public void cleanBindings()
    {
        bindingAssistants.clear();
    }



}

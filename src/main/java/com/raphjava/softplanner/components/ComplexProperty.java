package com.raphjava.softplanner.components;

import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.function.Consumer;

public class ComplexProperty<T> implements Property<T>
{


    private final T value;
    private final Consumer<ObservableValue<? extends T>> bindingAction;
    private final Runnable unBindingAction;
    private boolean bound;

    public ComplexProperty(T value, Consumer<ObservableValue<? extends T>> bindingAction, Runnable unBindingAction)
    {
        this.value = value;
        this.bindingAction = bindingAction;
        this.unBindingAction = unBindingAction;
    }
    @Override
    public void bind(ObservableValue<? extends T> observable)
    {
        bindingAction.accept(observable);
        bound = true;
    }

    @Override
    public void unbind()
    {
        unBindingAction.run();
    }

    @Override
    public boolean isBound()
    {
        return bound;
    }

    @Override
    public void bindBidirectional(Property<T> other)
    {
        int a = 5/0;

    }

    @Override
    public void unbindBidirectional(Property<T> other)
    {

    }

    @Override
    public Object getBean()
    {
        return value;
    }

    @Override
    public String getName()
    {
        return value.toString();
    }

    @Override
    public void addListener(ChangeListener<? super T> listener)
    {

    }

    @Override
    public void removeListener(ChangeListener<? super T> listener)
    {

    }

    @Override
    public T getValue()
    {
        return null;
    }

    @Override
    public void setValue(T value)
    {

    }

    @Override
    public void addListener(InvalidationListener listener)
    {

    }

    @Override
    public void removeListener(InvalidationListener listener)
    {

    }
}

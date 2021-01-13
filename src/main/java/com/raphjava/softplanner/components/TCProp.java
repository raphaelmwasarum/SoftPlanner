package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.Prop;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TCProp<C> implements Prop<C>
{

    private Class<C> propertyClass;
    private Supplier<C> getter;
    private Consumer<C> setter;
    private String name;

    private C property;

    public TCProp(String name, Class propClass, Supplier<C> getter, Consumer<C> setter)
    {
        this.name = name;
        this.getter = getter;
        this.setter = setter;
        propertyClass = propClass;

    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + ". Represents the " + name + ". Property type: " + propertyClass.getSimpleName()
        + ". Current value: " + getter.get();
    }
    public TCProp(String name, Class propClass, C property)
    {
        this.name = name;
        setter = e -> this.property = e;
        getter = () -> this.property;
        setter.accept(property);
        propertyClass = propClass;
    }

    @Override
    public String getPropertyName()
    {
        return name;
    }

    @Override
    public C get()
    {
        return getter.get();
    }

    @Override
    public void set(C value)
    {
        setter.accept(value);
    }

    @Override
    public Class<C> getPropertyType()
    {
        return propertyClass;
    }
}

package com.raphjava.softplanner.components.interfaces;

public interface Prop<Clazz>
{
    String getPropertyName();

    Clazz get();

    void set(Clazz value);

    Class<Clazz> getPropertyType();
}

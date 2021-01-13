package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.Factory;

import java.util.Map;

public abstract class AbstractFactory<T> implements Factory<T>
{

    public abstract T build();



    @Override
    public synchronized T createProduct()
    {
        return build();
    }

    private RuntimeException ex = new RuntimeException("Not implemented.");


    @Override
    public T createProduct(Map<Integer, Object> otherConstructorArgs)
    {
        throw ex;
    }

    @Override
    public void deleteProduct(T t)
    {
        throw ex;
    }
}

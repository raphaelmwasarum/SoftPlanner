package com.raphjava.softplanner.components;

public class LambdaSettable<Type>
{
    private Type item;

    public Type getItem()
    {
        return item;
    }

    public void setItem(Type item)
    {
        this.item = item;
    }

    public LambdaSettable(Type item)
    {
        this.item = item;
    }
}

package com.raphjava.softplanner.components;

import java.lang.ref.WeakReference;

public class TCWeakReference<T>
{
    private WeakReference reference;


    public boolean isAlive()
    {
        if(reference == null) return false;
        if(reference.get() == null) return false;
        return true;
    }

    public T getReferent()
    {
        if(reference != null) return (T) reference.get();
        return null;
    }

    public TCWeakReference(T object)
    {
        reference = new WeakReference(object);
    }

    public void markForDeletion()
    {
        reference.clear();
        reference = null;
    }




}

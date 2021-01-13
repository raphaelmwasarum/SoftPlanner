package com.raphjava.softplanner.components;

import net.raphjava.raphtility.collectionmanipulation.ArrayList;
import net.raphjava.raphtility.collectionmanipulation.interfaces.Explorable;
import net.raphjava.raphtility.interfaces.Notifiable;
import net.raphjava.raphtility.interfaces.NotifyingValue;
//import com.raphjava.studeeconsole.components.interfaces.Notifiable;
//import com.raphjava.studeeconsole.components.interfaces.NotifyingValue;

public abstract class AbstractNotifyingValue<T> implements NotifyingValue<T>
{
    protected Explorable<Notifiable<T>> notifiables = new ArrayList<Notifiable<T>>();

    protected T value;

    public void set(T value)
    {
        this.value = value;
    }

    public T get()
    {
        return value;
    }


    @Override
    public synchronized void addWeakNotifiable(Notifiable<T> notifiable)
    {
        addNotifiable(new WeakNotifiable<T>(notifiable, this::removeNotifiable));
    }


    @Override
    public synchronized void addNotifiable(Notifiable<T> notifiable)
    {
        notifiables.add(notifiable);
        notifiable.valueChanged(this, value, value);
    }

    protected void informNotifiables(T oldValue)
    {
        for(Notifiable<T> notifiable : getCopyOfNotifiables()) notifiable.valueChanged(this, oldValue, value);
    }

    private synchronized Explorable<Notifiable<T>> getCopyOfNotifiables()
    {
        return notifiables.list();
    }


    @Override
    public synchronized void removeNotifiable(Notifiable<T> notifiable)
    {
        notifiables.remove(notifiable);
    }

    @Override
    public synchronized void removeNotifiables()
    {
        notifiables.clear();
    }
}

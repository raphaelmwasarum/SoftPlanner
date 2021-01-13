package com.raphjava.softplanner.components.binding;

import com.raphjava.softplanner.components.binding.interfaces.Binding;
import com.raphjava.softplanner.components.interfaces.ValueConverter;
import net.raphjava.raphtility.interfaces.Notifiable;
import net.raphjava.raphtility.interfaces.NotifyingValue;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.function.Function;

public class BindingAssistant implements Binding
{

    private Notifiable<?> sourceListener;
    private Notifiable<?> targetListener;

    private Updater sourceUpdater;
    private Updater targetUpdater;
    private ValueConverter converter;

    private Type bindingType;

    public BindingAssistant()
    {

    }

    public <T> void createSourceUpdater(NotifyingValue<T> source)
    {
        sourceUpdater = getNewUpdater(source);
    }


    @SuppressWarnings("unchecked")
    public <T> void createTargetUpdater(NotifyingValue<T> target)
    {
        targetUpdater = getNewUpdater(target);
    }

    private Updater getNewUpdater(NotifyingValue<?> target)
    {
        return new Updater(target);
    }

    @SuppressWarnings("unchecked")
    public void setupBinding()
    {
        if (converter != null)
        {
            targetUpdater.converter = sourceValue -> converter.convert(sourceValue);
            sourceUpdater.converter = targetValue -> converter.convertBack(targetValue);
            targetUpdater.convertBack = sourceUpdater.converter;
            sourceUpdater.convertBack = targetUpdater.converter;
        }

        Objects.requireNonNull(sourceUpdater.nvWeakReference.get()).addWeakNotifiable(getSourceListener());
        if (bindingType != Type.TwoWay) sourceUpdater = null;
        else Objects.requireNonNull(targetUpdater.nvWeakReference.get()).addWeakNotifiable(getTargetListener());
    }


    public <T> Notifiable<T> getSourceListener()
    {
        if (sourceListener != null) throw new RuntimeException("This assistant already has a source listener.");
        Notifiable<T> notifiable = this::onSourceChanged;
        sourceListener = notifiable;
        return notifiable;
    }

    public <T> Notifiable<T> getTargetListener()
    {
        if (targetListener != null) throw new RuntimeException("This assistant already has a target listener.");
        Notifiable<T> notifiable = this::onTargetChanged;
        targetListener = notifiable;
        return notifiable;
    }

    private void onSourceChanged(NotifyingValue<?> nv, Object old, Object new_)
    {
        if (notSynced(new_, targetUpdater.getCurrentValue())) targetUpdater.update(new_);
    }

    private void onTargetChanged(NotifyingValue<?> nv, Object old, Object new_)
    {
        if (notSynced(new_, sourceUpdater.getCurrentValue())) sourceUpdater.update(new_);
    }

    private boolean notSynced(Object old, Object new_)
    {
        //If old is null, update if new_ is not null, otherwise update if old is not equal to new_.
        return old == null ? new_ != null : !old.equals(new_);
    }

    public void setBindingType(Type bindingType)
    {
        this.bindingType = bindingType;
    }

    @Override
    public <T> Binding source(NotifyingValue<T> source)
    {
        createSourceUpdater(source);
        return this;
    }

    @Override
    public <T> Binding target(NotifyingValue<T> target)
    {
        createTargetUpdater(target);
        return this;
    }

    @Override
    public Binding bindingType(Type bindingType)
    {
        setBindingType(bindingType);
        return this;
    }

    @Override
    public <S, T> void converter(ValueConverter<S, T> converter)
    {
        this.converter = converter;
    }

    private class Updater
    {
        private final WeakReference<NotifyingValue<?>> nvWeakReference;
        private Function converter;
        private Function convertBack;


        public Updater(NotifyingValue<?> target)
        {
            nvWeakReference = new WeakReference<NotifyingValue<?>>(target);
        }

        @SuppressWarnings("unchecked")
        public void update(Object new_)
        {
            NotifyingValue n = nvWeakReference.get();
            if (n != null)
            {
                if (converter != null) n.set(converter.apply(new_));
                else n.set(new_);
            }
        }

        public Object getCurrentValue()
        {
            NotifyingValue n = nvWeakReference.get();
            if (n != null)
            {
                if (convertBack != null) return convertBack.apply(n.get());
                return n.get();
            }
            return null;
        }
    }

}

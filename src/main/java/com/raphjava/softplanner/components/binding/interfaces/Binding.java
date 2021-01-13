package com.raphjava.softplanner.components.binding.interfaces;

import com.raphjava.softplanner.components.interfaces.ValueConverter;
import net.raphjava.raphtility.interfaces.NotifyingValue;

public interface Binding
{

    /**
     * Adds a weak notifiable to the source. This JavaFxBinder maintains a reference of that notifiable so
     * if the binder is GC'd that notifiable will also be eligible for GC.
     *
     * @param source the property that needs to be observed.
     * @param <T>    the type of the property's value.
     * @return A Binding interface instance.
     */
    <T> Binding source(NotifyingValue<T> source);



    /**
     * Setups the target to be updated every time the source changes. Please note that this JavaFxBinder maintains
     * a weak reference of the target.
     *
     * @param target
     * @param <T>    the value of the target property.
     * @return a Binding interface instance.
     */
    <T> Binding target(NotifyingValue<T> target);


    Binding bindingType(Type bindingType);


    <S, T> void converter(ValueConverter<S, T> converter);

    public enum Type
    {
        OneWay,

        /**
         * The source is also updated when the target changes.
         */
        TwoWay
    }
}
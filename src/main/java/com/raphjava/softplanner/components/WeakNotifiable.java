package com.raphjava.softplanner.components;

//import net.raphjava.studeeconsole.components.interfaces.Notifiable;
//import net.raphjava.studeeconsole.components.interfaces.NotifyingValue;

import net.raphjava.raphtility.interfaces.Notifiable;
import net.raphjava.raphtility.interfaces.NotifyingValue;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

/** A weak reference notifiable. It removes itself from notifiables the moment the actual notifiable is garbage collected.
 * @param <T>
 */
public class WeakNotifiable<T> implements Notifiable<T>
{
    private final Consumer<Notifiable<T>> removeMeFromNotifiablesAction;
    private WeakReference<Notifiable<T>> weakNotifiableReference;

    public Notifiable<T> getNotifiable()
    {
        return weakNotifiableReference.get();
    }

    public WeakNotifiable(Notifiable<T> weakNotifiableReference, Consumer<Notifiable<T>> unRegistrationAction)
    {
        this.weakNotifiableReference = new WeakReference<Notifiable<T>>(weakNotifiableReference);
        this.removeMeFromNotifiablesAction = unRegistrationAction;
    }


    @Override
    public void valueChanged(NotifyingValue<T> notifyingValue, T oldValue, T newValue)
    {
        Notifiable<T> notifiable = weakNotifiableReference.get();
        if(notifiable == null) removeMeFromNotifiablesAction.accept(this);
        else notifiable.valueChanged(notifyingValue, oldValue, newValue);
    }
}

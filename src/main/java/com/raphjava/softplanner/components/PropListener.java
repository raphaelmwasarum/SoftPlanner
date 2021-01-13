package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.Prop;
//import com.raphjava.softplanner.viewmodels.TheViewModelBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class PropListener<T> implements ChangeListener<T>
{
    private final TCWeakReference<ComponentBase> viewModelBase;

    private Prop<T> property;

    public PropListener(TCWeakReference<ComponentBase> ref, Prop<T> viewModelProperty)
    {
        this.viewModelBase = ref;
        property = viewModelProperty;
    }

    /*@Override
    public void invalidated(Observable observable)
    {
        try
        {
            throw new NotImplementedException();
        }
        catch (NotImplementedException ex)
        {
            ex.printStackTrace();
        }
    }*/

    @Override
    public void changed(ObservableValue<? extends T> observable, T oldValue, T newValue)
    {
        if(oldValue != newValue)
        {
            property.set(newValue);
            if(!viewModelBase.isAlive()) return;
            viewModelBase.getReferent().handlePropertyChanges(property.getPropertyName());
        }
    }
}

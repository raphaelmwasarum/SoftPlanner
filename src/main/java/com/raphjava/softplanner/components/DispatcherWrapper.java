package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.DispatcherHelper;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

@Lazy
@Scope(Singleton)
@Service
public class DispatcherWrapper implements DispatcherHelper
{

    private DispatcherHelper dispatcherHelper;

    public void setDispatcherHelper(DispatcherHelper dispatcherHelper)
    {
        this.dispatcherHelper = dispatcherHelper;
    }

    @Override
    public void checkBeginInvokeOnUI(Runnable action)
    {
        dispatcherHelper.checkBeginInvokeOnUI(action);
    }

    @Override
    public void invokeOnUI(Runnable action)
    {
        dispatcherHelper.invokeOnUI(action);

    }
}

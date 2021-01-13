package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.DispatcherHelper;


public class ConsoleDispatcher implements DispatcherHelper
{

    public ConsoleDispatcher()
    {
    }

    @Override
    public void checkBeginInvokeOnUI(Runnable action)
    {
        action.run();
    }

    @Override
    public void invokeOnUI(Runnable action)
    {
        action.run();
    }
}

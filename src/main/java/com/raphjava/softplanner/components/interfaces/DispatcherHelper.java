package com.raphjava.softplanner.components.interfaces;

public interface DispatcherHelper
{
    public static final String instance = "dispatcherHelper";
    String console = "internalDispatcherHelper";

    void checkBeginInvokeOnUI(Runnable action);

    void invokeOnUI(Runnable action);

}

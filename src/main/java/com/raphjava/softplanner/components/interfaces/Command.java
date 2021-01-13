package com.raphjava.softplanner.components.interfaces;

public interface Command
{
    void execute(Object parameter);
    boolean canExecute(Object parameter);
}

package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.Command;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class TCCommand implements Command
{

    private Consumer actionWithParameter;
    private Runnable action;

    private Supplier<Boolean> canExecute;

    public TCCommand(Runnable action)
    {
        this.action = action;
    }

    public TCCommand(Consumer action)
    {
        actionWithParameter = action;
    }

    public TCCommand(Runnable action, Supplier<Boolean> canExecute)
    {
        this.action = action;
        this.canExecute = canExecute;
    }

    @Override
    public void execute(Object parameter)
    {
        if(action != null)
        {
            action.run();
            return;
        }
        actionWithParameter.accept(parameter);
    }

    @Override
    public boolean canExecute(Object parameter)
    {
        return canExecute.get();
    }

}

package com.raphjava.softplanner.components;

import net.raphjava.raphtility.collectionmanipulation.ArrayList;
import net.raphjava.raphtility.collectionmanipulation.interfaces.Explorable;
import net.raphjava.raphtility.interfaceImplementations.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class Action extends ComponentBase
{
    private Boolean actionable = true;

    private Property<Boolean> actionableProperty;

    public Property<Boolean> actionableProperty()
    {
        if(actionableProperty == null) actionableProperty = setupProperty((nv, o, n) -> actionable = n, actionable);
        return actionableProperty;
    }

    public Boolean isActionable()
    {
        return actionable;
    }

    public void setActionable(Boolean actionable)
    {
        if(actionableProperty != null) actionableProperty.set(actionable); /*No need to update field coz property updates field
        when it changes.*/
        else this.actionable = actionable;
    }

    private String commandDescription;

    private Property<String> commandDescriptionProperty;

    public Property<String> commandDescriptionProperty()
    {
        if(commandDescriptionProperty == null) commandDescriptionProperty = setupProperty((nv, o, n) -> commandDescription = n, commandDescription);
        return commandDescriptionProperty;
    }


    public void setCommandDescription(String commandDescription)
    {
        if(commandDescriptionProperty != null) commandDescriptionProperty.set(commandDescription);
        else this.commandDescription = commandDescription;
    }

    public String getCommandDescription()
    {
        return commandDescription;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    private Runnable action;

    public Runnable getAction()
    {
        return action;
    }

    public void setAction(Runnable action)
    {
        this.action = action;
    }


    private Action(Builder builder)
    {
        super(builder.baseBuilder);
    }

    private Explorable<Action> contextActions = new ArrayList<Action>();

    public Explorable<Action> getContextActions()
    {
        return contextActions;
    }

    public static final String FACTORY = "actionFactory";

    @Lazy
    @Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<Action>
    {
        private ComponentBase.Builder baseBuilder;

        private Builder()
        {
            super(Action.class);
        }

        @Autowired
        public Builder setBaseBuilder(ComponentBase.Builder val)
        {
            baseBuilder = val;
            return this;
        }

        public Action build()
        {
            return new Action(this);
        }


    }


}

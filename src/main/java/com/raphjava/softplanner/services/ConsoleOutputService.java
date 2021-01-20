package com.raphjava.softplanner.services;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.components.MainComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ConsoleOutputService extends ComponentBase
{

    private ConsoleOutputService(Builder builder)
    {
        super(builder.baseBuilder);
    }

    public void show(String ownerTag, String message)
    {
        System.out.println(String.format("%s: %s", ownerTag, message));
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public final static String FACTORY = "consoleOutputService";


    @Lazy
    @Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ConsoleOutputService>
    {
        private ComponentBase.Builder baseBuilder;

        private Builder()
        {
            super(ConsoleOutputService.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;//TODO Continue from here. Chicken - egg situation causing null pointer.
            return this;
        }

        public ConsoleOutputService build()
        {
            return new ConsoleOutputService(this);
        }
    }
}

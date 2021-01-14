package com.raphjava.softplanner.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Scanner;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ConsoleInput extends ComponentBase
{

    private ConsoleInput(Builder builder)
    {
        super(builder.baseBuilder);
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public int getInt()
    {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public String getInput()
    {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }


    @Lazy
    @Component
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ConsoleInput>
    {
        private ComponentBase.Builder baseBuilder;

        private Builder()
        {
            super(ConsoleInput.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        public synchronized ConsoleInput build()
        {
            return new ConsoleInput(this);
        }
    }
}

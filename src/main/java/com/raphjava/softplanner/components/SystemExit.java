package com.raphjava.softplanner.components;

import com.raphjava.softplanner.data.models.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class SystemExit extends ComponentBase
{
    private SystemExit(Builder builder)
    {
        super(builder.baseBuilder);
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }


    public void shutDown(String shuttingDownMessagePrefix)
    {
        show(String.format("%s. Beginning pre-closing operations...please wait...", shuttingDownMessagePrefix));
        sendMessage(Notification.CleanUp);
        show("Pre-closing operations complete.");
        show("Stopping program.");
        System.exit(0);
    }

    @Lazy
    @Component
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<SystemExit>
    {
        private ComponentBase.Builder baseBuilder;

        private Builder()
        {
            super(SystemExit.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        public SystemExit build()
        {
            return new SystemExit(this);
        }
    }
}

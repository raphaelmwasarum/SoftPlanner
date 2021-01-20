package com.raphjava.softplanner.main;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.MainComponent;
import net.raphjava.raphtility.logging.interfaces.Log;
import net.raphjava.raphtility.logging.interfaces.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class SoftPlannerConsole
{
    public static final String NAME = "Soft planner";

    private LoggerFactory loggerFactory;

    private MainComponent mainComponent;

    private Log logger;

    private SoftPlannerConsole(Builder builder)
    {
        loggerFactory = builder.loggerFactory;
        mainComponent = builder.mainComponent;
    }


    public static Builder newBuilder()
    {
        return new Builder();
    }


    public void start()
    {
        logger = loggerFactory.createLogger(getClass().getSimpleName());
        debug("App is starting...");
        debug(String.format("Hello. I'm an instance of the %s console.", NAME));
        mainComponent.start();


    }

    private void debug(String message)
    {
        logger.debug("[" + logger.getName() + "]" + " - " + message);
    }

    @Lazy
    @Component
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<SoftPlannerConsole>
    {
        private LoggerFactory loggerFactory;
        private MainComponent mainComponent;

        private Builder()
        {
            super(SoftPlannerConsole.class);
        }

        @Autowired
        public Builder setLoggerFactory(LoggerFactory val)
        {
            loggerFactory = val;
            return this;
        }

        public SoftPlannerConsole build()
        {
            return new SoftPlannerConsole(this);
        }


        @Autowired
        public Builder inputProcessor(MainComponent mainComponent)
        {
            this.mainComponent = mainComponent;
            return this;
        }
    }

}

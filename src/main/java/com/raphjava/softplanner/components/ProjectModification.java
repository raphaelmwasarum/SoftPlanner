package com.raphjava.softplanner.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ProjectModification extends ComponentBase
{

    private ConsoleInput inputProcessor;

    private ProjectModification(Builder builder)
    {
        super(builder.baseBuilder);
        inputProcessor = builder.inputProcessor;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    private void initialization()
    {

    }

    public final static String FACTORY = "projectModificationFactory";


    @Lazy
    @org.springframework.stereotype.Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ProjectModification>
    {

        private ComponentBase.Builder baseBuilder;

        private ConsoleInput inputProcessor;

        private Builder()
        {
            super(ProjectModification.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        @Autowired
        public Builder inputProcessor(ConsoleInput inputProcessor)
        {
            this.inputProcessor = inputProcessor;
            return this;
        }
        public synchronized ProjectModification build()
        {
            ProjectModification pm = new ProjectModification(this);
            pm.initialization();
            return pm;
        }

    }
}

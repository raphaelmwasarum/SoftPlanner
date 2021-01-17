package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.data.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class Projects extends ComponentBase
{
    private Projects(Builder builder)
    {
        super(builder.baseBuilder);
    }

    public Optional<Collection<Project>> get()
    {
        @SuppressWarnings("unchecked") Optional<Collection<Project>>[] rez = new Optional[1];
        dataService.read(r -> r.getAll(Project.class).onSuccess(ps -> rez[0] = Optional.of(ps))
                .onFailure(() ->
                {
                    System.out.println("Failed to fetch projects from the repository");
                    rez[0] = Optional.empty();
                }));
        return rez[0];
    }

    public Optional<Collection<Project>> get(String... eagerLoadPaths)
    {
        @SuppressWarnings("unchecked") Optional<Collection<Project>>[] rez = new Optional[1];
        dataService.read(r -> r.getAll(Project.class)
                .eagerLoad(e ->
                {
                    for (String p : eagerLoadPaths) e.include(p);
                })
                .onSuccess(ps -> rez[0] = Optional.of(ps))
                .onFailure(() ->
                {
                    System.out.println("Failed to fetch projects from the repository");
                    rez[0] = Optional.empty();
                }));
        return rez[0];
    }


    public static Builder newBuilder()
    {
        return new Builder();
    }

    public static final String FACTORY = "projectsFactory";

    @Lazy
    @Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<Projects>
    {
        private ComponentBase.Builder baseBuilder;

        private Builder()
        {
            super(Projects.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        public synchronized Projects build()
        {
            return new Projects(this);
        }
    }
}

package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.annotations.Named;
import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.components.ConsoleInput;
import com.raphjava.softplanner.components.interfaces.Factory;
import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.Collection;
import java.util.Optional;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ComponentSelection extends ComponentBase
{

    private ComponentSelection(Builder builder)
    {
        super(builder.baseBuilder);
        inputService = builder.inputProcessor;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    private ConsoleInput inputService;

    private String selectionPurpose;

    public void setSelectionPurpose(String selectionPurpose)
    {
        this.selectionPurpose = selectionPurpose;
    }

    private Component component;

    public void setComponent(Component component)
    {
        this.component = component;
    }

    public Optional<Component> startAsConsole()
    {
        StringBuilder componentDescriptions = new StringBuilder();
        Collection<Component> ps = asExp(component.getSubComponents()).select(sc -> sc.getSubComponentDetail().getComponent()).list();
        ps.forEach(c -> componentDescriptions.append(String.format("%s. Component ID: %s", c.getName(), c.getId())).append("\n"));
        System.out.println(String.format("The following are the sub-components of component [%s]: %s. " +
                        "\n\nEnter the id of the sub-component you want to %s: ",
                String.format("%s. Component ID: %s", component.getName(), component.getId()), componentDescriptions
                , selectionPurpose));
        inputService.getInput().ifPresent(i ->
        {
            Component sc = asExp(ps).firstOrDefault(p -> String.valueOf(p.getId()).equals(i.trim()));
            if (sc == null) System.out.println("Error in selection");
            else setSelectedComponent(sc);
        });

        Optional<Component> rez = selectedComponent != null ? Optional.of(selectedComponent) : Optional.empty();
        setSelectedComponent(null);
        return rez;
    }

    private Component selectedComponent;

    public void setSelectedComponent(Component selectedProject)
    {
        this.selectedComponent = selectedProject;
    }

    public static final String FACTORY = "componentSelectionFactory";


    @Lazy
    @org.springframework.stereotype.Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ComponentSelection>
    {
        private ComponentBase.Builder baseBuilder;
        private ConsoleInput inputProcessor;
        private Factory<Projects> projectsFactory;

        private Builder()
        {
            super(ComponentSelection.class);
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

        @Autowired
        public Builder projectsFactory(@Named(Projects.FACTORY) Factory<Projects> projectsFactory)
        {
            this.projectsFactory = projectsFactory;
            return this;
        }

        public ComponentSelection build()
        {
            return new ComponentSelection(this);
        }
    }
}

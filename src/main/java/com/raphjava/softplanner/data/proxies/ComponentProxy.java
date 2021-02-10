package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.data.interfaces.DataService;
import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.SubComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

public class ComponentProxy extends ProxyBase
{

    private Component component;

    private ComponentProxy(Builder builder)
    {
        super(builder.component.getClass().getSimpleName());
        dataService = builder.dataService;
        component = builder.component;
    }

    private SubComponentDetailProxy subComponentDetail;

    public SubComponentDetailProxy getSubComponentDetail()
    {
        throwNotImplementedEx();
        return null;
    }

    private Collection<SubComponentProxy> subComponents = new LinkedHashSet<>();

    public Collection<SubComponentProxy> getSubComponents(boolean... force)
    {
        ensureLoaded(Component.SUB_COMPONENTS, force, this::loadSubComponentsFromModel, this::loadSubComponentsFromRepository);
        return subComponents;
    }

    private void loadSubComponentsFromRepository()
    {
        subComponents.addAll(loadSubComponents(get(Component.class, component.getId(), false, e -> e
        .include(Component.SUB_COMPONENTS)).getSubComponents()));
    }



    private Boolean loadSubComponentsFromModel()
    {
        subComponents.clear();
        subComponents.addAll(loadSubComponents(component.getSubComponents()));
        return !subComponents.isEmpty();
    }

    private Collection<SubComponentProxy> loadSubComponents(Collection<SubComponent> subComponents)
    {
        Collection<SubComponentProxy> r = new ArrayList<>();
        for(SubComponent s : subComponents)
        {
            r.add(SubComponentProxy.newBuilder().dataService(dataService)
                    .subComponent(s).build());
        }
        return r;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public Component getComponent()
    {
        return component;
    }


    public static final class Builder
    {
        private DataService dataService;
        private Component component;

        private Builder()
        {
        }

        public Builder dataService(DataService dataService)
        {
            this.dataService = dataService;
            return this;
        }

        public Builder component(Component component)
        {
            this.component = component;
            return this;
        }

        public ComponentProxy build()
        {
            return new ComponentProxy(this);
        }
    }
}

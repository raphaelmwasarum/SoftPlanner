package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.data.interfaces.DataService;
import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.SubComponent;
import com.raphjava.softplanner.data.models.SubComponentDetail;

import java.util.Objects;

public class SubComponentDetailProxy extends ProxyBase
{

    private SubComponentDetail subComponentDetail;

    private SubComponentDetailProxy(Builder builder)
    {
        super(builder.subComponentDetail.getClass().getSimpleName());
        dataService = builder.dataService;
        subComponentDetail = builder.subComponentDetail;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public SubComponentDetail getSubComponentDetail()
    {
        return subComponentDetail;
    }

    private ComponentProxy component;

    public ComponentProxy getComponent(boolean... force)
    {
        ensureLoaded(SubComponentDetail.COMPONENT, force, this::loadComponentFromModel, this::loadComponentFromRepo);
        return component;
    }

    private void loadComponentFromRepo()
    {
        component = newComponentProxy(get(SubComponentDetail.class, subComponentDetail.getId(), false, e ->
                e.include(SubComponentDetail.COMPONENT)).getComponent());
    }

    private Boolean loadComponentFromModel()
    {
        if(subComponentDetail.getComponent() != null) component = newComponentProxy(subComponentDetail.getComponent());
        return component != null;
    }


    private SubComponentProxy subComponent;

    public SubComponentProxy getSubComponent(boolean... force)
    {
        ensureLoaded(SubComponentDetail.SUB_COMPONENT, force, this::loadSubComponentFromModel, this::loadSubComponentFromRepo);
        return subComponent;
    }

    private void loadSubComponentFromRepo()
    {
         subComponent = newSubComponentProxy(get(SubComponentDetail.class, subComponentDetail.getId(), false, e ->
                e.include(SubComponentDetail.SUB_COMPONENT)).getSubComponent());
    }

    private SubComponentProxy newSubComponentProxy(SubComponent subComponent)
    {
        SubComponent x = Objects.requireNonNull(subComponent);
        return SubComponentProxy.newBuilder().dataService(dataService).subComponent(x).build();
    }

    private Boolean loadSubComponentFromModel()
    {
        if(subComponentDetail.getComponent() != null)
        {
            component = newComponentProxy(subComponentDetail.getComponent());
        }

        return component != null;
    }

    private ComponentProxy newComponentProxy(Component component)
    {
        Component x = Objects.requireNonNull(component);
        return ComponentProxy.newBuilder().dataService(dataService).component(x).build();
    }


    public static final class Builder
    {
        private DataService dataService;
        private SubComponentDetail subComponentDetail;

        private Builder()
        {
        }

        public Builder dataService(DataService dataService)
        {
            this.dataService = dataService;
            return this;
        }

        public Builder subComponentDetail(SubComponentDetail subComponentDetail)
        {
            this.subComponentDetail = subComponentDetail;
            return this;
        }

        public SubComponentDetailProxy build()
        {
            return new SubComponentDetailProxy(this);
        }
    }
}

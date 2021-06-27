//package com.raphjava.softplanner.data.proxies;
//
//import com.raphjava.softplanner.data.interfaces.DataService;
//import com.raphjava.softplanner.data.models.Component;
//import com.raphjava.softplanner.data.models.SubComponent;
//import com.raphjava.softplanner.data.models.SubComponentDetail;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.LinkedHashSet;
//import java.util.Objects;
//
//public class ComponentProxy_Old extends ProxyBase
//{
//
//    private Component component;
//
//    private ComponentProxy_Old(Builder builder)
//    {
//        super(builder.component.getClass().getSimpleName());
//        dataService = builder.dataService;
//        component = builder.component;
//    }
//
//    private SubComponentDetailProxy subComponentDetail;
//
//    public SubComponentDetailProxy getSubComponentDetail(boolean... force)
//    {
//        ensureLoaded(Component.SUB_COMPONENT_DETAIL, force, this::loadSubComponentDetailFromModel, this::loadSubComponentDetailFromRepo);
//        return subComponentDetail;
//    }
//
//    private void loadSubComponentDetailFromRepo()
//    {
//        subComponentDetail = newSubComponentDetailProxy(get(Component.class, component.getId(), false
//                , e -> e.include(Component.SUB_COMPONENT_DETAIL)).getSubComponentDetail());
//
//    }
//
//    private Boolean loadSubComponentDetailFromModel()
//    {
//        if(component.getSubComponentDetail() != null)
//        {
//            subComponentDetail = newSubComponentDetailProxy(component.getSubComponentDetail());
//        }
//        return subComponentDetail != null;
//
//    }
//
//    private SubComponentDetailProxy newSubComponentDetailProxy(SubComponentDetail subComponentDetail)
//    {
//        SubComponentDetail x = Objects.requireNonNull(subComponentDetail);
//        return SubComponentDetailProxy.newBuilder().dataService(dataService)
//                .subComponentDetail(x).build();
//    }
//
//    private Collection<SubComponentProxy_Old> subComponents = new LinkedHashSet<>();
//
//    public Collection<SubComponentProxy_Old> getSubComponents(boolean... force)
//    {
//        ensureLoaded(Component.SUB_COMPONENTS, force, this::loadSubComponentsFromModel, this::loadSubComponentsFromRepository);
//        return subComponents;
//    }
//
//    private void loadSubComponentsFromRepository()
//    {
//        subComponents.addAll(loadSubComponents(get(Component.class, component.getId(), false, e -> e
//        .include(Component.SUB_COMPONENTS)).getSubComponents()));
//    }
//
//    private Collection<SubComponentProxy_Old> loadSubComponents(Collection<SubComponent> subComponents)
//    {
//        Collection<SubComponentProxy_Old> r = new ArrayList<>();
//        for(SubComponent s : subComponents)
//        {
//            r.add(SubComponentProxy_Old.newBuilder().dataService(dataService)
//                    .subComponent(s).build());
//        }
//        return r;
//    }
//
//
//
//    private Boolean loadSubComponentsFromModel()
//    {
//        subComponents.clear();
//        subComponents.addAll(loadSubComponents(component.getSubComponents()));
//        return !subComponents.isEmpty();
//    }
//
//    public static Builder newBuilder()
//    {
//        return new Builder();
//    }
//
//    public Component getComponent()
//    {
//        return component;
//    }
//
//
//    public static final class Builder
//    {
//        private DataService dataService;
//        private Component component;
//
//        private Builder()
//        {
//        }
//
//        public Builder dataService(DataService dataService)
//        {
//            this.dataService = dataService;
//            return this;
//        }
//
//        public Builder component(Component component)
//        {
//            this.component = component;
//            return this;
//        }
//
//        public ComponentProxy_Old build()
//        {
//            return new ComponentProxy_Old(this);
//        }
//    }
//}

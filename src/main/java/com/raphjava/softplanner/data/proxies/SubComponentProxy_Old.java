//package com.raphjava.softplanner.data.proxies;
//
//import com.raphjava.softplanner.data.interfaces.DataService;
//import com.raphjava.softplanner.data.models.SubComponent;
//import com.raphjava.softplanner.data.models.SubComponentDetail;
//
//public class SubComponentProxy_Old extends ProxyBase
//{
//
//    private SubComponent subComponent;
//
//    private SubComponentProxy_Old(Builder builder)
//    {
//        super(builder.subComponent.getClass().getSimpleName());
//        dataService = builder.dataService;
//        subComponent = builder.subComponent;
//    }
//
//
//    private SubComponentDetailProxy subComponentDetail;
//
//    public SubComponentDetailProxy getSubComponentDetail(boolean... force)
//    {
//        ensureLoaded(SubComponent.SUB_COMPONENT_DETAIL, force, this::loadSubComponentDetailFromModel,
//                this::loadSubComponentDetailFromRepository);
//        return subComponentDetail;
//    }
//
//    private void loadSubComponentDetailFromRepository()
//    {
//        String pName = SubComponent.SUB_COMPONENT_DETAIL;
//        subComponentDetail = newSubComponentDetailProxy(get(SubComponent.class, subComponent.getId(), false
//        , e -> e.include(pName)).getSubComponentDetail());
//    }
//
//    private Boolean loadSubComponentDetailFromModel()
//    {
//        if(subComponent.getSubComponentDetail() != null) subComponentDetail = newSubComponentDetailProxy(subComponent.getSubComponentDetail());
//        return subComponentDetail != null;
//    }
//
//    private SubComponentDetailProxy newSubComponentDetailProxy(SubComponentDetail detail)
//    {
//        return SubComponentDetailProxy.newBuilder()
//                .dataService(dataService).subComponentDetail(detail).build();
//    }
//
//    public static Builder newBuilder()
//    {
//        return new Builder();
//    }
//
//
//    public SubComponent getSubComponent()
//    {
//        return subComponent;
//    }
//
//    public ComponentProxy getParentComponent()
//    {
//        throwNotImplementedEx();
//        return null;
//    }
//
//
//    public static final class Builder
//    {
//        private DataService dataService;
//        private SubComponent subComponent;
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
//        public Builder subComponent(SubComponent subComponent)
//        {
//            this.subComponent = subComponent;
//            return this;
//        }
//
//        public SubComponentProxy_Old build()
//        {
//            return new SubComponentProxy_Old(this);
//        }
//    }
//}

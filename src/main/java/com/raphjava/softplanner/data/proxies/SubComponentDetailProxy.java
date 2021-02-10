package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.data.interfaces.DataService;
import com.raphjava.softplanner.data.models.SubComponent;
import com.raphjava.softplanner.data.models.SubComponentDetail;

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

    public ComponentProxy getComponent()
    {
        return component;
    }

    public SubComponentProxy getSubComponent()
    {
        throwNotImplementedEx();
        return null;
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

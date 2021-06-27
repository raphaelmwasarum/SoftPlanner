package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.data.models.SubComponent;

public class SubComponentProxy extends SubComponent
{

    private ProxyAssistant assistant;

    private ProxyFactory_Template proxyFactory;

    private SubComponentProxy(Builder builder)
    {
        this.assistant = builder.assistant;
        this.proxyFactory = builder.proxyFactory;
    }


    public static Builder builder()
    {
        return new Builder();
    }

    public static class Builder
    {
        private ProxyAssistant assistant;
        private ProxyFactory_Template proxyFactory;

        private Builder()
        {
        }

        public Builder setAssistant(ProxyAssistant assistant)
        {
            this.assistant = assistant;
            return this;
        }

        public Builder setProxyFactory(ProxyFactory_Template proxyFactory)
        {
            this.proxyFactory = proxyFactory;
            return this;
        }

        public SubComponentProxy build()
        {
            return new SubComponentProxy(this);
        }
    }
}

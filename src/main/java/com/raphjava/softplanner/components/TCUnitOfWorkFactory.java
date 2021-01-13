package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import net.raphjava.studeeconsole.components.interfaces.*;
//
//public class TCUnitOfWorkFactory implements UnitOfWorkFactory
//{
//    private TCUtils tcUtils;
//    private EagerLoaderFactoryProvider eagerLoaderFactoryProvider;
//    private SessionFactoryProvider sessionFactoryProvider;
//
//
//    public TCUnitOfWorkFactory(SessionFactoryProvider sessionFactoryProvider, TCUtils tcUtils, EagerLoaderFactoryProvider e)
//    {
//        this.sessionFactoryProvider = sessionFactoryProvider;
//        this.tcUtils = tcUtils;
//        eagerLoaderFactoryProvider = e;
//    }
//
//    @Override
//    public <ExtrasGetter> UnitOfWork<ExtrasGetter> create()
//    {
//        return new TCUnitOfWork<ExtrasGetter>(sessionFactoryProvider.getSessionFactory(), tcUtils, eagerLoaderFactoryProvider);
//    }
//}

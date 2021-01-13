package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import net.raphjava.raphtility.interfaces.Factory;
//import net.raphjava.studeeconsole.components.interfaces.EagerLoader;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
//public class RaphtilityRepositoryBaseArgs<TEntity>
//{
//    private final Factory<EagerLoader<TEntity>> extrasGetterFac;
//
//    public Class<TEntity> getEntityClass()
//    {
//        return entityClass;
//    }
//
//    public Factory<EagerLoader<TEntity>> getExtrasGetterFac()
//    {
//        return extrasGetterFac;
//    }
//
//    private final Class<TEntity> entityClass;
//
//    private List<String> entityBasePropertyNames;
//
//    public List<String> getEntityBasePropertyNames()
//    {
//        return entityBasePropertyNames;
//    }
//
//    public RaphtilityRepositoryBaseArgs(Factory<EagerLoader<TEntity>> extrasGetterFac, Class<TEntity> entityClass, Map<Class, List<String>> propNames)
//    {
//        this.extrasGetterFac = extrasGetterFac;
//        this.entityClass = entityClass;
//        entityBasePropertyNames = propNames.get(entityClass);
//        Objects.requireNonNull(entityBasePropertyNames);
//    }
//}

package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import net.raphjava.raphtility.collectionmanipulation.interfaces.Itemizer;
//import net.raphjava.raphtility.interfaces.Factory;
//import net.raphjava.raphtility.utils.ReflectionHelper;
//import net.raphjava.studeeconsole.components.interfaces.EagerLoader;
//import net.raphjava.studeeconsole.components.interfaces.KeyGenerator;
//import org.hibernate.Session;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.function.Supplier;
//
//public class TCRepositoryBaseArgs<TEntity>
//{
//    private final Supplier<Session> sessionSupplier;
//    private final KeyGenerator keyGenerator;
//    private final ReflectionHelper reflectionHelper;
//    private final HibernateMapping hibernateMapping;
//
//    public Itemizer getSequenceManipulator()
//    {
//        return sequenceManipulator;
//    }
//
//    private final Itemizer sequenceManipulator;
//
//    public Class<TEntity> getEntityClass()
//    {
//        return entityClass;
//    }
//
//    private final Class<TEntity> entityClass;
//
//    public Factory<EagerLoader<TEntity>> getExtrasGetterFac()
//    {
//        return extrasGetterFac;
//    }
//
//    private final Factory<EagerLoader<TEntity>> extrasGetterFac;
//
//
//
//    public KeyGenerator getKeyGenerator()
//    {
//        return keyGenerator;
//    }
//
//    public Supplier<Session> getSessionSupplier()
//    {
//        return sessionSupplier;
//    }
//
//    public ReflectionHelper getReflectionHelper()
//    {
//        return reflectionHelper;
//    }
//
//    public HibernateMapping getHibernateMapping()
//    {
//        return hibernateMapping;
//    }
//
//    private List<String> entityBasePropertyNames;
//
//    public List<String> getEntityBasePropertyNames()
//    {
//        return entityBasePropertyNames;
//    }
//
//    public TCRepositoryBaseArgs(RaphtilityRepositoryBaseArgs<TEntity> baseArgs, HibernateRepositoryArgs hibArgs, TCUtils tCGeneralUtilities)
//    {
//        extrasGetterFac = baseArgs.getExtrasGetterFac();
//        entityClass = baseArgs.getEntityClass();
//        sessionSupplier = hibArgs.getSessionSupplier();
//        keyGenerator = tCGeneralUtilities.getKeyGenerator();
//        hibernateMapping = hibArgs.getHibernateMapping();
//        reflectionHelper = tCGeneralUtilities.getReflectionHelper();
//        sequenceManipulator = tCGeneralUtilities.getLook();
//        entityBasePropertyNames = tCGeneralUtilities.getEntityClassPropertyNames().get(entityClass);
//        Objects.requireNonNull(entityBasePropertyNames);
//    }
//
//
//}

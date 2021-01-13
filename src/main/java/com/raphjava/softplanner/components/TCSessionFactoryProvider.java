package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import net.raphjava.studeeconsole.components.interfaces.SessionFactoryProvider;
//import org.hibernate.SessionFactory;
//import org.hibernate.cfg.Configuration;
//
//public class TCSessionFactoryProvider implements SessionFactoryProvider
//{
//
//    public void setSessionFactory(SessionFactory sessionFactory)
//    {
//        this.sessionFactory = sessionFactory;
//    }
//
//    private SessionFactory sessionFactory;
//
//    public synchronized SessionFactory getSessionFactory()
//    {
//        if(sessionFactory == null) sessionFactory = new Configuration().configure().buildSessionFactory();
//        return sessionFactory;
//    }
//
//
//    public TCSessionFactoryProvider()
//    {
//
//    }
//}

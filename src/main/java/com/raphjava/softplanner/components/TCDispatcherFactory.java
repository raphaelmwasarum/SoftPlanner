package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import net.raphjava.studee.main.Studee;
//import net.raphjava.studeeconsole.components.interfaces.DispatcherFactory;
//import net.raphjava.studeeconsole.components.interfaces.instance;
//import org.apache.log4j.Logger;
//
//public class TCDispatcherFactory implements DispatcherFactory
//{
//    Logger logger = Logger.getLogger(getClass().getSimpleName());
//
//    public TCDispatcherFactory()
//    {
//        logger.debug("Dispatcher factory created.");
//    }
//
//    @Override
//    public instance create()
//    {
//
//        if(Studee.isGUIInstance())
//        {
//            logger.debug("[" + getClass().getSimpleName() + "] - This is a GUI instance.");
//            return new JavaFXDispatcher();
//        }
//        logger.debug("[" + getClass().getSimpleName() + "] - This is a Console instance.");
//        return new ConsoleDispatcher();
//    }
//}

package com.raphjava.softplanner.services;//package net.raphjava.studeeconsole.services;
//
//import net.raphjava.raphtility.messaging.Message;
//import net.raphjava.studeeconsole.components.interfaces.KeyGenerator;
//import net.raphjava.studeeconsole.interfaces.Communication;
//import net.raphjava.studeeconsole.interfaces.KeygenCommunication;
//import com.raphjava.softplanner.data.models.Notification;
//
//import java.lang.reflect.Type;
//import java.util.List;
//import java.util.function.Consumer;
//
//public class TCKeygenCommunication implements KeygenCommunication
//{
//    private KeyGenerator keygen;
//    private Communication comm;
//
//    @Override
//    public List<Double> getUsedKeys()
//    {
//        return keygen.getUsedKeys();
//    }
//
//    @Override
//    public double getKey()
//    {
//        return keygen.getKey();
//    }
//
//    public TCKeygenCommunication(KeyGenerator keyGenerator, Communication messenger)
//    {
//        keygen = keyGenerator;
//        comm = messenger;
//    }
//
//
//    @Override
//    public <TMessage extends Message<Type, Double, Notification>> void register(Object o, Class<TMessage> aClass, Consumer<TMessage> consumer)
//    {
//        comm.register(o, aClass, consumer);
//    }
//
//    @Override
//    public void unRegister(Object recipient)
//    {
//        comm.unRegister(recipient);
//    }
//
//    @Override
//    public <TMessage extends Message<Type, Double, Notification>> void unRegister(Object recipient, TMessage tMessage)
//    {
//        comm.unRegister(recipient, tMessage);
//    }
//
//    @Override
//    public void sendMessage(Notification notification, Double senderToken, Type... senderType)
//    {
//        comm.sendMessage(notification, senderToken, senderType);
//    }
//
//    @Override
//    public <Content> void sendMessage(Notification notification, Content content, Double senderToken, Type... senderType)
//    {
//        comm.sendMessage(notification, content, senderToken, senderType);
//    }
//
//
//}

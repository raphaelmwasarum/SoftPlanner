package com.raphjava.softplanner.services;

import com.raphjava.softplanner.annotations.Basic;
import com.raphjava.softplanner.annotations.Logging;
import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.LoggableBase;
import com.raphjava.softplanner.components.interfaces.KeyGenerator;
import com.raphjava.softplanner.data.models.Notification;
import com.raphjava.softplanner.interfaces.Communication;
import net.raphjava.raphtility.logging.interfaces.LoggerFactory;
import net.raphjava.raphtility.messaging.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.function.Consumer;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

//import data.models.Notification;

@Lazy
//@Component
@Logging
//@Scope(Singleton)
public class LoggingStuCommunication extends LoggableBase implements Communication
{


    private Communication communication;

    private LoggingStuCommunication(Builder builder)
    {
        loggerFactory = builder.loggerFactory;
        keyGenerator = builder.keyGenerator;
        communication = builder.communication;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }


    @Override
    public <TMessage extends Message<Type, Double, Notification>> void register(Object o, Class<TMessage> aClass, Consumer<TMessage> consumer)
    {
        String opID = getFormattedLoggerName();
        String msg = "Registering for a message of type " + aClass.getSimpleName() + " for object [" + o.toString() + "]" ;
        debug(opID + fullStopAndSpace + msg);
        communication.register(o, aClass, consumer);
        debug(opID + fullStopAndSpace + msg + space + successful + period);
    }

    @Override
    public void unRegister(Object o)
    {
        String opID = getFormattedLoggerName();
        String msg = "Unregistering object: " + o.toString();
        debug(opID + fullStopAndSpace + msg);
        communication.unRegister(o);
        debug(opID + fullStopAndSpace + msg + space + successful + period);
    }

    @Override
    public <TMessage extends Message<Type, Double, Notification>> void unRegister(Object o, TMessage tMessage)
    {
        String opID = getFormattedLoggerName();
        String msg = "Unregistering object: " + o.toString() + space + "from message" + space + tMessage.getTheNotification().toString() + period;
        communication.unRegister(o, tMessage);
        debug(opID + fullStopAndSpace + msg + space + successful + period);
    }

    @Override
    public void sendMessage(Notification notification, Double senderToken, Type... senderType)
    {
        String opID = getFormattedLoggerName();
        String msg = "Sending message: " + notification.toString();
        debug(opID + fullStopAndSpace + msg);
        communication.sendMessage(notification, senderToken, senderType);
        debug(opID + fullStopAndSpace + msg + space + successful);
    }

    @Override
    public <Content> void sendMessage(Notification notification, Content content, Double senderToken, Type... senderType)
    {
        String opID = getFormattedLoggerName();
        String msg = "Sending message: " + notification.toString();
        debug(opID + fullStopAndSpace + msg);
        communication.sendMessage(notification, content, senderToken, senderType);
        debug(opID + fullStopAndSpace + msg + space + successful);
    }


    @Lazy
    @Component
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<LoggingStuCommunication>
    {
        private LoggerFactory loggerFactory;
        private KeyGenerator keyGenerator;
        private Communication communication;

        @Override
        public boolean isSingleton()
        {
            return true;
        }

        private Builder()
        {
            super(LoggingStuCommunication.class);
        }

        @Autowired
        public Builder setLoggerFactory(LoggerFactory loggerFactory)
        {
            this.loggerFactory = loggerFactory;
            return this;
        }

        @Autowired
        public Builder setKeyGenerator(KeyGenerator keyGenerator)
        {
            this.keyGenerator = keyGenerator;
            return this;
        }

        @Autowired
        public Builder setCommunication(@Basic Communication communication)
        {
            this.communication = communication;
            return this;
        }

        public LoggingStuCommunication build()
        {
            return new LoggingStuCommunication(this);
        }
    }
}

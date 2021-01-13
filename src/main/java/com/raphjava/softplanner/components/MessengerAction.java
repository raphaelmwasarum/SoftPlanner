package com.raphjava.softplanner.components;

import com.raphjava.softplanner.data.models.Notification;
import net.raphjava.raphtility.messaging.Message;

import java.lang.reflect.Type;
import java.util.function.Consumer;

//import data.models.Notification;

public class MessengerAction<Arg extends Message<Type, Double, Notification>> implements Consumer<Arg>
{
    private final Double senderToken;
    private Consumer action;
    private Notification notification;


    public MessengerAction(Notification notification, Double senderToken, Consumer action)
    {
        this.notification = notification;
        this.senderToken = senderToken;
        this.action = action;
    }

    @Override
    public void accept(Arg arg)
    {
        if((arg.getTheNotification() == notification) && (senderToken != (double) arg.getSenderToken()))
        {
            if(action != null)
            {
                action.accept(arg);
            }
        }

    }
}

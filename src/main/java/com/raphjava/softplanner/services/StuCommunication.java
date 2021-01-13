package com.raphjava.softplanner.services;

import com.raphjava.softplanner.annotations.Basic;
import com.raphjava.softplanner.data.models.Notification;
import com.raphjava.softplanner.interfaces.Communication;
import net.raphjava.raphtility.messaging.CommunicationBase;
import net.raphjava.raphtility.messaging.interfaces.Messenger;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

//import data.models.Notification;

@Lazy
@Component
@Basic
@Scope(Singleton)
public class StuCommunication extends CommunicationBase<Notification, Double> implements Communication
{
    public StuCommunication()
    {

    }

    /**This is not the default constructor. The 'no parameters' one already wires up this object with Raphtility's
     Mediator in the instantiation of CommunicationBase.
     Use this constructor only if you want to set your own messenger.
     * @param messenger
     */

    public StuCommunication(Messenger messenger)
    {
        this.messenger = messenger;
    }
}

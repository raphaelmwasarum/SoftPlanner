package com.raphjava.softplanner.components;

//import data.models.Notification;

import com.raphjava.softplanner.data.models.Notification;

public class OperationData
{
    private int operationID;

    private Notification notification;

    public int getOperationID()
    {
        return operationID;
    }

    public Notification getNotification()
    {
        return notification;
    }

    public OperationData(int operationID, Notification notification)
    {
        this.operationID = operationID;
        this.notification = notification;
    }
}

package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.KeyGenerator;
import net.raphjava.raphtility.logging.interfaces.Log;
import net.raphjava.raphtility.logging.interfaces.LoggerFactory;

//import net.raphjava.studeeconsole.interfaces.Log;


public abstract class LoggableBase
{
    protected String opIDtxt = "Operation id: ";
    protected String successful = "successful.";
    protected String unSuccessful = "unsuccessful";
    protected String space = " ";
    protected String fullStopAndSpace = ". ";
    protected String period = ".";
    protected String defaultErrorMessageShort = "Something has gone wrong.";
    protected String defaultErrorMessage = "Something has gone wrong. Details:";

    protected LoggerFactory loggerFactory;

    protected KeyGenerator keyGenerator;

    protected Log logger;


    public Log getNewLogger(Class newLoggerOwnerClassType)
    {
        return  loggerFactory.createLogger(newLoggerOwnerClassType.getSimpleName() + " of ID: " + (int)keyGenerator.getKey());
    }

    protected String getFormattedLoggerName()
    {
        return "[" + getClass().getSimpleName() + "]";
    }

    protected void debug(String message)
    {
        logger().debug(getFormattedLoggerName() + " - " + message);
    }

    private Log logger()
    {
        if(logger == null)
        {
            logger = loggerFactory.createLogger(getFormattedLoggerName());
        }
        return logger;
    }

    protected synchronized String getOperationIDDescription()
    {
        return opIDtxt + keyGenerator.getKey();
    }



}

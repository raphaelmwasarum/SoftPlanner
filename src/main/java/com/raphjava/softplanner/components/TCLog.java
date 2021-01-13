package com.raphjava.softplanner.components;

//import net.raphjava.studeeconsole.interfaces.Log;
import org.apache.log4j.Logger;

public class TCLog implements net.raphjava.raphtility.logging.interfaces.Log
{
    private Logger logger;

    public TCLog(Logger logger)
    {
        this.logger = logger;
    }

    @Override
    public String getName()
    {
        return logger.getName();
    }

    @Override
    public void debug(String message)
    {
        logger.debug(message);
    }

    @Override
    public void warn(String message)
    {
        logger.warn(message);
    }

    @Override
    public void info(String message)
    {
        logger.info(message);
    }

    @Override
    public void error(String message)
    {
        logger.error(message);
    }

    @Override
    public void fatal(String message)
    {
        logger.fatal(message);
    }
}

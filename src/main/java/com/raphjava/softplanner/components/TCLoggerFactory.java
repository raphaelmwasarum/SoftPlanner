package com.raphjava.softplanner.components;

//import net.raphjava.studeeconsole.interfaces.Log;

import net.raphjava.raphtility.logging.interfaces.Log;
import net.raphjava.raphtility.logging.interfaces.LoggerFactory;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.raphjava.softplanner.annotations.Scope.Prototype;

//import net.raphjava.raphtility.logging.interfaces.LoggerFactory;

@Component
@Scope(Prototype)
@Lazy
public class TCLoggerFactory implements LoggerFactory
{
    public TCLoggerFactory()
    {

    }

    @Override
    public synchronized Log createLogger(String loggerName)
    {
        return new TCLog(Logger.getLogger(loggerName));
    }
}

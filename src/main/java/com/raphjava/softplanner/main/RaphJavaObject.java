package com.raphjava.softplanner.main;

import net.raphjava.raphtility.utils.Utils;

import java.util.Objects;

public class RaphJavaObject
{

    protected <T> T ifNull(T obj, T alternative)
    {
        if(obj != null) return obj;
        else return alternative;
    }

    protected <T> T ifNullX(T obj, String message)
    {
        return Objects.requireNonNull(obj, message);
    }

    protected boolean isBlank(String s)
    {
        return Utils.isBlank(s);
    }
}

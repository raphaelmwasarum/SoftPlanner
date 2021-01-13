package com.raphjava.softplanner.interfaces;

public interface DTO
{

    <X> X get(String var1, Class<X> var2);

    Object get(String var1);

    <X> X get(int var1, Class<X> var2);

    Object get(int var1);

    Object[] toArray();

}

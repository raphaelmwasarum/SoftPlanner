package com.raphjava.softplanner.data.interfaces;


public interface EagerLoader<TEntity>
{
    <TResult> EagerLoader<TEntity> include(String path);


}

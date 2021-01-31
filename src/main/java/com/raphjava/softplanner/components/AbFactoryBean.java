package com.raphjava.softplanner.components;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

/** An abstract FactoryBean implementation. AbFactoryBean::build() method is the instantiation logic. Builds prototypes by default.
 * If you want singletons override the isSingleton method and make it return 'true'.
 * @param <T> the bean this builder builds.
 */
public abstract class AbFactoryBean<T> extends AbstractFactory<T> implements FactoryBean<T>, InitializingBean
{
    private final Class<T> clazz;
    private boolean earlyRiser;

    public abstract T build();

    public AbFactoryBean(Class<T> c, boolean... earlyRiser)
    {
        clazz = c;
        setEarlyRiser(earlyRiser);
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        System.out.println(String.format("Thread: %s. Builder for object %s dependency injection complete.", Thread.currentThread(), Objects.requireNonNull(getObjectType()).getSimpleName()));
    }



    private void setEarlyRiser(boolean[] earlyRiser)
    {
        this.earlyRiser = earlyRiser.length != 0 && earlyRiser[0];
    }

    public boolean isEarlyRiser()
    {
        return earlyRiser;
    }

    @Override
    public boolean isSingleton()
    {
        return false;
    }

    @Override
    public T getObject() throws Exception
    {
        return build();
    }

    @Override
    public Class<?> getObjectType()
    {
        return clazz;
    }
}

package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.Factory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Function;

@Lazy
@Component
public class SimpleFactory<T> implements Factory<T>
{
    private final String instanceId;

    public SimpleFactory(String instanceId)
    {
        this.instanceId = instanceId;
    }

    private Function<String, T> instanceGetter;

    public Function<String, T> getInstanceGetter()
    {
        return instanceGetter;
    }


    public void setInstanceGetter(Function<String, T> instanceGetter)
    {
        this.instanceGetter = instanceGetter;
    }

    @Override
    public synchronized T createProduct()
    {
        return instanceGetter.apply(instanceId);
    }

    @Override
    public T createProduct(Map<Integer, Object> map)
    {
        throw new RuntimeException("This factory doesn't support the createProduct(parameters) method.");
    }

    @Override
    public void deleteProduct(T t)
    {
        throw new RuntimeException("This factory doesn't support the deleteProduct method.");

    }
}

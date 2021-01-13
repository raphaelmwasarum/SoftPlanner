package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.BiFactory;
import com.raphjava.softplanner.components.interfaces.Factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TCBiFactory<Product1, Product2> implements BiFactory<Product1, Product2>
{


    private final Factory<Product1> factory1;
    private final Factory<Product2> factory2;
    private final Map<Class, Consumer<Object>> deleters;

    public TCBiFactory(Factory<Product1> f1, Factory<Product2> f2)
    {
        factory1 = f1;
        factory2 = f2;
        deleters = new HashMap<Class, Consumer<Object>>();
    }

    @Override
    public Product1 createProduct()
    {
        return factory1.createProduct();
    }

    @Override
    public Product1 createProduct(Map<Integer, Object> otherConstructorArgs)
    {
        Product1 s = factory1.createProduct(otherConstructorArgs);
        if(!deleters.containsKey(s.getClass())) deleters.put(s.getClass(), o -> factory1.deleteProduct((Product1) o));
        return s;
    }

    @Override
    public void deleteProduct(Product1 product1)
    {
        deleters.get(product1.getClass()).accept(product1);
    }


    @Override
    public Product2 createProduct2()
    {
        Product2 a = factory2.createProduct();
        if(!deleters.containsKey(a.getClass())) deleters.put(a.getClass(), o -> factory2.deleteProduct((Product2) o));
        return a;
    }

    @Override
    public Product2 createProduct2(Map<Integer, Object> otherConstructors)
    {
        Product2 a = factory2.createProduct(otherConstructors);
        if(!deleters.containsKey(a.getClass())) deleters.put(a.getClass(), o -> factory2.deleteProduct((Product2) o));
        return a;
    }

    @Override
    public void delete(Object object)
    {
        Class<?> entityClass = object.getClass();
        deleters.get(entityClass).accept(object);
    }
}

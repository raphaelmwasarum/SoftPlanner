package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.Factory;
import com.raphjava.softplanner.components.interfaces.MultiFactory;

import java.util.Map;

public class TCMultiFactory<Product1, Product2, Product3> extends TCBiFactory<Product1, Product2> implements MultiFactory<Product1, Product2, Product3>
{


    private final Factory<Product3> factory3;

    public TCMultiFactory(Factory<Product1> f1, Factory<Product2> f2, Factory<Product3> f3)
    {
        super(f1, f2);
        factory3 = f3;
    }


    @Override
    public Product3 createProduct3()
    {
        return factory3.createProduct();
    }

    @Override
    public Product3 createProduct3(Map<Integer, Object> otherConstructorArgs)
    {
        return factory3.createProduct(otherConstructorArgs);
    }
}

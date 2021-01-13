package com.raphjava.softplanner.components.interfaces;

import java.util.Map;

public interface BiFactory<Product1, Product2> extends Factory<Product1>
{
    Product2 createProduct2();

    Product2 createProduct2(Map<Integer, Object> otherConstructors);

    void delete(Object object);
}

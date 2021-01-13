package com.raphjava.softplanner.components.interfaces;

import java.util.Map;

public interface MultiFactory<Product1, Product2, Product3> extends BiFactory<Product1, Product2>
{
    Product2 createProduct2();

    Product2 createProduct2(Map<Integer, Object> otherConstructorArgs);

    Product3 createProduct3();

    Product3 createProduct3(Map<Integer, Object> otherConstructorArgs);

    void delete(Object obj);





}

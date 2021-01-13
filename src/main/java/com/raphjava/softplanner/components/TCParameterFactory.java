package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.Factory;

import java.util.Map;

public class TCParameterFactory<P> extends TCFactory<P> implements Factory<P>
{

    public TCParameterFactory(String fullyQualifiedName, String[] fullyQualifiedParameterTypeNames, Map<Integer, Object> components, Integer factoryIndexAsParameter)
    {
        super(fullyQualifiedName, fullyQualifiedParameterTypeNames, components);
        components.put(factoryIndexAsParameter, this);

    }

}

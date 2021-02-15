package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.data.models.EntityBase;
import com.raphjava.softplanner.data.proxies.interfaces.EntityProxy;
import net.raphjava.qumbuqa.databasedesign.interfaces.MappingManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ProxyFactory
{


    public <ActualEntity extends EntityBase> ActualEntity proxy(ActualEntity actualEntity)
    {
        return newProxy(actualEntity);
    }

    private Map<Class, Function<EntityBase, EntityProxy>> proxyFactories = new HashMap<>();


    private void initialize()
    {
        createFactories();
    }

    private void createFactories()
    {
        //The code for this part is to be generated according to entities annotated with @Proxied.

    }

    @SuppressWarnings("unchecked")
    private <ActualEntity extends EntityBase> ActualEntity newProxy(ActualEntity actualEntity)
    {
        if(proxyFactories.containsKey(actualEntity.getClass()))
        {
            Function<EntityBase, EntityProxy> factory = proxyFactories.get(actualEntity.getClass());
            EntityProxy proxy = factory.apply(actualEntity);
            return (ActualEntity) proxy;
        }
        else
        {
            String entName = actualEntity.getClass().getSimpleName();
            throw new IllegalStateException(String
                    .format("Proxy factory for entity %s does not exist. Did you forget to annotate entity %s with @Proxied?"
                    , entName, entName));
        }
    }
}

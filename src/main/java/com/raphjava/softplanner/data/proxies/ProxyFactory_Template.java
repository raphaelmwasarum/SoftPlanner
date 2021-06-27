package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.data.interfaces.DataService;
import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.EntityBase;
import com.raphjava.softplanner.data.models.SubComponent;
import net.raphjava.qumbuqa.databasedesign.interfaces.MappingManager;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class ProxyFactory_Template
{


    private DataService dataService;

    /**
     * Generates a proxy of the passed entity that facilitates lazy loading of its properties.
     * @param actualEntity instance of the entity whose proxy is needed.
     * @param freshProxy if true, a new instance of the actual entity is fetched from the source and used to build the proxy.
     * @param <ActualEntity> the entity class type.
     * @return a proxy as an ActualEntity.
     */
    public <ActualEntity extends EntityBase> ActualEntity proxy(ActualEntity actualEntity, boolean... freshProxy)
    {
        if(freshProxy.length != 0 && freshProxy[0]) return freshProxy(actualEntity.getClass(), actualEntity.getId());
        else return buildProxy(actualEntity);
    }

    private MappingManager mappingManager;




    @SuppressWarnings("unchecked")
    private <ActualEntity extends EntityBase> ActualEntity freshProxy(Class<? extends EntityBase> entityClass, int entityId)
    {
        EntityBase[] rez = new EntityBase[1];
        dataService.read(r -> r.get(entityClass, entityId).onSuccess(en -> rez[0] = en).onFailure(() -> System.out
                .println(String.format("Failure getting a Entity %s of id: %s from the repository",
                        entityClass.getSimpleName(), entityId))));

        return buildProxy((ActualEntity) rez[0]);

    }

    private Map<Class, UnaryOperator<EntityBase>> proxyFactories = new HashMap<>();


    private void initialize()
    {
        createFactories();
    }

    private void createFactories()
    {
        //The code for this part is to be generated according to entities annotated with @Proxied.
        proxyFactories.put(Component.class, this::buildComponentProxy);
        proxyFactories.put(SubComponent.class, this::buildSubComponentProxy);

    }

    private EntityBase buildSubComponentProxy(EntityBase subComponent)
    {
        SubComponent actualEntity = (SubComponent) subComponent;
        SubComponentProxy proxy = SubComponentProxy.builder().setAssistant(getNewAssistant(subComponent.getClass().getSimpleName())).build();
        proxy.setId(subComponent.getId());
        proxy.setParentComponent(actualEntity.getParentComponent());
        proxy.setSubComponentDetail(actualEntity.getSubComponentDetail());
        return proxy;
    }

    private ComponentProxy buildComponentProxy(EntityBase component)
    {
        Component actualEntity = (Component) component;
        ComponentProxy proxy = ComponentProxy.builder().setAssistant(getNewAssistant(component.getClass().getSimpleName())).build();
        proxy.setId(actualEntity.getId());
        proxy.getSubComponents().addAll(actualEntity.getSubComponents());
        return proxy;
    }

    private ProxyAssistant getNewAssistant(String modelName)
    {
        return new ProxyAssistant(modelName);
    }

    @SuppressWarnings("unchecked")
    private <ActualEntity extends EntityBase> ActualEntity buildProxy(ActualEntity actualEntity)
    {
        if(proxyFactories.containsKey(actualEntity.getClass()))
        {
            UnaryOperator<EntityBase> factory = proxyFactories.get(actualEntity.getClass());
            EntityBase proxy = factory.apply(actualEntity);
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

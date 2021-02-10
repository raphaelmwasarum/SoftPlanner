package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.data.interfaces.DataService;
import com.raphjava.softplanner.data.interfaces.EagerLoader;
import com.raphjava.softplanner.data.models.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * To facilitate lazy loading of an entity's relationship properties.
 */
public abstract class ProxyBase
{

    protected DataService dataService;

    private String proxyName = getClass().getSimpleName();
    private String modelName;

    protected String exceptionMessage(String propertyName)
    {
        return "Error in " + proxyName + " . Failed to load " + modelName + "::" + propertyName;
    }

    public ProxyBase(String modelName)
    {
        this.modelName = modelName;
    }

    protected void ensureLoaded(String propertyName, boolean[] force, Supplier<Boolean> loadFromModel, Runnable loadFromRepo)
    {
        propertyLoaders.computeIfAbsent(propertyName,
                _propertyName -> newPropertyLoader(loadFromModel, loadFromRepo))
                .ensureLoaded(force(force));
    }


    protected void throwNotImplementedEx(String... extraMessage)
    {
        String n = "Not implemented";
        throw new RuntimeException(extraMessage.length == 0 ? n : n + extraMessage[0]);
    }

    @SuppressWarnings("unchecked")
    protected  <T> T get(Class<T> cl, int id, boolean withRelatives, Consumer<EagerLoader<T>> eagerLoaderAction)
    {
        final Object[] rez = new Object[1];
        dataService.read(r ->
        {
            DataService.EntityReadAction<T> v = r.get(cl, id);
            if (withRelatives) v = v.withRelationships();
            if (eagerLoaderAction != null) v.eagerLoad(eagerLoaderAction)
                    .onSuccess(en -> rez[0] = en)
                    .onFailure(() -> System.out.println("proxy data read failure."));
        });

        return (T) rez[0];
    }






    protected String exceptionMessagePrefix = getClass().getSimpleName();

    public PropertyLoader newPropertyLoader(Supplier<Boolean> loadFromModel, Runnable loadFromRepo)
    {
        PropertyLoader pl = new PropertyLoader();
        pl.loadFromModel = loadFromModel;
        pl.loadFromRepository = loadFromRepo;
        return pl;
    }

    protected static class PropertyLoader
    {
        /**
         * Returns true if the load from model is successful.
         */
        public Supplier<Boolean> loadFromModel;

        private boolean propertyLoaded;

        public boolean isPropertyLoaded()
        {
            return propertyLoaded;
        }

        private Runnable loadFromRepository;

        protected void ensureLoaded(boolean force)
        {
            if (force) loadFromRepository.run();
            else if (!propertyLoaded)
            {
                if (!loadFromModel.get())
                {
                    loadFromRepository.run();
                }
            }
            propertyLoaded = true;
        }
    }


    protected Map<String, PropertyLoader> propertyLoaders = new HashMap<>();


    protected boolean force(boolean[] force)
    {
        if (force == null) return false;
        return force.length != 0 && force[0];
    }
}

package com.raphjava.softplanner.services;

import com.raphjava.softplanner.annotations.Basic;
import com.raphjava.softplanner.annotations.Logging;
import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.LoggableBase;
import com.raphjava.softplanner.components.interfaces.KeyGenerator;
import com.raphjava.softplanner.data.interfaces.CRUD;
import com.raphjava.softplanner.data.interfaces.DataService;
import com.raphjava.softplanner.data.models.EntityBase;
import com.raphjava.softplanner.interfaces.Communication;
import net.raphjava.raphtility.logging.interfaces.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

//import net.raphjava.studeeconsole.components.interfaces.CRUD;

@Logging
@Lazy
public class LoggingQuDataService extends LoggableBase implements DataService
{
    private DataService dataService;
    private Communication communication;


//    public LoggingQuDataService(LoggerFactory lh, DataService ds, LoggableCommunication lkc)
//    {
////        super(lh, lkc);
//        dataService = ds;
//        communication = lkc;
//        debug("Finished instantiation.");
//    }

    private LoggingQuDataService(Builder builder)
    {
        loggerFactory = builder.loggerFactory;
        dataService = builder.dataService;
        communication = builder.communication;
        keyGenerator = builder.keyGenerator;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }


    /*@Override
    public <T extends EntityBase> void add(T entity)
    {
        String opID = getOperationIDDescription();
        String msg = "Adding entity to repository";
        debug(opID + fullStopAndSpace + msg + period);
        dataService.add(entity);
        debug(opID + fullStopAndSpace + msg + space + successful);
    }

    @Override
    public <T extends EntityBase> void add(T entity, Consumer<EagerLoader<T>> cascadeActions)
    {
        String opID = getOperationIDDescription();
        String msg = "Adding entity to repository";
        debug(opID + fullStopAndSpace + msg + period);
        dataService.add(entity, cascadeActions);
        debug(opID + fullStopAndSpace + msg + space + successful);
    }


    @Override
    public <T> void addRange(List<T> entities)
    {
        String opID = getOperationIDDescription();
        String msg = "Adding entities to repository";
        debug(opID + fullStopAndSpace + msg + period);
        dataService.addRange(entities);
        debug(opID + fullStopAndSpace + msg + space + successful + period);

    }

    @Override
    public <T extends EntityBase> T addAndReturn(T entity)
    {
        String opID = getOperationIDDescription();
        String msg = "Adding entity to repository";
        T ent = dataService.addAndReturn(entity);
        debug(ent != null? opID + fullStopAndSpace + msg + space + successful + fullStopAndSpace + "Returning added entity" + period : opID + fullStopAndSpace + "Failed to retrieve added entity" + fullStopAndSpace + "Null is being returned instead.");
        return ent;
    }

    @Override
    public <T extends EntityBase> List<T> addAndReturnRange(List<T> entities)
    {
        String opID = getOperationIDDescription();
        String msg = "Adding entity to repository";
        var ent = dataService.addAndReturnRange(entities);
        debug(ent != null? opID + fullStopAndSpace + msg + space + successful + fullStopAndSpace + "Returning added entities" + period : opID + fullStopAndSpace + "Failed to retrieve added entity" + fullStopAndSpace + "Null is being returned instead.");
        return ent;
    }


    @Override
    public <T extends EntityBase> T get(Class<T> entityClass, int ID, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
    {
        String opID = getOperationIDDescription();
        String msg = "Getting entity from repository";
        debug(opID + fullStopAndSpace + msg + period);
        T ent = dataService.get(entityClass, ID, withAllNavigationalProperties, extrasLoaderAction);
        debug(ent != null ? opID + fullStopAndSpace + msg + space + successful + period : opID + fullStopAndSpace + "There's no entity of type " + entityClass.getSimpleName() + "with that ID." + fullStopAndSpace + "Returning null instead" + period);
        return ent;
    }

    @Override
    public <T extends EntityBase> T get(Class<T> entityClass, int ID)
    {
        String opID = getOperationIDDescription();
        String msg = "Getting entity from repository";
        debug(opID + fullStopAndSpace + msg + period);
        T ent = dataService.get(entityClass, ID);
        debug(ent != null ? opID + fullStopAndSpace + msg + space + successful + period : opID + fullStopAndSpace + "There's no entity of type " + entityClass.getSimpleName() + "with that ID." + fullStopAndSpace + "Returning null instead" + period);
        return ent;
    }

    @Override
    public <T extends EntityBase> List<T> getAll(Class<T> entityClass, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
    {
        String opID = getOperationIDDescription();
        String msg = "Getting entities from the repository";
        debug(opID + fullStopAndSpace + msg);
        List<T> entities = dataService.getAll(entityClass, withAllNavigationalProperties, extrasLoaderAction);
        debug(entities != null ? opID + fullStopAndSpace + msg + space + successful + period : opID + fullStopAndSpace + msg + space + unSuccessful + period);
        return entities;
    }

    @Override
    public <T extends EntityBase> void edit(T entity)
    {
        String opID = getOperationIDDescription();
        String msg = "Editing an entity in the repository";
        debug(opID + fullStopAndSpace + msg);
        dataService.edit(entity);
        debug(opID + msg + space + successful + period);
    }

    @Override
    public <T extends EntityBase> void edit(T entity, Consumer<EagerLoader<T>> cascaderAction)
    {
        String opID = getOperationIDDescription();
        String msg = "Editing an entity in the repository";
        debug(opID + fullStopAndSpace + msg);
        dataService.edit(entity, cascaderAction);
        debug(opID + msg + space + successful + period);
    }

    @Override
    public <T extends EntityBase> void editRange(List<T> entities)
    {
        String opID = getOperationIDDescription();
        String msg = "editing entities";
        debug(opID + fullStopAndSpace + msg);
        dataService.editRange(entities);
        debug(opID + msg + space + successful + period);
    }

    @Override
    public <T extends EntityBase> void remove(T entity)
    {
        String opID = getOperationIDDescription();
        String msg = "Removing entity from repository";
        debug(opID + fullStopAndSpace + msg);
        dataService.remove(entity);
        debug(opID + msg + space + successful + period);
    }

    @Override
    public <T extends EntityBase> void removeRange(List<T> entities)
    {
        String opID = getOperationIDDescription();
        String msg = "Removing entities from repository";
        debug(opID + fullStopAndSpace + msg);
        dataService.removeRange(entities);
        debug(opID + msg + space + successful + period);
    }

    @Override
    public <T> void crud(Consumer<CRUD> creationUpdatingDeletionAction)
    {
        String opID = getOperationIDDescription();
        String msg = "Carrying out possible Create, Update, Delete operations with one unit of work.";
        debug(opID + fullStopAndSpace + msg);
        dataService.crud(creationUpdatingDeletionAction);
        debug(opID + msg + space + successful + period);
    }*/

    @Override
    public <T extends EntityBase> T newProxy(T entity)
    {
        return dataService.newProxy(entity);
    }

    @Override
    public void write(Consumer<CRUD> creationUpdatingDeletionAction)
    {
        String opID = getOperationIDDescription();
        String msg = "Carrying out possible Create, Update, Delete operations with one unit of work.";
        debug(opID + fullStopAndSpace + msg);
        dataService.write(creationUpdatingDeletionAction);
        debug(opID + msg + space + successful + period);
    }

    @Override
    public void read(Consumer<ReadAction> readAction)
    {
        String opID = getOperationIDDescription();
        String msg = "Carrying out Reading operations with one unit of work.";
        debug(opID + fullStopAndSpace + msg);
        dataService.read(readAction);
        debug(opID + msg + space + successful + period);
    }

    @Lazy
    @Component
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<LoggingQuDataService>
    {
        private LoggerFactory loggerFactory;
        private KeyGenerator keyGenerator;
        private DataService dataService;
        private Communication communication;

        private Builder()
        {
            super(LoggingQuDataService.class);
        }

        @Autowired
        public Builder setLoggerFactory(LoggerFactory val)
        {
            loggerFactory = val;
            return this;
        }

        @Override
        public boolean isSingleton()
        {
            return true;
        }

        @Autowired
        public Builder setKeyGenerator(KeyGenerator val)
        {
            keyGenerator = val;
            return this;
        }

        public LoggingQuDataService build()
        {
            return new LoggingQuDataService(this);
        }

        @Autowired
        public Builder setDataService(@Basic DataService val)
        {
            dataService = val;
            return this;
        }

        @Autowired
        public Builder setCommunication(@Logging Communication val)
        {
            communication = val;
            return this;
        }

//        @Override
//        public LoggingQuDataService getObject() throws Exception
//        {
//            return build();
//        }
//
//        @Override
//        public Class<?> getObjectType()
//        {
//            return LoggingQuDataService.class;
//        }
    }
}

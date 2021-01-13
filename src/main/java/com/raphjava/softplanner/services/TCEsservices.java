package com.raphjava.softplanner.services;//package net.raphjava.studeeconsole.services;
//
//import net.raphjava.raphtility.asynchrony.Task;
//import net.raphjava.raphtility.collectionmanipulation.Seq;
////import net.raphjava.raphtility.collectionmanipulation.interfaces.Itemize;
//import net.raphjava.raphtility.collectionmanipulation.interfaces.Itemizer;
//import net.raphjava.raphtility.messaging.Message;
//import net.raphjava.studee.views.components.BindablePropertyCommands;
////import net.raphjava.studeeconsole.components.Raphtilities;
//import net.raphjava.studeeconsole.components.interfaces.*;
//import net.raphjava.studeeconsole.interfaces.*;
//import com.raphjava.softplanner.data.models.EntityBase;
//import com.raphjava.softplanner.data.models.Notification;
//
//import javax.persistence.Tuple;
//import java.lang.reflect.Type;
//import java.util.List;
//import net.raphjava.raphtility.collectionmanipulation.interfaces.Set;
//import java.util.function.Consumer;
//import java.util.function.Predicate;
//
//public class TCEsservices implements Esservices
//{
//
////    private final Raphtilities raphtilities;
//    private final BindablePropertyCommands bindablePropertyCommands;
//    private LoggableCommunication loggableKeygenCommunication;
//    private instance dispatcherHelper;
//    private LoggableDataService dataService;
////    private  final Itemizer itemizer;
//
//
//
//    public TCEsservices(LoggableCommunication lkc, DispatcherFactory dh, LoggableDataService ds/*, Raphtilities r*/, BindablePropertyCommands pCommands)
//    {
//        loggableKeygenCommunication = lkc;
//        dispatcherHelper = dh.create();
//        dataService = ds;
////        raphtilities = r;
////        itemizer = raphtilities.getItemizer();
//        bindablePropertyCommands = pCommands;
//    }
//
//    @Override
//    public List<Double> getUsedKeys()
//    {
//        return loggableKeygenCommunication.getUsedKeys();
//    }
//
//    @Override
//    public double getKey()
//    {
//        return loggableKeygenCommunication.getKey();
//    }
//
//   /* @Override
//    public <TMessage extends Message<Type, Notification>> void register(Object o, Type type, Consumer<TMessage> consumer)
//    {
//        loggableKeygenCommunication.register(o, type, consumer);
//    }*/
//
//    @Override
//    public <TMessage extends Message<Type, Double, Notification>> void register(Object o, Class<TMessage> aClass, Consumer<TMessage> consumer)
//    {
//        loggableKeygenCommunication.register(o, aClass, consumer);
//    }
//
//
//
//    @Override
//    public void unRegister(Object o)
//    {
//        loggableKeygenCommunication.unRegister(o);
//    }
//
//    @Override
//    public <TMessage extends Message<Type, Double, Notification>> void unRegister(Object o, TMessage tMessage)
//    {
//        loggableKeygenCommunication.unRegister(o, tMessage);
//    }
//
//    @Override
//    public void sendMessage(Notification notification, Double senderToken, Type... types)
//    {
//        loggableKeygenCommunication.sendMessage(notification, senderToken, types);
//    }
//
//    @Override
//    public <Content> void sendMessage(Notification notification, Content content, Double senderToken, Type... types)
//    {
//        loggableKeygenCommunication.sendMessage(notification, content, senderToken, types);
//    }
//
//    @Override
//    public void checkBeginInvokeOnUI(Runnable action)
//    {
//        dispatcherHelper.checkBeginInvokeOnUI(action);
//    }
//
//    @Override
//    public void invokeOnUI(Runnable action)
//    {
//        dispatcherHelper.invokeOnUI(action);
//    }
//
//    @Override
//    public <T extends EntityBase> void add(T entity)
//    {
//        dataService.add(entity);
//    }
//
//    @Override
//    public <T extends EntityBase> void add(T entity, Consumer<Creation<T>> entityCreator)
//    {
//        dataService.add(entity, entityCreator);
//    }
//
//    @Override
//    public <T> void addRange(List<T> entities)
//    {
//        dataService.addRange(entities);
//    }
//
//    @Override
//    public <T extends EntityBase> T addAndReturn(T entity)
//    {
//        return dataService.addAndReturn(entity);
//    }
//
//    @Override
//    public <T extends EntityBase> T addAndReturn(T entity, Consumer<Creation<T>> entityCreator)
//    {
//        return dataService.addAndReturn(entity, entityCreator);
//    }
//
//    @Override
//    public <T> List<T> addAndReturnRange(List<T> entities)
//    {
//        return dataService.addAndReturnRange(entities);
//    }
//
//    @Override
//    public <T extends EntityBase> T get(Class<T> entityClass, int ID, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
//    {
//        return dataService.get(entityClass, ID, withAllNavigationalProperties, extrasLoaderAction);
//    }
//
//    @Override
//    public <T extends EntityBase> List<T> get(Class<T> entityClass, Predicate<T> predicate, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
//    {
//        return dataService.get(entityClass, predicate, withAllNavigationalProperties, extrasLoaderAction);
//    }
//
//    @Override
//    public <T extends EntityBase> List<T> get(Class<T> entityClass, Consumer<CriteriaBuilder<T>> criteriaBuilderConsumer, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
//    {
//        return dataService.get(entityClass, criteriaBuilderConsumer, withAllNavigationalProperties, extrasLoaderAction);
//    }
//
//    @Override
//    public <T extends EntityBase> List<Tuple> getAndSelect(Class<T> entityClass, List<String> propertyNames, Consumer<CriteriaBuilder> criteriaBuilderConsumer)
//    {
//        return dataService.getAndSelect(entityClass, propertyNames, criteriaBuilderConsumer);
//    }
//
//    /*@Override
//    public <T, TResult> List<TResult> getAndSelect(Class<T> entityClass, Predicate<T> predicate, Function<T, TResult> selector)
//    {
//        return dataService.getAndSelect(entityClass, predicate, selector);
//    }*/
//
//    @Override
//    public <T extends EntityBase> List<T> getAll(Class<T> entityClass, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
//    {
//        return dataService.getAll(entityClass, withAllNavigationalProperties, extrasLoaderAction);
//    }
//
//    @Override
//    public <T> void edit(Class<T> entityClass, T entity)
//    {
//        dataService.edit(entityClass, entity);
//    }
//
//    @Override
//    public <T> void editRange(Class<T> entityClass, List<T> entities)
//    {
//        dataService.editRange(entityClass, entities);
//    }
//
//    @Override
//    public <T> void remove(Class<T> entityClass, T entity)
//    {
//        dataService.remove(entityClass, entity);
//    }
//
//    @Override
//    public <T> void removeRange(Class<T> entityClass, List<T> entities)
//    {
//        dataService.removeRange(entityClass, entities);
//    }
//
//    @Override
//    public <T> void crud(Consumer<CRUD> creationUpdatingDeletionAction)
//    {
//        dataService.crud(creationUpdatingDeletionAction);
//    }
//
//    @Override
//    public Log getLogger()
//    {
//        return loggableKeygenCommunication.getLogger();
//    }
//
//    @Override
//    public void setLogger(Log logger)
//    {
//        loggableKeygenCommunication.setLogger(logger);
//    }
//
//    @Override
//    public Log getNewLogger(Class loggerOwnerClassType)
//    {
//        return loggableKeygenCommunication.getNewLogger(loggerOwnerClassType);
//    }
//
//    @Override
//    public Task startNewTask(Consumer action)
//    {
////        Task newTask = raphtilities.startNewTask(action);
//        getLogger().debug("[Esservices] - The total number of currently running async tasks: " + Task.getRunningTasksCount());
//        getLogger().debug("[Esservices] - The total number of currently running distinct async tasks: " + Task.getDistinctRunningTasksCount());
////        return newTask;
//        return null;
//    }
//
//    @Override
//    public Task startNewTask(Runnable action)
//    {
////        Task newTask = raphtilities.startNewTask(e -> action.run());
//        getLogger().debug("[Esservices] - The total number of currently running async tasks: " + Task.getRunningTasksCount());
//        getLogger().debug("[Esservices] - The total number of currently running distinct async tasks: " + Task.getDistinctRunningTasksCount());
//        /*return newTask;*/return null;
//    }
//
//    @Override
//    public Seq getSeq()
//    {
//        return null; /*raphtilities.getSeq()*/
//    }
//
//   /* @Override
//    public Itemizer getSequenceManipulator()
//    {
//        return raphtilities.getItemizer();
//    }*/
//
//    @Override
//    public Command getBinder()
//    {
//        return bindablePropertyCommands.getBinder();
//    }
//
//    @Override
//    public Command getBinder()
//    {
//        return bindablePropertyCommands.getBinder();
//    }
//
////    @Override
////    public <T> Itemize<T, List<T>> in(List<T> list)
////    {
////        return itemizer.in(list);
////    }
////
////    @Override
////    public <T> Itemize<T, List<T>> in(Set<T> set)
////    {
////        return itemizer.in(set);
////    }
//}

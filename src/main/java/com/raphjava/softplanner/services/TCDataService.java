package com.raphjava.softplanner.services;//package net.raphjava.studeeconsole.services;
//
//import net.raphjava.raphtility.collectionmanipulation.Seq;
//import net.raphjava.raphtility.collectionmanipulation.interfaces.Itemizer;
//import net.raphjava.raphtility.interfaces.Repository;
//import net.raphjava.raphtility.reflection.interfaces.ReflectionHelper;
//import net.raphjava.studeeconsole.components.TCUtils;
//import net.raphjava.studeeconsole.components.interfaces.*;
//import net.raphjava.studeeconsole.components.interfaces.CRUD;
//import net.raphjava.studeeconsole.interfaces.DataService;
//import com.raphjava.softplanner.data.models.EntityBase;
//
//import javax.persistence.Tuple;
//import java.lang.reflect.Method;
//import java.util.*;
//import java.util.function.Consumer;
//import java.util.function.Predicate;
//
//public class TCDataService implements DataService
//{
//    @Deprecated
//    private final Seq seq;
//
//    private final Itemizer look;
//    private KeyGenerator keyGenerator;
//    private UnitOfWorkFactory unitOfWorkFactory;
//    private ReflectionHelper reflectionHelper;
//
//    public TCDataService(TCUtils k, UnitOfWorkFactory fac)
//    {
//        keyGenerator = k.getKeyGenerator();
//        reflectionHelper = k.getReflectionHelper();
//        look = k.getLook();
//        seq = k.getSeq();
//        unitOfWorkFactory = fac;
//    }
//
//
//    private int getKey()
//    {
//        return (int) keyGenerator.getKey();
//    }
//
//    @Override
//    public <T extends EntityBase> void add(T entity)
//    {
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.getRepository((Class<T>) entity.getClass());
//        T ent = repository.setNewID(entity);
//        repository.add(ent);
//        w.complete();
//
//        //TODO make UnitOfWork.complete() also cleanup its instance. Done way earlier, noted on March 11th 2019.
//    }
//
//    @Override
//    public <T extends EntityBase> void add(T entity, Consumer<Creation<T>> entityCreator)
//    {
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.getRepository((Class<T>) entity.getClass());
//        Consumer<net.raphjava.raphtility.interfaces.Creation<T>> entityCreatorAdapter = creation ->
//            entityCreator.accept((Creation<T>) creation);
//        repository.add(entity, entityCreatorAdapter);
//        w.complete();
//    }
//
//    @Override
//    public <T> void addRange(List<T> entities)
//    {
//        if(entities == null) return;
//        if(entities.size() == 0) return;
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.<T>getRepository((Class<T>) entities.get(0).getClass());
//        entities.forEach(repository::setNewID);
//        repository.addRange(entities);
//        w.complete();
//    }
//
//    @Override
//    public <T extends EntityBase> T addAndReturn(T entity)
//    {
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.getRepository((Class<T>) entity.getClass());
//        //int id = getKey();
//        //T ent = repository.setNewID(entity, id);
//        repository.add(entity);
//        w.flush();
//        T result = repository.get(entity.getId(), true, null);
//        w.complete();
//        return result;
//    }
//
//    @Override
//    public <T extends EntityBase> T addAndReturn(T entity, Consumer<Creation<T>> entityCreator)
//    {
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.getRepository((Class<T>) entity.getClass());
//        Consumer<net.raphjava.raphtility.interfaces.Creation<T>> entityCreatorAdapter = creation ->
//                entityCreator.accept((Creation<T>) creation);
//        repository.add(entity, entityCreatorAdapter);
//        w.flush();
//        T result = repository.get(entity.getId(), true, null);
//        w.complete();
//        return result;
//    }
//
//    @Override
//    public <T> List<T> addAndReturnRange(List<T> entities)
//    {
//        if(entities == null)return null;
//        if(entities.size() == 0) return null;
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.getRepository((Class<T>) entities.get(0).getClass());
//        List<T> results = new ArrayList<>();
//        List<Integer> resultIDs = new ArrayList<>();
//        entities.forEach(entity ->
//        {
//            int id = getKey();
//            T ent = repository.setNewID(entity, id);
//            repository.add(ent);
//            resultIDs.add(id);
//        });
//        w.flush();
//        resultIDs.forEach(id -> results.add(repository.get(id, true, null)));
//        w.complete();
//        return results;
//    }
//
//    private List<EntityBase> flattenGraph(EntityBase entity)
//    {
//        if(entity == null) return new ArrayList<>();
//        Queue<EntityBase> q = new LinkedList<>();
//        List<EntityBase> results = new ArrayList<>();
//        q.add(entity);
//        EntityBase current = null;
//        while (q.size() > 0)
//        {
//            current = q.poll();
//            results.add(current);
//            List<EntityBase> children = getEntityBasesInProperties(current, results);
//            q.addAll(children);
//        }
//
//        return results;
//    }
//
//    private List<Class> entityBaseTypes = Arrays.asList(EntityBase.class, TimeSlotOwner.class);
//
//    private List<EntityBase> getEntityBasesInProperties(EntityBase entity, List<EntityBase> dontReturnTheseOnes)
//    {
//        List<EntityBase> res = new ArrayList<>();
//        List<Method> getters = seq.where(Arrays.asList(entity.getClass().getMethods()), m -> reflectionHelper.isGetter(m));
//        getters = seq.makeItNonNull(getters);
//        getters.forEach(getter ->
//        {
//            Class returnType = getter.getReturnType();
//            Object obj = null;
//            obj = reflectionHelper.callMethod(entity, getter);
//            if(obj == null) return;
//            if(seq.any(entityBaseTypes, bt -> bt == getter.getReturnType().getSuperclass()))
//            {
//                EntityBase entBase = (EntityBase) obj;
//                if(seq.firstOrDefault(dontReturnTheseOnes, i -> i.getId() == entBase.getId()) == null)
//                {
//                    res.add(entBase);
//                }
//            }
//            else if(returnType == Set.class)
//            {
//                Set entBaseSet = (Set) obj;
//                entBaseSet.forEach(element ->
//                {
//                    if(!(element instanceof EntityBase)) return;
//                    EntityBase entityBase = (EntityBase) element;
//                    if(seq.firstOrDefault(dontReturnTheseOnes, el -> el.getId() == entityBase.getId()) == null) res.add(entityBase);
//                });
//            }
//        });
//        return res;
//    }
//
//    @Override
//    public <T extends EntityBase> T get(Class<T> entityClass, int ID, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
//    {
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.<T>getRepository(entityClass);
//        T entity = repository.get(ID, withAllNavigationalProperties, extrasLoaderAction);
//        w.complete();
//        setInRepository(Collections.singletonList(entity));
//        return entity;
//    }
//
//    @Override
//    public <T extends EntityBase> List<T> get(Class<T> entityClass, Predicate<T> predicate, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
//    {
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.<T>getRepository(entityClass);
//        List<T> results = repository.get(predicate, withAllNavigationalProperties, extrasLoaderAction);
//        w.complete();
//        setInRepository(results);
//        return results;
//    }
//
//    private <T extends EntityBase> void setInRepository(List<T> results)
//    {
//        Optional<List<T>> res = Optional.ofNullable(results);
//        res.orElse(new ArrayList<>()).forEach(entity -> flattenGraph(entity).forEach(entityBase -> entityBase.setInRepository(true)));
//    }
//
//    @Override
//    public <T extends EntityBase> List<T> get(Class<T> entityClass, Consumer<CriteriaBuilder<T>> criteriaBuilderConsumer, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
//    {
//        Consumer<net.raphjava.raphtility.interfaces.CriteriaBuilder<T>> criteriaBuilderConsumerWrapper = cb -> criteriaBuilderConsumer.accept((CriteriaBuilder<T>) cb);
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.getRepository(entityClass);
//        List<T> results = repository.get(criteriaBuilderConsumerWrapper, withAllNavigationalProperties, extrasLoaderAction);
//        w.complete();
//        setInRepository(results);
//        return results;
//    }
//
//    @Override
//    public <T extends EntityBase> List<Tuple> getAndSelect(Class<T> entityClass, List<String> propertyNames, Consumer<CriteriaBuilder> criteriaBuilderConsumer)
//    {
//        Consumer<net.raphjava.raphtility.interfaces.CriteriaBuilder<T>> criteriaBuilderConsumerWrapper = cb -> criteriaBuilderConsumer.accept((CriteriaBuilder<T>) cb);
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.getRepository(entityClass);
//        List<Tuple> results = repository.getAndSelect(propertyNames, criteriaBuilderConsumerWrapper);
//        w.complete();
//        return results;
//    }
//
//    @Override
//    public <T extends EntityBase> List<T> getAll(Class<T> entityClass, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
//    {
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.getRepository(entityClass);
//        List<T> results = repository.getAll(withAllNavigationalProperties, extrasLoaderAction);
//        w.complete();
//        setInRepository(results);
//        return results;
//    }
//
//    @Override
//    public <T> void edit(Class<T> entityClass, T entity)
//    {
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.getRepository(entityClass);
//        repository.edit(entity);
//        w.complete();
//    }
//
//    @Override
//    public <T> void editRange(Class<T> entityClass, List<T> entities)
//    {
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.<T>getRepository(entityClass);
//        entities.forEach(repository::edit);
//        w.complete();
//    }
//
//    @Override
//    public <T> void remove(Class<T> entityClass, T entity)
//    {
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.<T>getRepository(entityClass);
//        repository.remove(entity);
//        w.complete();
//    }
//
//    @Override
//    public <T> void removeRange(Class<T> entityClass, List<T> entities)
//    {
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        Repository<T, EagerLoader<T>> repository = w.<T>getRepository(entityClass);
//        entities.forEach(repository::remove);
//        w.complete();
//    }
//
//    @Override
//    public <T> void crud(Consumer<CRUD> creationUpdatingDeletionAction)
//    {
//        UnitOfWork<EagerLoader<T>> w = unitOfWorkFactory.create();
//        w.cud(creationUpdatingDeletionAction);
//        w.complete();
//    }
//
//
//}

package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import net.raphjava.raphtility.collectionmanipulation.interfaces.Itemizer;
//import net.raphjava.studeeconsole.components.interfaces.Creation;
//import com.raphjava.softplanner.data.models.EntityBase;
//import com.raphjava.softplanner.data.models.fromTustantConsole.TimeSlotOwner;
//
//import java.lang.reflect.Method;
//import java.util.*;
//import java.util.function.BiConsumer;
//import java.util.function.Function;
//import java.util.function.Supplier;
//import java.util.stream.Collectors;
//
//public class PersistenceCreation<T extends EntityBase> implements Creation<T>
//{
//
//    protected final T entity;
//    protected final Supplier<Session> sessionSupplier;
//    protected final Map<Integer, EntityBase> creationData = new HashMap<>();
//    protected final Set<EntityBase> persistenceEntities = new HashSet<>();
//    protected final Itemizer look;
//    protected final ReflectionHelper reflectionHelper;
//    private final BiConsumer<EntityBase, Creation> registrationAction;
//    private boolean isRegistered;
//
//    public PersistenceCreation(T entity, TCRepositoryBaseArgs<T> args, BiConsumer<EntityBase, Creation> entityCreatorRegistrationAction)
//    {
//        this.entity = entity;
//        this.sessionSupplier = args.getSessionSupplier();
//        look = args.getSequenceManipulator();
//        reflectionHelper = args.getReflectionHelper();
//        registrationAction = entityCreatorRegistrationAction;
//    }
//
//
//
//
//    List<Class> entityBaseTypes = new ArrayList<>(Arrays.asList(EntityBase.class, TimeSlotOwner.class));
//
//    protected <R extends EntityBase> void nullifyAllRelationships(R entity)
//    {
//        List<Method> methods = Arrays.asList(entity.getClass().getMethods());
//        List<Method> getterOfPropsWithValues = new ArrayList<>();
//
//        methods.forEach(method ->
//        {
//            if(!reflectionHelper.isGetter(method)) return;
//            Object value = reflectionHelper.callMethod(entity, method);
//            if(value == null) return;
//            if(asExp(entityBaseTypes).any(c -> c == value.getClass().getSuperclass()))
//            {
//                getterOfPropsWithValues.add(method);
//            }
//            if(value instanceof Set)
//            {
//                getterOfPropsWithValues.add(method);
//
//            }
//        });
//
//        if(getterOfPropsWithValues.size() < 1) return;
//        List<AbstractMap.SimpleEntry<Method, Method>> gAndS = getGettersAndSetters(entity);
//        getterOfPropsWithValues.forEach(getter ->
//        {
//            AbstractMap.SimpleEntry<Method, Method> gs = asExp(gAndS).firstOrDefault(x ->
//                    x.getKey().getName().equalsIgnoreCase(getter.getName()) &&
//                    x.getKey().getReturnType() == getter.getReturnType());
//            if(gs == null) return;
//            reflectionHelper.callMethod(entity, gs.getValue(), new Object[] { null });
//        });
//
//    }
//
//    protected List<AbstractMap.SimpleEntry<Method, Method>> getGettersAndSetters(EntityBase entity)
//    {
//
//        List<Method> methods = Arrays.stream(entity.getClass().getMethods()).collect(Collectors.toList());
//        List<Method> getters = asExp(methods).where(reflectionHelper::isGetter).list();
//        List<Method> setters = asExp(methods).where(reflectionHelper::isSetter).list();
//        List<AbstractMap.SimpleEntry<Method,Method>> getterSetters = new ArrayList<>();
//        getters.forEach(getter -> getterSetters.add(getSetter(getter, setters)));
//        return getterSetters;
//    }
//
//    protected AbstractMap.SimpleEntry<Method, Method> getSetter(Method getter, List<Method> setters)
//    {
//        List<Method> fsetters = setters.stream().filter(s -> (s.getName().replace("set", "get")).equals(getter.getName())).collect(Collectors.toList());
//        if(fsetters.size() != 0) return new AbstractMap.SimpleEntry<>(getter, fsetters.get(0));
//        fsetters = setters.stream().filter(s -> s.getName().replace("set", "is").equals(getter.getName())).collect(Collectors.toList());
//        if(fsetters.size() != 0) return new AbstractMap.SimpleEntry<>(getter, fsetters.get(0));
//        return new AbstractMap.SimpleEntry<>(getter, null);
//    }
//
//    protected <S extends EntityBase> S getWorkingCopy(S entity)
//    {
//        S pCopy = (S) creationData.get(entity.getId());
//        if(pCopy == null)
//        {
//            pCopy = createShallowCopy(entity);
//            updateCreationData(entity, pCopy);
//            nullifyAllRelationships(pCopy);
//            initializeSets(pCopy);
//        }
//        return pCopy;
//    }
//
//    private <S extends EntityBase> void initializeSets(S pCopy)
//    {
//        getGettersAndSetters(pCopy).forEach(gs ->
//        {
//            if(gs.getKey().getReturnType() == Set.class)
//            {
//                reflectionHelper.callMethod(pCopy, gs.getValue(), new HashSet<>());
//            }
//        });
//    }
//
//    protected <S extends EntityBase> void updateCreationData(S entity, S copy)
//    {
//        creationData.put(entity.getId(), copy);
//
//    }
//
//    protected <S extends  EntityBase> S createShallowCopy(S entity)
//    {
//        try
//        {
//            return (S) entity.clone();
//        }
//        catch (CloneNotSupportedException e)
//        {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public void add()
//    {
//        ensureRegistrationOfThisEntityCreator();
//        T copy = getWorkingCopy(entity);
//        //sessionSupplier.get().persist(copy);
//        persist(Collections.singletonList(copy));
//    }
//
//    private void ensureRegistrationOfThisEntityCreator()
//    {
//        if(!isRegistered())
//        {
//            registrationAction.accept(entity, this);
//            setRegistered(true);
//        }
//    }
//
//    private void setRegistered(boolean isRegistered)
//    {
//        this.isRegistered = isRegistered;
//    }
//
//    private boolean isRegistered()
//    {
//        return isRegistered;
//    }
//
//    @Override
//    public <R> void addOneToOne(R entityOne, BiConsumer<T,R> entitySetter, BiConsumer<R,T> entityOneSetter)
//    {
//        Objects.requireNonNull(ensureEntityBase(entityOne));
//        //addOneToOne(this.entity, entityOne, entitySetter, entityOneSetter);
//        T eCopy1 = (T) getWorkingCopy(entity);
//        //EntityBase e2 = (EntityBase) entityOne;
//        //R eCopy2 = (R) getWorkingCopy(e2);
//        ensureRegistrationOfThisEntityCreator();
//        EntityBase ent = (EntityBase) entityOne;
//        R copy = (R) getWorkingCopy(ent);
//        //persist(Arrays.asList(entity,(EntityBase) copy));
//        entitySetter.accept(eCopy1, copy);
//        entityOneSetter.accept(copy, eCopy1);
//        persist(Collections.singletonList(eCopy1));
//    }
//
//    protected <R> Boolean ensureEntityBase(R entity)
//    {
//        Class<? extends Object> clas = entity.getClass();
//        Class<? extends Object> sc = entity.getClass().getSuperclass();
//
//        if(!(entity instanceof EntityBase))
//        {
//            throwThisException(new Exception("Entity must be an instance of EntityBase"));
//            return null;
//        }
//        return true;
//    }
//
//    @Override
//    public <R> void addOneToZero(R entityZero, BiConsumer<T, R> entitySetter, BiConsumer<R, T> entityZeroSetter)
//    {
//        //addOneToZero(this.entity, entityZero, entitySetter, entityZeroSetter);
//        ensureRegistrationOfThisEntityCreator();
//        if(entityZero == null)
//        {
//          //  EntityBase e = (EntityBase) entityOne;
//            T copy = getWorkingCopy(entity);
//            persist(Collections.singletonList(copy));
//        }
//        else
//        {
//            Objects.requireNonNull(ensureEntityBase(entityZero));
//            T copy = getWorkingCopy(entity);
//            EntityBase e = (EntityBase) entityZero;
//            R enZero = (R) getWorkingCopy(e);
//            entitySetter.accept(copy, enZero);
//            entityZeroSetter.accept(enZero, copy);
//            persist(Collections.singletonList(copy));
//        }
//    }
//
//    @Override
//    public <R> void addZeroToOne(R entityOne, BiConsumer<T, R> entitySetter, BiConsumer<R, T> entityOneSetter)
//    {
//        //addZeroToOne(this.entity, entityOne, entitySetter, entityOneSetter);
//        ensureRegistrationOfThisEntityCreator();
//        //EntityBase en = (EntityBase) entityZero;
//        T copy = getWorkingCopy(entity);
//        EntityBase e = (EntityBase) entityOne;
//        R enOne = (R) getWorkingCopy(e);
//        entitySetter.accept(copy, enOne);
//        entityOneSetter.accept(enOne, copy);
//        persist(Collections.singletonList(copy));
//    }
//
//    @Override
//    public <R> void addZeroToZero(R entityZero, BiConsumer<T, R> entitySetter, BiConsumer<R, T> entityZeroSetter)
//    {
//        addOneToZero(entityZero, entitySetter, entityZeroSetter);
//    }
//
//    @Override
//    public <R> void addOneToMany(R childEntity, Function<T,Set<R>> setGetter, BiConsumer<R, T> childEntitySetter)
//    {
//        //addOneToMany(this.entity, childEntity, setGetter, childEntitySetter);
//        ensureRegistrationOfThisEntityCreator();
//        T copy = getWorkingCopy(entity);
//        if(childEntity != null)
//        {
//            EntityBase c = (EntityBase) childEntity;
//            R childProxy = (R) getWorkingCopy(c);
//            setGetter.apply(copy).add(childProxy);
//            childEntitySetter.accept(childProxy, copy);
//        }
//        persist(Collections.singletonList(copy));
//    }
//
//    @Override
//    public <R> void addManyToOne(R parentEntity, Function<R, Set<T>> parentSetGetter, BiConsumer<T, R> childSetter)
//    {
//        //addManyToOne(this.entity, parentEntity, parentSetGetter, childSetter);
//        Objects.requireNonNull(ensureEntityBase(parentEntity));
//        ensureRegistrationOfThisEntityCreator();
//        T copy = getWorkingCopy(entity);
//        EntityBase e = (EntityBase) parentEntity;
//        R parent = (R) getWorkingCopy(e);
//        parentSetGetter.apply(parent).add(copy);
//        childSetter.accept(copy, parent);
//        persist(Collections.singletonList(copy));
//    }
//
//    @Override
//    public <R> void addManyToMany(R entityTwo, Function<T, Set<R>> entitySetGetter, Function<R, Set<T>> entity2SetGetter)
//    {
//        //addManyToMany(this.entity, entityTwo, entitySetGetter, entity2SetGetter);
//        ensureRegistrationOfThisEntityCreator();
//        T copy = getWorkingCopy(entity);
//        if(entityTwo != null)
//        {
//            entity2SetGetter.apply(entityTwo).add(copy);
//            EntityBase e = (EntityBase) entityTwo;
//            R eCopy = (R) getWorkingCopy(e);
//            entitySetGetter.apply(copy).add(eCopy);
//        }
//        persist(Collections.singletonList(copy));
//    }
//
//    @Override
//    public <S, R> void addOneToOneToOther(S entity, R entityOne, BiConsumer<S, R> entitySetter, BiConsumer<R, S> entityOneSetter)
//    {
//        Objects.requireNonNull(ensureEntityBase(entity));
//        Objects.requireNonNull(ensureEntityBase(entityOne));
//        Objects.requireNonNull(ensureNoneIsTheMainEntity(Arrays.asList((EntityBase) entity, (EntityBase)entityOne)));
//        EntityBase e1 = (EntityBase) entity;
//        S copy1 = (S) getWorkingCopy(e1);
//        EntityBase e2 = (EntityBase) entityOne;
//        R copy2 = (R) getWorkingCopy(e2);
//        entitySetter.accept(copy1, copy2);
//        entityOneSetter.accept(copy2, copy1);
//    }
//
//    private Object ensureNoneIsTheMainEntity(List<EntityBase> list)
//    {
//        LambdaSettable<Boolean> result = new LambdaSettable<>(false);
//        if(list == null) return  null;
//        if(list.size() > 0) return null;
//        list.forEach(en ->
//        {
//            if(en.equals(this.entity))
//            {
//                throwThisException(new Exception("Wrong argument. You can't manipulate relationships of the main entity with this method."));
//                if(!result.getText()) result.setItem(true);
//            }
//        });
//        if(result.getText()) return null;
//        return new Object();
//
//    }
//
//    @Override
//    public <S, R> void addOneToZeroToOther(S entityOne, R entityZero, BiConsumer<S, R> entityOneSetter, BiConsumer<R, S> entityZeroSetter)
//    {
//        ensureOtherEntityBase(Arrays.asList( entityOne, entityZero));
//        if(entityZero == null) return;
//        EntityBase e = (EntityBase) entityOne;
//        EntityBase ee = (EntityBase) entityZero;
//        S copy1 = (S) getWorkingCopy(e);
//        R copy2 = (R) getWorkingCopy(ee);
//        entityOneSetter.accept(copy1, copy2);
//        entityZeroSetter.accept(copy2, copy1);
//    }
//
//    private void ensureOtherEntityBase(List<Object> list)
//    {
//        Objects.requireNonNull(list);
//        Object obj = new Object();
//        LambdaSettable<Boolean> inValid = new LambdaSettable<>(false);
//        if(list.size() > 0)
//        {
//            list.forEach(en ->
//            {
//                if(en == null) inValid.setItem(true);
//                if(ensureEntityBase(en) == null) inValid.setItem(true);
//                if(en == entity) inValid.setItem(true);
//            });
//            if(inValid.getText()) obj = null;
//        }
//        Objects.requireNonNull(obj);
//    }
//
//    @Override
//    public <S, R> void addZeroToOneToOther(S entityZero, R entityOne, BiConsumer<S, R> entityZeroSetter, BiConsumer<R, S> entityOneSetter)
//    {
//        ensureOtherEntityBase(Arrays.asList( entityZero, entityOne));
//        EntityBase e = (EntityBase) entityZero;
//        EntityBase ee = (EntityBase) entityOne;
//        S copy1 = (S) getWorkingCopy(e);
//        R copy2 = (R) getWorkingCopy(ee);
//        entityZeroSetter.accept(copy1, copy2);
//        entityOneSetter.accept(copy2, copy1);
//    }
//
//    @Override
//    public <S, R> void addZeroToZeroToOther(S entity1, R entity2, BiConsumer<S, R> entity1Setter, BiConsumer<R, S> entity2Setter)
//    {
//        ensureOtherEntityBase(Arrays.asList(entity1, entity2));
//        EntityBase e = (EntityBase) entity1;
//        EntityBase ee = (EntityBase) entity2;
//        S copy1 = (S) getWorkingCopy(e);
//        R copy2 = (R) getWorkingCopy(ee);
//        entity1Setter.accept(copy1, copy2);
//        entity2Setter.accept(copy2, copy1);
//    }
//
//    @Override
//    public <S, R> void addOneToManyToOther(S parentEntity, R childEntity, Function<S, Set<R>> parentSetGetter, BiConsumer<R, S> childEntitySetter)
//    {
//        ensureOtherEntityBase(Arrays.asList(parentEntity, childEntity));
//        EntityBase p = (EntityBase) parentEntity;
//        EntityBase c = (EntityBase) childEntity;
//        S copy1 = (S) getWorkingCopy(p);
//        R copy2 = (R) getWorkingCopy(c);
//        parentSetGetter.apply(copy1).add(copy2);
//        childEntitySetter.accept(copy2, copy1);
//    }
//
//    @Override
//    public <S, R> void addManyToOneToOther(S childEntity, R parentEntity, Function<R, Set<S>> parentSetGetter, BiConsumer<S, R> childEntitySetter)
//    {
//        ensureOtherEntityBase(Arrays.asList(childEntity, parentEntity));
//        EntityBase e = (EntityBase) childEntity;
//        EntityBase ee = (EntityBase) parentEntity;
//        S c = (S) getWorkingCopy(e);
//        R p = (R) getWorkingCopy(ee);
//        parentSetGetter.apply(p).add(c);
//        childEntitySetter.accept(c, p);
//    }
//
//    @Override
//    public <S, R> void addManyToManyToOther(S entityOne, R entityTwo, Function<S, Set<R>> entity1SetGetter, Function<R, Set<S>> entity2SetGetter)
//    {
//        ensureOtherEntityBase(Arrays.asList(entityOne, entityTwo));
//        EntityBase e = (EntityBase) entityOne;
//        EntityBase ee = (EntityBase) entityTwo;
//        S e1 = (S) getWorkingCopy(e);
//        R e2 = (R) getWorkingCopy(ee);
//        entity1SetGetter.apply(e1).add(e2);
//        entity2SetGetter.apply(e2).add(e1);
//    }
//
//
//    protected void persist(List<EntityBase> entities)
//    {
//        persistenceEntities.addAll(entities);
//    }
//
//    protected void throwThisException(Exception e)
//    {
//        try
//        {
//            throw e;
//        }
//        catch (Exception x)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public List<EntityBase> getPersistenceEntities()
//    {
//        return new ArrayList<>(persistenceEntities);
//    }
//}

package com.raphjava.softplanner.services;

import com.raphjava.softplanner.annotations.Basic;
import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.data.QumbuqaComponent;
import com.raphjava.softplanner.data.interfaces.CRUD;
import com.raphjava.softplanner.data.interfaces.DataService;
import com.raphjava.softplanner.data.interfaces.EagerLoader;
import net.raphjava.qumbuqa.core.BatchDataContext;
import net.raphjava.qumbuqa.core.DataContext;
import net.raphjava.qumbuqa.core.Qumbuqa;
import net.raphjava.qumbuqa.read.ReaderParameterBuilder;
import net.raphjava.qumbuqa.read.interfaces.FluentCriteriaBuilder;
import net.raphjava.qumbuqa.write.interfaces.BatchWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

//import net.raphjava.studeeconsole.components.interfaces.CRUD;
//import net.raphjava.studeeconsole.components.interfaces.EagerLoader;
//import net.raphjava.studeeconsole.interfaces.DataService;

@Basic
@Lazy
public class QuDataService implements DataService
{

    private Qumbuqa qumbuqa;

    public QuDataService(Qumbuqa qumbuqa)
    {
        this.qumbuqa = qumbuqa;
    }

    private QuDataService(Builder builder)
    {
        qumbuqa = builder.qumbuqa;
    }


    public static Builder newBuilder()
    {
        return new Builder();
    }

    /*@Override
    public <T > void add(T entity)
    {
        run(throwable(ctx ->
        {
            ctx.add(entity);
            ctx.commit();
        }));
    }*/

    private void run(Consumer<DataContext> persistenceAction)
    {
        try (DataContext ctx = qumbuqa.getDataContext())
        {
            persistenceAction.accept(ctx);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*@Override
    public <T > void add(T entity, Consumer<EagerLoader<T>> cascadeActions)
    {
        run(throwable());

    }*/

    /*private <T> Consumer<Writer.Cascader> getCascader(Consumer<EagerLoader<T>> extrasLoaderAction)
    {
        //This method creates a Consumer<EagerLoader> to Consumer<Cascader> adapter.

        return new Consumer<>() *//*When cascader is passed into this:
        1. The cascader is saved to a field.
        2. The eagerLoader is passed into the eagerloader action.
        3. Every time the include method is called, the cascader.include method is also called. *//*
        {
            private Writer.Cascader cascader;
            private Consumer<EagerLoader<T>> eagerLoaderConsumer = extrasLoaderAction;

            private EagerLoader<T> eagerLoader = new EagerLoader<>()
            {
                @Override
                public void include(String property)
                {
                    cascader.cascade(property);
                }
            };

            @Override
            public void accept(Writer.Cascader cascader)
            {
                this.cascader = cascader;
                this.eagerLoaderConsumer.accept(eagerLoader);
            }
        };
    }

    @Override
    public <T> void addRange(List<T> entities)
    {
        run(throwable(ctx ->
        {
            for(var en : entities) ctx.add(en);
            ctx.commit();

        }));

    }
*/
    /*@SuppressWarnings("unchecked")
    @Override
    public <T > T addAndReturn(T entity)
    {
        var rex = new LambdaSettable<T>(null);
        run(throwable(ctx ->
        {
            ctx.add(entity);
            ctx.commit();
            rex.setItem((T) ctx.get(entity.getClass(), entity.getId()));

        }));
        return rex.getItem();
    }
*/
  /*  @SuppressWarnings("unchecked")
    @Override
    public <T > List<T> addAndReturnRange(List<T> entities)
    {
        var rex = new ArrayList<T>();
        run(throwable(ctx ->
        {
            for (var entity : entities) ctx.add(entity);
            ctx.commit();
            for (var entity : entities) rex.add((T) ctx.get(entity.getClass(), entity.getId()));
        }));

        return rex;
    }
*/
    /*@Override
    public <T> T get(Class<T> entityClass, int ID, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
    {
        var rex = new ArrayList<T>();
        run(throwable(ctx ->
        {
            var pb = ctx.get(entityClass, c -> c.equation().path("id").constant(ID));
            if (withAllNavigationalProperties) pb = pb.withRelatives();
            rex.addAll(pb.eagerLoader(extrasLoaderAction == null ? null : getEagerLoader(extrasLoaderAction)).list());
        }));

        return rex.isEmpty() ? null : rex.get(0);
    }

    @Override
    public <T> T get(Class<T> entityClass, int ID)
    {
        var rex = new LambdaSettable<T>(null);
        run(throwable(ctx -> rex.setItem(ctx.get(entityClass, ID))));

        return rex.getItem();
    }
*/
    private <T> Consumer<net.raphjava.qumbuqa.read.interfaces.EagerLoader<T>> getEagerLoader(Consumer<EagerLoader<T>> extrasLoaderAction)
    {
        //This method creates a Consumer<studee EagerLoader> to Consumer<qumbuqa EagerLoader> adapter.

        return new Consumer<net.raphjava.qumbuqa.read.interfaces.EagerLoader<T>>() /*When qumbuqa's EagerLoader is passed into this:
        1. The qEagerLoader is saved to a field.
        2. The studeeEagerLoader is passed into the eagerloader action.
        3. Every time the include method is called, the qEagerLoader.include method is also called. */
        {
            private net.raphjava.qumbuqa.read.interfaces.EagerLoader<T> qEagerLoader;
            private Consumer<EagerLoader<T>> eagerLoaderConsumer = extrasLoaderAction;

            private EagerLoader<T> studeeEagerLoader = new EagerLoader<T>()
            {
                @Override
                public EagerLoader include(String property)
                {
                    qEagerLoader.include(property);
                    return this;
                }
            };

            @Override
            public void accept(net.raphjava.qumbuqa.read.interfaces.EagerLoader<T> eagerLoader)
            {
                this.qEagerLoader = eagerLoader;
                this.eagerLoaderConsumer.accept(studeeEagerLoader);
            }
        };
    }

//    @Override
//    public <T > List<T> get(Class<T> entityClass, Predicate<T> predicate, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
//    {
//        var rex = new ArrayList<T>();
//        run(throwable(ctx ->
//        {
//            var pb = ctx.get(entityClass, c -> c.equation().path("id").constant());
//            if(withAllNavigationalProperties) pb = pb.withRelatives();
//            rex.addAll(pb.eagerLoader(getEagerLoader(extrasLoaderAction)).list());
//        }));
//
//        return rex.isEmpty() ? null : rex.get(0);
//    }

//    @Override
//    public <T > List<T> get(Class<T> entityClass, Consumer<CriteriaBuilder<T>> criteriaBuilderConsumer, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
//    {
//        var cb = new TCCriteriaBuilder<T>()
//    }

//    @Override
//    public <T > List<Tuple> getAndSelect(Class<T> entityClass, List<String> propertyNames, Consumer<CriteriaBuilder> criteriaBuilderConsumer)
//    {
//        var rex = new ArrayList<T>();
//        run(throwable(ctx ->
//        {
//            var pb = ctx.get(entityClass, c -> c.equation().path("id").constant(ID));
//            rex.addAll(pb.eagerLoader(getEagerLoader(extrasLoaderAction)).list());
//        }));
//
//        return rex.isEmpty() ? null : rex.get(0);
//    }

//    @Override
//    public <T> List<T> getAll(Class<T> entityClass, boolean withAllNavigationalProperties, Consumer<EagerLoader<T>> extrasLoaderAction)
//    {
//        var rex = new ArrayList<T>();
//        run(throwable());
//
//        return rex;
//    }

    /*@Override
    public <T > void edit(T entity, Consumer<EagerLoader<T>> cascaderAction)
    {
        run(throwable());

    }*/

    /* @Override
     public <T > void edit(T entity)
     {
         run(throwable());
     }
 */
   /* @Override
    public <T > void editRange(List<T> entities)
    {
        run(throwable(ctx ->
        {
            for (var entity : entities)
            {
                ctx.update(entity);
            }
            ctx.commit();

        }));
    }

    @Override
    public <T > void remove(T entity)
    {
        run(throwable(ctx ->
        {
            ctx.remove(entity);
            ctx.commit();

        }));
    }

    @Override
    public <T > void removeRange(List<T> entities)
    {
        run(throwable(ctx ->
        {
            for (var entity : entities)
            {
                ctx.remove(entity);
            }
            ctx.commit();

        }));
    }

    private void run(Consumer<DataContext> pAction, DataContext context)
    {
        pAction.accept(context);
    }

*/
    /*@Override
    public <T> void crud(Consumer<CRUD> creationUpdatingDeletionAction)
    {
        run(throwable(ctx -> creationUpdatingDeletionAction.accept(new CRUD()
                {
                    @Override
                    public <T > void add(T entity)
                    {
                        run(throwable(c -> c.add(entity)), ctx);
                    }

                    @Override
                    public <T > void add(T entity, Consumer<EagerLoader<T>> extrasLoaderAction)
                    {
                        run(throwable(c -> c.add(entity, getCascader(extrasLoaderAction))), ctx);

                    }

                    @Override
                    public <T > void edit(T entity)
                    {
                        run(throwable(c -> c.update(entity)), ctx);

                    }

                    @Override
                    public <T > void edit(T entity, Consumer<EagerLoader<T>> cascadeAction)
                    {
                        run(throwable(c -> c.update(entity, getCascader(cascadeAction))), ctx);

                    }

                    @Override
                    public <T > void remove(T entity)
                    {
                        run(throwable(c -> c.remove(entity)), ctx);
                    }

                    @Override
                    public void commit()
                    {
                        run(throwable(DataContext::commit), ctx);
                    }

                })));



    }*/
    private <T> Consumer<BatchWriter.EntityVisitor> getEntityVisitor(Consumer<EagerLoader<T>> extrasLoaderAction)
    {

        /*This method creates a Consumer<studee EagerLoader> to Consumer<EntityVisitor> adapter. It's a Consumer<EntityVisitor>
         * that internally calls a Consumer<studee EagerLoader> include method every time its own include method is called.*/

        return new Consumer<BatchWriter.EntityVisitor>()
        {
            /*
                It stores the Consumer<studee ExtrasLoader> in a field as domainAction.
                When qumbuqa's EntityVisitor is passed into this:
                1. The entityVisitor is saved to a field.
                2. The extrasLoader is passed into the extrasLoader action (domainAction).
                3. Every time the extrasLoader::include method is called, the visitor::include method is also called.

            */

            private Consumer<EagerLoader<T>> domainAction = extrasLoaderAction;

            private BatchWriter.EntityVisitor visitor;

            private EagerLoader<T> eagerLoader = new EagerLoader<T>()
            {
                @Override
                public EagerLoader<T> include(String property)
                {
                    visitor.include(property);
                    return this;
                }
            };

            @Override
            public void accept(BatchWriter.EntityVisitor entityVisitor)
            {
                visitor = entityVisitor;
                this.domainAction.accept(eagerLoader);

            }
        };
    }

    private class CRUDImp implements CRUD
    {
        Collection<Consumer<BatchDataContext>> actions = new LinkedList<Consumer<BatchDataContext>>();
        private Runnable onFailureCallback;

        @Override
        public <T> CRUD add(T entity)
        {
            actions.add(ctx ->
            {
                try
                {
                    ctx.add(entity);
                }
                catch (SQLIntegrityConstraintViolationException e)
                {
                    e.printStackTrace();
                    failed();
                }
            });
            return this;
        }


        @Override
        public <T> CRUD add(T entity, Consumer<EagerLoader<T>> extrasLoaderAction)
        {
            actions.add(ctx -> ctx.add(entity, getEntityVisitor(extrasLoaderAction)));
            return this;
        }

        @Override
        public <T> CRUD update(T entity)
        {
            actions.add(ctx -> ctx.update(entity));
            return this;
        }

        @Override
        public <T> CRUD update(T entity, Consumer<EagerLoader<T>> cascadeAction)
        {
            actions.add(ctx -> ctx.update(entity, getEntityVisitor(cascadeAction)));
            return this;
        }

        @Override
        public <T> CRUD updateSelect(T entity, Consumer<EagerLoader<T>> cascadeAction)
        {
            actions.add(ctx -> ctx.updateSelect(entity, getEntityVisitor(cascadeAction)));
            return this;
        }

        @Override
        public <T> CRUD remove(T entity)
        {
            actions.add(ctx ->
            {
                try
                {
                    ctx.remove(entity);
//                    ctx.commit();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    failed();
                }

            });

            return this;
        }

        @Override
        public <T> CRUD deRelate(T entity, Consumer<EagerLoader<T>> cascadeAction)
        {
            actions.add(ctx ->
            {
                try
                {
                    ctx.deRelate(entity, getEntityVisitor(cascadeAction));
//                    ctx.commit();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    failed();
                }

            });

            return this;
        }

        @Override
        public CRUD flush()
        {
            actions.add(ctx ->
            {
                try
                {
                    ctx.flush();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    failed();
                }

            });

            return this;
        }

        @Override
        public CRUD commit()
        {
            actions.add(ctx ->
            {
                try
                {
                    ctx.commit();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    failed();
                }

            });

            return this;
        }

        @Override
        public CRUD onSuccess(Runnable onSuccessAction)
        {
            actions.add(ctx -> onSuccessAction.run());
            return this;
        }

        @Override
        public CRUD onFailure(Runnable onFailureAction)
        {
            onFailureCallback = onFailureAction;
//            actions.add(ctx -> onFailureAction.run());
            return this;
        }

        private void failed()
        {
            onFailureCallback.run();
        }

        public void write(BatchDataContext dataContext)
        {
            actions.forEach(a -> a.accept(dataContext));
        }
    }

    private BatchDataContext getNewDataContext(CRUDImp crud)
    {
        try
        {
            return qumbuqa.getBatchDataContext();
        }
        catch (SQLException | ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e)
        {
            e.printStackTrace();
            crud.failed();
            return null;
        }
    }

    private BatchDataContext getNewDataContext(ReadActionImp reader)
    {
        try
        {
            return qumbuqa.getBatchDataContext();
        }
        catch (SQLException | ClassNotFoundException | NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e)
        {
            e.printStackTrace();
            reader.failed();
            return null;
        }
    }

    @Override
    public void write(Consumer<CRUD> creationUpdatingDeletionAction)
    {
        CRUDImp crud = new CRUDImp();
        creationUpdatingDeletionAction.accept(crud);
        try (BatchDataContext ctx = getNewDataContext(crud))
        {
            crud.write(ctx);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            crud.failed();
        }

    }

    private class ReadActionImp implements ReadAction
    {
        private Consumer<BatchDataContext> readCallback;
        private Runnable failedCallback;

        @Override
        public <T> EntityReadAction<T> get(Class<T> entityClass, int ID)
        {
            EntityReadActionImp<T> r = new EntityReadActionImp<T>();
            r.get(entityClass, ID);
            readCallback = r::read;
            failedCallback = r::failed;
            return r;
        }

        @Override
        public <T> CollectionReadAction<T> get(Class<T> entityClass, Consumer<FluentCriteriaBuilder<T>> criteriaBuilderAction)
        {
            CollectionReadActionImp<T> cra = new CollectionReadActionImp<T>();
            cra.get(entityClass, criteriaBuilderAction);
            readCallback = cra::read;
            failedCallback = cra::failed;
            return cra;
        }


        @Override
        public <T> CollectionReadAction<T> getAll(Class<T> entityClass)
        {
            CollectionReadActionImp<T> cra = new CollectionReadActionImp<T>();
            cra.getAll(entityClass);
            readCallback = cra::read;
            failedCallback = cra::failed;
            return cra;
        }

        public void read(BatchDataContext ctx)
        {
            readCallback.accept(ctx);
        }

        public void failed()
        {
            failedCallback.run();
        }
    }

    @Override
    public void read(Consumer<ReadAction> readAction)
    {
        ReadActionImp reader = new ReadActionImp();
        readAction.accept(reader);
        try (BatchDataContext ctx = getNewDataContext(reader))
        {
            reader.read(ctx);
        }
        catch (Exception e)
        {
            reader.failed();
            e.printStackTrace();
        }

    }

    private class EntityReadActionImp<T> implements EntityReadAction<T>
    {

        private Collection<Consumer<BatchDataContext>> actions = new LinkedList<Consumer<BatchDataContext>>();

        private Runnable onFailureAction;
        private T result;
        private ReaderParameterBuilder<T, T> pb;
        private Consumer<T> onSuccessCallback;


        @Override
        public Output<T> onSuccess(Consumer<T> outputAction)
        {
            onSuccessCallback = outputAction;
            return this;
        }

        @Override
        public Output<T> onFailure(Runnable onFailureAction)
        {
            this.onFailureAction = onFailureAction;
            return this;
        }

        private void get(Class<T> entityClass, int ID)
        {
            actions.add(ctx -> pb = ctx.get(entityClass, c -> c.equation().path("id").constant(ID)));
        }


        @Override
        public EntityReadAction<T> withRelationships()
        {
            actions.add(ctx -> pb = pb.withRelatives());
            return this;
        }

        @Override
        public EntityReadAction<T> eagerLoad(Consumer<EagerLoader<T>> extrasLoaderAction)
        {
            actions.add(ctx -> pb = pb.eagerLoader(extrasLoaderAction == null ? null : getEagerLoader(extrasLoaderAction)));
            return this;
        }

        public void failed()
        {
            onFailureAction.run();
        }


        private void read(BatchDataContext ctx)
        {
            actions.forEach(a -> a.accept(ctx));
            List<T> l = pb.list();
            result = l.isEmpty() ? null : l.get(0);
            onSuccessCallback.accept(result);
        }

    }

    private class CollectionReadActionImp<T> implements CollectionReadAction<T>
    {

        private Collection<Consumer<BatchDataContext>> actions = new LinkedList<Consumer<BatchDataContext>>();

        private Runnable onFailureAction;
        private Collection<T> result;
        private ReaderParameterBuilder<T, T> pb;
        private Consumer<Collection<T>> onSuccessCallback;


        private void getAll(Class<T> entityClass)
        {
            actions.add(ctx -> pb = ctx.getAll(entityClass));
        }


        @Override
        public Output<Collection<T>> onSuccess(Consumer<Collection<T>> outputAction)
        {
            onSuccessCallback = outputAction;
            return this;
        }

        @Override
        public Output<Collection<T>> onFailure(Runnable onFailureAction)
        {
            this.onFailureAction = onFailureAction;
            return this;
        }

        @Override
        public CollectionReadAction<T> withRelationships()
        {
            actions.add(ctx -> pb = pb.withRelatives());
            return this;
        }

        @Override
        public CollectionReadAction<T> eagerLoad(Consumer<EagerLoader<T>> extrasLoaderAction)
        {
            actions.add(ctx -> pb = pb.eagerLoader(extrasLoaderAction == null ? null : getEagerLoader(extrasLoaderAction)));
            return this;
        }

        @Override
        public CollectionReadAction<T> get(Class<T> entityClass, Consumer<FluentCriteriaBuilder<T>> criteriaBuilderAction)
        {
            actions.add(ctx -> pb = ctx.get(entityClass, criteriaBuilderAction));
            return this;
        }

        public void read(BatchDataContext ctx)
        {
            actions.forEach(a -> a.accept(ctx));
            result = pb.list();
            onSuccessCallback.accept(result);
        }

        public void failed()
        {
            onFailureAction.run();
        }

    }

    @Lazy
    @Component
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<QuDataService>
    {


        private Qumbuqa qumbuqa;

        private QumbuqaComponent qumbuqaComponent;

        private Builder()
        {
            super(QuDataService.class);
        }

        @Autowired
        public void setQumbuqaComponent(QumbuqaComponent qumbuqaComponent)
        {
            this.qumbuqaComponent = qumbuqaComponent;
        }

        public Builder setQumbuqa(Qumbuqa qumbuqa)
        {
            this.qumbuqa = qumbuqa;
            return this;
        }

        public QuDataService build()
        {
            qumbuqa = qumbuqaComponent.getQumbuqa();
            return new QuDataService(this);

        }

        @Override
        public boolean isSingleton()
        {
            return true;
        }
    }
}

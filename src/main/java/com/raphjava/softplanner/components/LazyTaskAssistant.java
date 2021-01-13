package com.raphjava.softplanner.components;

import net.raphjava.raphtility.asynchrony.Task;
import net.raphjava.raphtility.asynchrony.TaskResult;
import net.raphjava.raphtility.logging.interfaces.Log;

import java.util.function.Consumer;

/**
 * Represents an async action that can be lazily executed. This is achieved by breaking an async
 * action into two parts:
 * <li>Essential action part - the part of the task that must be executed every time this task is started. It can be null;</li>
 * <li>Lazy execution part - the part of the task that is lazily executed</li>
 * To enable lazy execution, setLazy to true before starting the asyncTask.
 * To enable the lazy part to be executed, setLazy to false before starting the asyncTask again.
 * Please note that:
 * <li>The asyncTask method returns a new Task object every time;</li>
 * <li>An instance of AsyncActionAssistant keeps a reference of the first Task object (The one created in the first asyncTask method call);</li>
 * <li>Subsequent asyncTasks created through the calling of the asyncTask method always execute after the first task
 *  finishes executing.</li>
 */
public class LazyTaskAssistant
{

    private boolean lazy;
    private Runnable mainAction;

    private LazyTaskAssistant(Builder builder)
    {
        setLazy(builder.setLazy);
        mainAction = builder.lazyAction;
        setEssentialActions(builder.essentialActions);
        unitDataLoaded = builder.unitDataLoaded;
        logger = builder.logger;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public synchronized boolean isLazy()
    {
        if(lazy) System.out.println("Lazy part of this task won't execute.");
        return lazy;
    }

    public synchronized void setLazy(boolean setLazy)
    {
        this.lazy = setLazy;
    }
    private Runnable essentialActions;
    private boolean unitDataLoaded;

    public void setEssentialActions(Runnable essentialActions)
    {
        this.essentialActions = essentialActions;
    }

    private Task asyncTask;
    private Log logger;

    public Task getAsyncTask()
    {
        return asyncTask;
    }


    public void setAsyncTask(Task asyncTask)
    {
        this.asyncTask = asyncTask;
    }


    public synchronized Task asyncTask()
    {
        if(asyncTask == null)
        {
            asyncTask = new Task(this::job);
            continueWithIfSuccessful(asyncTask, t -> logger.debug("Data loader successful."));
            return asyncTask;
        }
        return new Task(() -> asyncTask.continueWith(t -> job())/* continue with is already thread safe.*/);
    }


    public void runEssentialActions()
    {
        essentialActions.run();
    }

    private void job()
    {
        if(unitDataLoaded) return;
        if(essentialActions != null) essentialActions.run();
        if(isLazy()/* synchronized method.*/) return;/*To facilitate lazy loading so that if you want main action to be carried out you'd just call this
        method after setting this field to false if it was true.*/
        mainAction.run();
        unitDataLoaded = true;
    }

    /**
     * Throws and catches the passed exception. Appropriate when you want an exception
     * to be logged and stack trace to be printed without it crashing the program.
     * @param e
     */
    void silentException(RuntimeException e)
    {
        try
        {
            throw e;
        }
        catch (Exception x)
        {
            logger.debug("Silent exception thrown. Message: " + x.getMessage());
            x.printStackTrace();
        }
    }

    /**
     * Invokes the passed continuation action if the task completed without any exceptions. If it stops
     * prematurely because of errors, the exception will not be thrown but the stack trace will be
     * printed and a debug message of the exception's details will be made.
     * @param task The task.
     * @param continueWithAction The continuation action.
     * @param <T> The data type of the result of the task.
     */
    protected synchronized <T>  void continueWithIfSuccessful(TaskResult<T> task, Consumer<TaskResult<T>> continueWithAction)
    {
        task.continueWith(t ->
        {
            if(t.isFaulted())
            {
                logger.debug(t.getException().getMessage());
                t.getException().printStackTrace();
                return;
            }
            continueWithAction.accept(t);
        });
    }

    /**
     * Invokes the passed continuation action if the task completed without any exceptions. If it stops
     * prematurely because of errors, the exception will not be thrown but the stack trace will be
     * printed and a debug message of the exception's details will be made.
     * @param task
     * @param task The task.
     * @param continueWithAction The continuation action.
     */
    @SuppressWarnings("unchecked")
    protected void continueWithIfSuccessful(Task task, Consumer<Task> continueWithAction)
    {
        task.continueWith(t ->
        {
            if(t.isFaulted())
            {
                logger.debug(t.getException().getMessage());
                t.getException().printStackTrace();
                return;
            }
            continueWithAction.accept(t);
        });
    }


    public static final class Builder
    {
        private boolean setLazy;
        private Runnable lazyAction;
        private Runnable essentialActions;
        private boolean unitDataLoaded;
        private Log logger;

        private Builder()
        {
        }

        public Builder setLazy(boolean setLazy)
        {
            this.setLazy = setLazy;
            return this;
        }

        public Builder lazyAction(Runnable mainAction)
        {
            this.lazyAction = mainAction;
            return this;
        }

        public Builder essentialAction(Runnable essentialActions)
        {
            this.essentialActions = essentialActions;
            return this;
        }

        public Builder unitDataLoaded(boolean unitDataLoaded)
        {
            this.unitDataLoaded = unitDataLoaded;
            return this;
        }

        public Builder logger(Log logger)
        {
            this.logger = logger;
            return this;
        }

        public LazyTaskAssistant build()
        {
            return new LazyTaskAssistant(this);
        }
    }
}

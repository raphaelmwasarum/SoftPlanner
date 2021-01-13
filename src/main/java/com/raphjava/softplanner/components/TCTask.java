package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import net.raphjava.raphtility.asynchrony.Task;
//import net.raphjava.studee.main.Studee;
//import net.raphjava.studeeconsole.components.interfaces.KeyGenerator;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//import java.util.function.Consumer;
//
//public class TCTask extends Task
//{
//    private final Consumer action;
//    private final KeyGenerator keyGenerator;
//    private Consumer continuationAction;
//    private static ExecutorService executorService = Executors.newFixedThreadPool(50);
//    private  static List<TCTask> runningTasks = new ArrayList<>();
//
//    private boolean isFaulted;
//    private boolean hasContinuation;
//    private boolean isContinuationComplete;
//    private boolean isContinuationFaulted;
//    private Exception continuationException;
//    private int operationID;
//
//    public boolean isFaulted()
//    {
//        return isFaulted;
//    }
//
//    public void setFaulted(boolean faulted)
//    {
//        isFaulted = faulted;
//    }
//
//    private Exception exception;
//
//    public Exception getException()
//    {
//        return exception;
//    }
//
//    public void setException(Exception exception)
//    {
//        this.exception = exception;
//    }
//
//    public static int getRunningTasksCount()
//    {
//        return runningTasks.size();
//    }
//
//    public static int getDistinctRunningTasksCount()
//    {
//        return (int) runningTasks.stream().distinct().count();
//    }
//
//    private boolean isComplete;
//    private static final Object continuationActionLock = new Object();
//
//    public boolean isComplete()
//    {
//        return isComplete;
//    }
//
//    public TCTask(Consumer action, KeyGenerator keyGenerator)
//    {
//        super(action);
//        this.action = action;
//        this.keyGenerator = keyGenerator;
//        continuationAction = null;
//
//    }
//
//
//    public void start()
//    {
//        runningTasks.add(this);
//        executorService.submit(() ->
//        {
//            Future<Integer> work = worker(action);
//            try
//            {
//                work.get(); // Used a Callable because it makes it possible to catch ExecutionException where I then call the continuation action passing this as a parameter
//                //to enable subsequent actions based on the outcome of this one.
//                synchronized (continuationActionLock)
//                {
//                    continuationAction = continuationAction != null ? continuationAction : System.out::println; //Just to avoid nullPointer Exception.
//                    isComplete = true;
//                    runningTasks.remove(this);
//                    continuationAction.accept(this);
//                    operationID = (int) keyGenerator.getKey();
//                    System.out.println("Call made here. Task id:" + operationID);
//                }
//                return 0;
//            }
//            catch (InterruptedException ex)
//            {
//                return 0;
//            }
//            catch (ExecutionException ex)
//            {
//                //System.out.println("Execution exception has been caught");
//                setFaulted(true);
//                setException(ex);
//                synchronized (continuationActionLock)
//                {
//                    continuationAction = continuationAction != null ? continuationAction : o -> toString();
//                    runningTasks.remove(this);
//                    continuationAction.accept(this);
//                    System.out.println("Call made here. Task id:" + operationID);
//                }
//                return 0;
//            }
//
//        });
//
//    }
//
//    private Future<Integer> worker(Consumer action)
//    {
//        Future<Integer> submit = executorService.submit(() ->
//        {
//            action.accept(null);
//            return 0;
//        });
//        return submit;
//    }
//
//    public static <T> Task startNew(Consumer<T> action, KeyGenerator keyGenerator)
//    {
//        Task task = new TCTask(action, keyGenerator);
//        task.start();
//        return task;
//    }
//
////    public void continueWith(Consumer<Task> continueAction)
////    {
////        synchronized (continuationActionLock)
////        {
////            if(isComplete)
////            {
////                System.out.println("Task completed by the time continuation was called. task id: " + operationID);
////                runningTasks.add(this);
////                hasContinuation = true;
////
////                Future<Integer> waiter = executorService.submit(() ->
////                {
////                   try
////                   {
////                       Future<Integer> work = worker(e -> continueAction.accept(getThisTask()));
////                       work.get();
////                       isContinuationComplete = true;
////                       runningTasks.remove(this);
////                   }
////                   catch (ExecutionException ex)
////                   {
////                       isContinuationFaulted = true;
////                       continuationException = ex;
////                       runningTasks.remove(this);
////                   }
////                    return 0;
////                });
/////*
////                executorService.submit(() ->
////                {
////                    continueAction.accept(getThisTask());
////                    System.out.println("Call made here. Task id:" + operationID);
////                });*/
////                /*runningTasks.remove(this);*/
////                return;
////            }
////            continuationAction = continueAction;
////        }
////
////    }
//
//    private Task getThisTask()
//    {
//        return this;
//    }
//
//    public void reset(Boolean...doNotRemoveContinuationAction)
//    {
//        isComplete = false;
//        if(doNotRemoveContinuationAction != null && doNotRemoveContinuationAction.length > 0 && doNotRemoveContinuationAction[0]) return;
//        continuationAction = null;
//    }
//}

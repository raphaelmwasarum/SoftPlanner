package com.raphjava.softplanner.components;

import net.raphjava.raphtility.collectionmanipulation.ArrayList;
import net.raphjava.raphtility.collectionmanipulation.interfaces.Explorable;

import java.lang.ref.WeakReference;
import java.util.function.Consumer;

public class NotifyingObject
{

    private Consumer<Runnable> mainThreadDispatcher;


    public NotifyingObject(Consumer<Runnable> mainThreadDispatcher)
    {
        this.mainThreadDispatcher = mainThreadDispatcher;
    }

    protected Explorable<Runnable> notifiables = new ArrayList<Runnable>();

    public synchronized void addWeakNotifiable(Runnable notifiable)
    {
        addNotifiable(new WeakNotifiable(notifiable, this::removeNotifiable));
    }


    public synchronized void addNotifiable(Runnable notifiable)
    {
        notifiables.add(notifiable);
    }

    public void informNotifiables()
    {
        for (Runnable notifiable : getCopyOfNotifiables()) notifiable.run();
    }

    private synchronized Explorable<Runnable> getCopyOfNotifiables()
    {
        return notifiables.list();
    }


    public synchronized void removeNotifiable(Runnable notifiable)
    {
        notifiables.remove(notifiable);
    }

    public synchronized void removeNotifiables()
    {
        notifiables.clear();
    }

    public void informNotifiablesOnMainThread()
    {
        for (Runnable i : getCopyOfNotifiables()) mainThreadDispatcher.accept(i);
    }


    /**
     * A weak reference notifiable. It removes itself from notifiables the moment the actual notifiable is garbage collected.
     *
     * @param
     */
    public class WeakNotifiable implements Runnable
    {
        private final Consumer<Runnable> removeMeFromNotifiablesAction;
        private WeakReference<Runnable> weakNotifiableReference;

        public Runnable getNotifiable()
        {
            return weakNotifiableReference.get();
        }

        public WeakNotifiable(Runnable weakNotifiableReference, Consumer<Runnable> unRegistrationAction)
        {
            this.weakNotifiableReference = new WeakReference<Runnable>(weakNotifiableReference);
            this.removeMeFromNotifiablesAction = unRegistrationAction;
        }


        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        @Override
        public void run()
        {
            Runnable notifiable = weakNotifiableReference.get();
            if (notifiable == null) removeMeFromNotifiablesAction.accept(this);
            else notifiable.run();
        }
    }

}

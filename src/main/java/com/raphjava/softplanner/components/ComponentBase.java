package com.raphjava.softplanner.components;

import com.raphjava.softplanner.annotations.Logging;
import com.raphjava.softplanner.annotations.Named;
import com.raphjava.softplanner.components.binding.Binder;
import com.raphjava.softplanner.components.binding.ListenerManager;
import com.raphjava.softplanner.components.interfaces.DispatcherHelper;
import com.raphjava.softplanner.components.interfaces.Factory;
import com.raphjava.softplanner.components.interfaces.KeyGenerator;
import com.raphjava.softplanner.data.interfaces.DataService;
import com.raphjava.softplanner.data.models.Notification;
import com.raphjava.softplanner.interfaces.Communication;
import com.raphjava.softplanner.main.RaphJavaObject;
import net.raphjava.raphtility.asynchrony.Task;
import net.raphjava.raphtility.asynchrony.TaskResult;
import net.raphjava.raphtility.collectionmanipulation.ArrayList;
import net.raphjava.raphtility.collectionmanipulation.interfaces.Explorable;
import net.raphjava.raphtility.collectionmanipulation.interfaces.NotifyingCollection;
import net.raphjava.raphtility.interfaceImplementations.Property;
import net.raphjava.raphtility.logging.interfaces.Log;
import net.raphjava.raphtility.logging.interfaces.LoggerFactory;
import net.raphjava.raphtility.messaging.MessageWithContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

/**
 * Base class for a component. A component here is a part of a larger whole of an app with a specific function.
 * A component can also be made up of other components, or depend on other components.
 */
public class ComponentBase extends RaphJavaObject
{
    private NotifyingCollection<Action> commands = new net.raphjava.raphtility.collectionmanipulation.NotifyingCollection<>();


    public NotifyingCollection<Action> getCommands()
    {
        return commands;
    }

    private Runnable closeViewDelegate;

    public void setCloseViewDelegate(Runnable closeViewDelegate)
    {
        this.closeViewDelegate = closeViewDelegate;
    }


    public void setShowViewDelegate(Consumer<Boolean> showViewDelegate)
    {
        this.showViewDelegate = showViewDelegate;
    }

    private Consumer<Boolean> showViewDelegate;

    private Consumer<ComponentBase> viewInitialization;


    public void openView(boolean... dialog)
    {
        viewInitialization.accept(this);
        showViewDelegate.accept(dialog.length != 0 && dialog[0]);
    }



    protected DataService dataService;

//    protected final Itemizer look;

    //region viewModelID

    private double viewModelID;

    public void setViewModelID(double viewModelID)
    {
        this.viewModelID = viewModelID;

    }

    public double getViewModelID()
    {
        return viewModelID;
    }

    //endregion

    private String myLoggerName;

    public String getMyLoggerName()
    {
        return myLoggerName;
    }

    protected Communication communication;

    protected DispatcherHelper dispatcherHelper;

    protected Log logger;


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
            debug("Silent exception thrown. Message: " + x.getMessage());
            x.printStackTrace();
        }
    }

    /**
     * This field is here for facilitating  generation of builders in viewmodels so that
     * when you're creating new builder code using Alt + Insert, you just select this field as one
     * of the fields to include in the generation, so that every time you do this you'll only have
     * to add super(builder.baseBuilder) code after the generation.
     */
    protected Builder baseBuilder;

    protected ComponentBase(Builder builder)
    {
        dataService = builder.dataService;
        communication = builder.communication;
        dispatcherHelper = builder.dispatcherHelper;
        keyGenerator = builder.keyGenerator;
        loggerFactory = builder.loggerFactory;
//        viewInitialization = builder.viewInitialization;
//        dialogViewModelFactory = builder.dialogViewModelFactory;
        binder = builder.binder;
        myLoggerName = getClass().getSimpleName();
        logger = loggerFactory.createLogger(myLoggerName);
        setViewModelID(keyGenerator.getKey());
        subscribeToMessages();
    }

    protected boolean stringsMatch(String o, String n)
    {
        if (o != null) return o.equals(n);
        else return n == null;
    }

    @SuppressWarnings("unchecked")
    private void subscribeToMessages()
    {
        register(Notification.RepositoryHasChanged, Collection.class, coll ->
        {
            ArrayList<Class> changedEntities = new ArrayList<Class>();
            if (repositoryChangeAffectsMe((Collection<Class>) coll, changedEntities))
                handleRepositoryChangesAsync(changedEntities);
        });
    }

//    public ComponentTerminalViewModel getTerminalViewModel()
//    {
//        return null; /*Was trying to kill several import bugs.*/
//    }

//    public DescriptionItemViewModel getDescriptionViewModel()
//    {
//        return null;
//    }

    // Repository changes

    /**
     * Entities whose repository changes I listen to.
     */
    protected Explorable<Class> myEntities = new ArrayList<Class>();

    protected void handleRepositoryChangesAsync(Collection<Class> changedEntities)
    {
    }

    private boolean repositoryChangeAffectsMe(Collection<Class> coll, ArrayList<Class> changedEntities)
    {
        for (Class e : myEntities)
        {
            if (coll.contains(e)) changedEntities.add(e);
        }
        return changedEntities.size() != 0;
    }


    protected Task broadcastRepositoryChangeAsync(Explorable<Class> changedEntities)
    {
        return startNewTask(() -> broadcastRepositoryChanges(changedEntities));
    }

    protected void broadcastRepositoryChanges(Explorable<Class> changedEntities)
    {
        sendMessage(Notification.RepositoryHasChanged, changedEntities);
    }


    //

    //region BindingEngine

    private ListenerManager listenerManager = new ListenerManager();

    private Explorable<net.raphjava.raphtility.interfaceImplementations.Property> notifyingProperties = new ArrayList<Property>();

    protected <T> net.raphjava.raphtility.interfaceImplementations.Property<T> setupProperty(net.raphjava.raphtility.interfaces.Notifiable<T> fieldUpdater, T initialValue)
    {
        net.raphjava.raphtility.interfaceImplementations.Property<T> dp = new net.raphjava.raphtility.interfaceImplementations.Property<T>(initialValue);
        notifyingProperties.add(dp);
        dp.addNotifiable(fieldUpdater);
        dp.set(initialValue);
        return dp;
    }

    private Binder binder;

    protected <T> ListenerManager.PropertyListener<T> bind(Property<T> property)
    {
        return binder.bind(property);
    }

    protected <T> ListenerManager.CollectionListener<T> bind(NotifyingCollection<T> source)
    {
        ListenerManager.CollectionListener<T> cl = listenerManager.collectionListener();
        source.addWeakNotifiable(cl);
        return cl;
    }


    protected <T> boolean set(net.raphjava.raphtility.interfaceImplementations.Property<T> property, T value)
    {
        if (property == null) return false;
        property.set(value);
        return true;
    }


    //endregion
//    private Factory<DialogViewModel> dialogViewModelFactory;

//    protected void alert(String notification)
//    {
//        DialogViewModel vm = dialogViewModelFactory.createProduct().notification(notification);
//        dispatcherHelper.checkBeginInvokeOnUI(() -> vm.openView(true));
//    }

    protected void error(String message)
    {
        logger.error(message);


    }
    //If property has changed (a new entry has been made in the propertyChangedOList),
    //  -   In the list listener, get the most recently added item.
    //  -   Call the binder.set() method with the recent item as arguments.
    //  -   Remove the recently added item.

    public void onViewCreated()
    {
        debug("View for " + myLoggerName + " of id: " + getViewModelID() + " has been created.");
    }

    public void cleanUp()
    {
        setCloseViewDelegate(null); //At this point closeViewDelegate has been called.
        for (Property nP : notifyingProperties) nP.removeNotifiables();
        unRegister(this);
    }

    protected void successFailedNotificationResult(boolean isSuccessfull, String... notification)
    {
        String message;
        boolean userMessageIsValid = notification.length > 0 && !notification[0].isEmpty();
        if (isSuccessfull)
        {
            if (userMessageIsValid)
            {
                message = notification[0] + " done successfully.";
            }
            else
            {
                message = "Action done successfully.";
            }
        }
        else
        {
            if (userMessageIsValid)
            {
                message = notification[0] + " was not done successfully";
            }
            else
            {
                message = "Action was not done successfully";
            }
        }
        dispatcherHelper.checkBeginInvokeOnUI(() -> sendMessage(Notification.ShowDialog, message));
    }

    protected KeyGenerator keyGenerator;

    @Override
    public String toString()
    {
        return myLoggerName + " of id: " + viewModelID;
    }

    protected int getKey()
    {
        return (int) keyGenerator.getKey();
    }

    /*protected Task startNewTask(Consumer action)
    {
        var t = new Task(action);
        t.start();
        return t;
    }*/

    protected Task startNewTask(Runnable action)
    {
        Task t = new Task(action);
        t.start();
        return t;
    }

    protected String path(String... paths)
    {
        StringBuilder format = new StringBuilder();
        int currentIndex = 0;
        for(String path : paths)
        {
            format.append(path);
            if(currentIndex != (paths.length - 1)) format.append(".");
            currentIndex++;
        }
        return format.toString();
    }

    protected LoggerFactory loggerFactory;

    protected Log logger()
    {
        if (logger == null)
        {
            logger = loggerFactory.createLogger(getFormattedLoggerName());
        }
        return logger;
    }

    public String getFormattedLoggerName()
    {
        return "[" + getMyLoggerName() + "]";
    }

    protected void debug(String message)
    {
        logger.debug(getFormattedLoggerName() + " - " + message);
    }

    @SuppressWarnings("unchecked")
    public void register(Notification notification, Consumer action)
    {
        communication.register(this, net.raphjava.raphtility.messaging.Message.class, new MessengerAction<>(notification, getViewModelID(), action));
    }

    @SuppressWarnings("unchecked")
    public <TContent> void register(Notification notification, Class<TContent> contentClass, Consumer<TContent> action)
    {
        Consumer<MessageWithContent<OperationContent<TContent, Void>, Type, Double, Notification>> actionWrapper = m -> action.accept(m.getContent().getContent1());
        communication.register(this, MessageWithContent.class, new MessengerAction<>(notification, getViewModelID(), actionWrapper));
    }

    @SuppressWarnings("unchecked")
    public <TContent1, TContent2> void register(Notification notification, Class<TContent1> content1Class, Class<TContent2> content2Class, BiConsumer<TContent1, TContent2> action)
    {
        Consumer<MessageWithContent<OperationContent<TContent1, TContent2>, Type, Double, Notification>> actionWrap = m -> action.accept(m.getContent().getContent1(), m.getContent().getContent2());
        communication.register(this, MessageWithContent.class, new MessengerAction<>(notification, getViewModelID(), actionWrap));
    }

    public void sendMessage(Notification notification, Type... senderType)
    {
        communication.sendMessage(notification, getViewModelID(), senderType);
    }

    public <Content> void sendMessage(Notification notification, Content content, Type... senderType)
    {
        communication.sendMessage(notification, new OperationContent<Content, Void>(0, content), getViewModelID(), senderType);
    }

    public <CallbackParameter> void sendMessage(Notification notification, Consumer<CallbackParameter> afterProcessAction, Type... senderType)
    {
        communication.sendMessage(notification, new OperationContent<Consumer<CallbackParameter>, Void>(0, afterProcessAction), getViewModelID(), senderType);
    }

    public <Content, CallbackParameter> void sendMessage(Notification notification, Content content, Consumer<CallbackParameter> afterProcessAction)
    {
        communication.sendMessage(notification, new OperationContent<Content, Consumer<CallbackParameter>>(0, content, afterProcessAction), getViewModelID());
    }

    public void unRegister(Object object)
    {
        communication.unRegister(object);
    }

    public void closeDialog()
    {
        closeViewDelegate.run();
    }

    public void closeView()
    {
        closeViewDelegate.run();
    }

    protected <T> Explorable<T> asExp(Collection<T> coll)
    {
        return new net.raphjava.raphtility.collectionmanipulation.ArrayList<T>(coll);
    }

    protected void throwNotImplementedEx(String... extraMessage)
    {
        String n = "Not implemented";
        throw new RuntimeException(extraMessage.length == 0 ? n : n + extraMessage[0]);
    }

    /**
     * Invokes the passed continuation action if the task completed without any exceptions. If it stops
     * prematurely because of errors, the exception will not be thrown but the stack trace will be
     * printed and a debug message of the exception's details will be made.
     *
     * @param task               The task.
     * @param continueWithAction The continuation action.
     * @param <T>                The data type of the result of the task.
     */
    protected synchronized <T> void continueWithIfSuccessful(TaskResult<T> task, Consumer<TaskResult<T>> continueWithAction)
    {
        task.continueWith(t ->
        {
            if (t.isFaulted())
            {
                debug(t.getException().getMessage());
                t.getException().printStackTrace();
                sendMessage(Notification.EventLog, "Error. Details: " + t.getException().getMessage());
                return;
            }
            continueWithAction.accept(t);
        });
    }

    /**
     * Invokes the passed continuation action if the task completed without any exceptions. If it stops
     * prematurely because of errors, the exception will not be thrown but the stack trace will be
     * printed and a debug message of the exception's details will be made.
     *
     * @param task
     * @param task               The task.
     * @param continueWithAction The continuation action.
     */
    @SuppressWarnings("unchecked")
    protected void continueWithIfSuccessful(Task task, Consumer<Task> continueWithAction)
    {
        task.continueWith(t ->
        {
            if (t.isFaulted())
            {
                debug(t.getException().getMessage());
                t.getException().printStackTrace();
                return;
            }
            continueWithAction.accept(t);
        });
    }


    public void handlePropertyChanges(String propertyName)
    {

    }


    protected void eventLog(String s)
    {
        sendMessage(Notification.EventLog, s);
    }


    @Lazy
    @Component
    @Scope(Singleton)
    public static final class Builder
    {
        private DataService dataService;
        private Communication communication;
        private DispatcherHelper dispatcherHelper;
        private KeyGenerator keyGenerator;
        private LoggerFactory loggerFactory;
//        private Consumer<TheViewModelBase> viewInitialization;
//        private Factory<DialogViewModel> dialogViewModelFactory;
        private Binder binder;
        private Factory<Action> actionViewModelFactory;
//        private Factory<ConsoleInputViewModel> consoleInputViewModelFactory;

        private Builder()
        {
        }

//        @Autowired
//        public Builder setDialogViewModelFactory(@Named(DialogViewModel.FACTORY) Factory<DialogViewModel> dialogViewModelFactory)
//        {
//            this.dialogViewModelFactory = dialogViewModelFactory;
//            return this;
//        }

//        @Autowired
//        public Builder viewInitialization(Consumer<TheViewModelBase> viewInitialization)
//        {
//            this.viewInitialization = viewInitialization;
//            return this;
//        }

//        @Autowired
//        public Builder dialogViewModelFactory(@Named(DialogViewModel.FACTORY) Factory<DialogViewModel> dialogViewModelFactory)
//        {
//
//            return this;
//        }


        @Autowired
        public Builder setDataService(@Logging DataService val)
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

        @Autowired
        public Builder setDispatcherHelper(DispatcherHelper val)
        {
            dispatcherHelper = val;
            return this;
        }

        @Autowired
        public Builder setKeyGenerator(KeyGenerator val)
        {
            keyGenerator = val;
            return this;
        }

        @Autowired
        public Builder setLoggerFactory(LoggerFactory val)
        {
            loggerFactory = val;
            return this;
        }

        @Autowired
        public Builder binder(Binder binder)
        {
            this.binder = binder;
            return this;
        }

        public Builder actionViewModelFactory(Factory<Action> actionViewModelFactory)
        {
            this.actionViewModelFactory = actionViewModelFactory;
            return this;
        }

//        public Builder consoleInputViewModelFactory(Factory<ConsoleInputViewModel> consoleInputViewModelFactory)
//        {
//            this.consoleInputViewModelFactory = consoleInputViewModelFactory;
//            return this;
//        }
    }

}

package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.DispatcherHelper;
import javafx.application.Platform;

import java.util.concurrent.CountDownLatch;

public class JavaFXDispatcher implements DispatcherHelper
{

    @Override
    public void checkBeginInvokeOnUI(Runnable action)
    {
        if(Platform.isFxApplicationThread())
        {
            action.run();
            return;
        }
        Platform.runLater(action);
    }

    @Override
    public void invokeOnUI(Runnable action)
    {
        if (Platform.isFxApplicationThread())
        {
            action.run();
        }
        else
        {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(() ->
            {
                action.run();
                latch.countDown();
            });

            try
            {
                latch.await();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}

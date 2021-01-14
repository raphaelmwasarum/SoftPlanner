package com.raphjava.softplanner.components;

import com.raphjava.softplanner.data.models.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ConsoleInput extends ComponentBase
{

    private ConsoleInput(Builder builder)
    {
        super(builder.baseBuilder);
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }


    @Override
    public void cleanUp()
    {
        super.cleanUp();
    }

    public Optional<String> getInput()
    {
        Scanner scanner = new Scanner(System.in);
        try
        {
            return Optional.of(scanner.nextLine());
        }
        catch (NoSuchElementException x)
        {
            System.out.println("Forced close. Beginning pre-closing operations...please wait...");
            sendMessage(Notification.ForcedCloseCleanUp);
            System.out.println("pre-closing operations complete.");
            System.out.println("Stopping program.");
            System.exit(0);
            return Optional.empty();
        }




//        try
//        {
//            String rd = reader.readLine();
//            if (rd == null) return Optional.empty();
//            return Optional.of(rd);
//        }
//        catch (IOException e)
//        {
//            System.out.println("Input error.");
//            return Optional.empty();
//        }
        /*if(scanner.hasNext()) return Optional.of(scanner.nextLine());
        else
        {
            System.out.println("Input error.");
            return Optional.empty();
        }
*/
        /*try
        {

            return Optional.of(scanner.nextLine());
        }
        catch (Exception x)
        {
            System.out.println("Input error. Enter correct input or q to leave input port.");

            return Optional.empty();
        }*/
    }


    @Lazy
    @Component
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ConsoleInput>
    {
        private ComponentBase.Builder baseBuilder;

        private Builder()
        {
            super(ConsoleInput.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        public synchronized ConsoleInput build()
        {
            return new ConsoleInput(this);
        }
    }
}

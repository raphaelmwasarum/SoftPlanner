package com.raphjava.softplanner.components;

import com.raphjava.softplanner.data.models.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ConsoleInput extends ComponentBase
{

    private ConsoleInput(Builder builder)
    {
        super(builder.baseBuilder);
        shutDown = builder.shutDown;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    private SystemExit shutDown;

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
            shutDown.shutDown("Forced close. ");
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
//            show("Input error.");
//            return Optional.empty();
//        }
        /*if(scanner.hasNext()) return Optional.of(scanner.nextLine());
        else
        {
            show("Input error.");
            return Optional.empty();
        }
*/
        /*try
        {

            return Optional.of(scanner.nextLine());
        }
        catch (Exception x)
        {
            show("Input error. Enter correct input or q to leave input port.");

            return Optional.empty();
        }*/
    }


    @Lazy
    @Component
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ConsoleInput>
    {
        private ComponentBase.Builder baseBuilder;
        private SystemExit shutDown;

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

        @Autowired
        public Builder shutDown(SystemExit shutDown)
        {
            this.shutDown = shutDown;
            return this;
        }

        public synchronized ConsoleInput build()
        {
            return new ConsoleInput(this);
        }
    }
}

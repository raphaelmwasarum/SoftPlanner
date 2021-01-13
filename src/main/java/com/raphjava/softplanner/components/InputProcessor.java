package com.raphjava.softplanner.components;

import com.raphjava.softplanner.annotations.Named;
import com.raphjava.softplanner.components.interfaces.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class InputProcessor extends ComponentBase
{


    private InputProcessor(Builder builder)
    {
        super(builder.baseBuilder);
        actionFactory = builder.actionFactory;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public int getInt()
    {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }


    public String getInput()
    {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public void start()
    {
        debug(String.format("Starting [%s] with console interface.", this));
        String input = "";
        while (input != null && !input.equalsIgnoreCase("q"))
        {
            System.out.println("Enter command. (Enter \"commands\" to see available commands.):");
            final boolean[] rightInput = new boolean[1];
            input = getInput();
            getCommand(input).ifPresent(c ->
            {
                c.getAction().run();
                rightInput[0] = true;
            });

            if (!rightInput[0] && !input.equalsIgnoreCase("q"))
            {
                System.out.println("Wrong input. Type \"commands\" to see available commands.");

            }


        }
    }

    public ComponentBase getCurrentContent()
    {
        return currentContent;
    }

    public void setCurrentContent(ComponentBase currentContent)
    {
        this.currentContent = currentContent;
    }

    private void initialize()
    {
        setCurrentContent(this);
        loadCommands();


    }

    private Factory<Action> actionFactory;


    private void printCommands()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("***** Available commands shown below: *****\n\n");
        if(!currentContent.equals(this)) currentContent.getCommands().forEach(c -> sb.append(c.getCommandDescription()).append("\n"));
        getCommands().forEach(c -> sb.append(c.getCommandDescription()).append("\n"));
        sb.append("\"q\" - to quit.");
        sb.append("\n\n***** Available commands.*****\n\n\n");
        System.out.println(sb.toString());

    }


    private void loadCommands()
    {
        Action printCommands = actionFactory.createProduct();
        printCommands.setCommandDescription("commands");
        printCommands.setAction(this::printCommands);
        getCommands().add(printCommands);

        Action addNewProject = actionFactory.createProduct();
        addNewProject.setCommandDescription("Add New Project");
        addNewProject.setAction(this::addNewProject);
        getCommands().add(addNewProject);

    }

    private void addNewProject()
    {
        System.out.println("Adding new project");
        //TODO Continue from here.
        System.out.println("Adding new project complete.");

    }

    private ComponentBase currentContent;


    private Optional<Action> getCommand(String commandId)
    {
        Action comm = currentContent.getCommands().firstOrDefault(c -> c.getCommandDescription().equalsIgnoreCase(commandId));
        if (comm == null)
        {
            comm = getCommands().firstOrDefault(c -> c.getCommandDescription().equalsIgnoreCase(commandId));
        }
        return comm != null ? Optional.of(comm) : Optional.empty();
    }

    @Lazy
    @Component
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<InputProcessor>
    {

        private ComponentBase.Builder baseBuilder;
        private Factory<Action> actionFactory;

        private Builder()
        {
            super(InputProcessor.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        @Autowired
        public Builder actionFactory(@Named(Action.FACTORY) Factory<Action> actionFactory)
        {
            this.actionFactory = actionFactory;
            return this;
        }

        public InputProcessor build()
        {

            InputProcessor ip = new InputProcessor(this);
            ip.initialize();
            return ip;
        }

    }
}

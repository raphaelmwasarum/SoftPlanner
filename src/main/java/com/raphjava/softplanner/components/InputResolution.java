package com.raphjava.softplanner.components;

import com.raphjava.softplanner.annotations.Named;
import com.raphjava.softplanner.components.interfaces.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class InputResolution extends ComponentBase
{


    private InputResolution(Builder builder)
    {
        super(builder.baseBuilder);
        inputService = builder.inputService;
        projectAdditionFactory = builder.projectAdditionFactory;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }



    public void start()
    {
        debug(String.format("Starting [%s] with console interface.", this));
        Optional<String> input = Optional.empty();
        while (Optional.of(

                /*If input has data it returns it otherwise returns empty string*/
                !input.orElse("")

                /*If input data is q loop stops, otherwise it continues.*/
                .equalsIgnoreCase("q")).orElse(true))
        {
            System.out.println("Enter command. (Enter \"commands\" to see available commands.):");
            input = inputService.getInput();
                    input.flatMap(this::getCommand)
                    .ifPresent(c -> c.getAction().run());


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

    private ConsoleInput inputService;


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


    private Factory<ProjectAddition> projectAdditionFactory;


    private void addNewProject()
    {
        ProjectAddition pa = projectAdditionFactory.createProduct();
        pa.startAsConsole();

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
    public static final class Builder extends AbFactoryBean<InputResolution>
    {

        private ComponentBase.Builder baseBuilder;
        private ConsoleInput inputService;
        private Factory<ProjectAddition> projectAdditionFactory;

        private Builder()
        {
            super(InputResolution.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        @Autowired
        public Builder inputService(ConsoleInput inputService)
        {
            this.inputService = inputService;
            return this;
        }


        @Autowired
        public Builder projectAdditionFactory(@Named(ProjectAddition.FACTORY) Factory<ProjectAddition> projectAdditionFactory)
        {
            this.projectAdditionFactory = projectAdditionFactory;
            return this;
        }

        public InputResolution build()
        {
            InputResolution ip = new InputResolution(this);
            ip.initialize();
            return ip;
        }

    }
}

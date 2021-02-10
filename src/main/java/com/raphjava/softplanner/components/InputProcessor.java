package com.raphjava.softplanner.components;

import com.raphjava.softplanner.annotations.Named;
import com.raphjava.softplanner.components.interfaces.Factory;
import com.raphjava.softplanner.domain.Help;
import com.raphjava.softplanner.services.ConsoleOutputService;
import net.raphjava.qumbuqa.commons.trees.Tree;
import net.raphjava.qumbuqa.commons.trees.TreeNodeImp;
import net.raphjava.qumbuqa.commons.trees.interfaces.TreeNode;
import net.raphjava.raphtility.collectionmanipulation.interfaces.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Consumer;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class InputProcessor extends ComponentBase
{


    private ConsoleInput inputService;

    private InputProcessor(Builder builder)
    {
        super(builder.baseBuilder);
        inputService = builder.inputService;
        shutDownService = builder.shutDownService;
        helpFactory = builder.helpFactory;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public void process(String commandDoesntExistMessage)
    {
        processInput(inputService.getInput().orElse(""), commandDoesntExistMessage);
    }

    public void processInput(String input, String... commandDoesntExistMessage)
    {
        Optional<String> _input = Optional.of(input);
        boolean[] commandExists = new boolean[1];
        _input.flatMap(this::processCommand).ifPresent(successful ->
        {
            commandExists[0] = true;
            if(!successful) show("Error processing command.");
        });

        if(!commandExists[0] && !_input.orElse("").equalsIgnoreCase("q"))
        {
            if(commandDoesntExistMessage.length != 0) show(commandDoesntExistMessage[0]);
        }
    }


    /** Processes a command string.
     * @param commandString command data.
     * @return True wrapped in an Optional if command was successfully processed,
     * <p>False wrapped in an Optional if an exception was thrown while processing the command,</p>
     * <p>An Empty Optional if the command doesn't exist.</p>
     */
    private Optional<Boolean> processCommand(String commandString)
    {
        List<String> commandData = split(commandString, "\"");
        List<String> commandData0 = new net.raphjava.raphtility.collectionmanipulation.ArrayList<>(commandData);
        Queue<String> cd;
        if(commandData.size() > 1) //command string has quoted arguments.
        {
            commandData = split(commandData.get(0), " ");
            commandData.add(commandData0.get(commandData0.size() - 1));
            cd = new LinkedList<>(commandData);
        }
        else
        {
            cd = new LinkedList<>(split(commandString, " "));
        }

        return processCommand(commandTree.getRoot(), cd);
    }

    public Optional<Boolean> processCommand(TreeNode<Command> commandNode, Queue<String> cd)
    {
        String comData = cd.poll();
        return getChild(commandNode, comData).flatMap(args -> executeCommand(cd, args));
    }

    public Optional<Boolean> executeCommand(Queue<String> args, TreeNode<Command> commandNode)
    {
        try
        {
//                commandTreeNode.getValue().action.accept(cd);
            commandNode.getValue().action.accept(args);
            return Optional.of(true);
        }
        catch (Exception e)
        {
            show(e.getMessage() != null ? e.getMessage() : e.toString());
            return Optional.of(false);
        }
    }

    public Optional<TreeNode<Command>> getChild(TreeNode<Command> parent, String comData)
    {
        TreeNode<Command> child = asExp(parent.getChildren()).firstOrDefault(c -> c
                .getValue().description.equalsIgnoreCase(comData));
        return child != null ? Optional.of(child) : Optional.empty();
    }

    public TreeNode<Command> newTreeItem()
    {
        return new TreeNodeImp<>(newCommand());
    }

    private Tree<Command> commandTree = new Tree<>(new TreeNodeImp<>(newCommand().setDescription("root")));

    public Tree<Command> getCommandTree()
    {
        return commandTree;
    }

    Command newCommand()
    {
        return new Command();
    }

    private SystemExit shutDownService;

    private void initialize()
    {
        finishTagging("Input");
        TreeNode<Command> quit = newTreeItem();
        quit.getValue().setDescription("q").setAction(q -> shutDownService.shutDown("Normal shutdown."));
        commandTree.getRoot().getChildren().add(quit);

        TreeNode<Command> help = newTreeItem();
        help.getValue().setDescription("Help").setAction(this::showHelp);
        commandTree.getRoot().getChildren().add(help);
    }

    private Help help;
    private Factory<Help> helpFactory;

    public synchronized Help getHelp()
    {
        if(help == null) help = helpFactory.createProduct();
        return help;

    }


    private void showHelp(Queue<String> commandData)
    {
        getHelp().startAsConsole();
    }




    public static class Command
    {
        private String description;

        private Consumer<Queue<String>> action;

        public Command setDescription(String description)
        {
            this.description = description;
            return this;
        }

        public Command setAction(Consumer<Queue<String>> action)
        {
            this.action = action;
            return this;
        }

        public String getDescription()
        {
            return description;
        }
    }

    public static final String FACTORY = "inputProcessorFactory";

    @Lazy
    @Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<InputProcessor>
    {
        private ConsoleOutputService outputService;
        private ComponentBase.Builder baseBuilder;

        private ConsoleInput inputService;
        private SystemExit shutDownService;
        private Factory<Help> helpFactory;

        private Builder()
        {
            super(InputProcessor.class);
        }

        @Autowired
        public Builder outputService(ConsoleOutputService outputService)
        {
            this.outputService = outputService;
            return this;
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
        public Builder shutDownService(SystemExit shutDownService)
        {
            this.shutDownService = shutDownService;
            return this;
        }

        @Autowired
        public Builder helpFactory(@Named(Help.FACTORY) Factory<Help> helpFactory)
        {
            this.helpFactory = helpFactory;
            return this;
        }

        @PostConstruct
        private void post()
        {
            System.out.printf("%s construction complete. From the post method.%n", Objects.requireNonNull(getObjectType())
                    .getSimpleName());
        }


        public synchronized InputProcessor build()
        {
            InputProcessor i = new InputProcessor(this);
            i.initialize();
            return i;
        }

    }
}

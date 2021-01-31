package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.annotations.Named;
import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.components.InputProcessor;
import com.raphjava.softplanner.components.interfaces.Factory;
import com.raphjava.softplanner.services.ConsoleOutputService;
import net.raphjava.qumbuqa.commons.trees.interfaces.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Queue;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class Help extends ComponentBase
{

    private InputProcessor inputProcessor;


    private Help(Builder builder)
    {
        super(builder.baseBuilder);
        outputService = builder.outputService;
        inputProcessor = builder.inputProcessor;
        finishTagging("Help");
    }

    private void initialize()
    {
        loadCommandTree();

    }

    /* Tree:
     * Project
     *   Help
     *
     */

    private void loadCommandTree()
    {
        TreeNode<InputProcessor.Command> projectNode = inputProcessor.newTreeItem();
        projectNode.getValue().setDescription("Project").setAction(this::projectActions);
        inputProcessor.getCommandTree().getRoot().getChildren().add(projectNode);
        TreeNode<InputProcessor.Command> projectHelpNode = inputProcessor.newTreeItem();
        projectHelpNode.getValue().setDescription("Help").setAction(this::projectHelp);
        projectNode.getChildren().add(projectHelpNode);


    }

    private void projectActions(Queue<String> args)
    {
        inputProcessor.getChild(inputProcessor.getCommandTree().getRoot(), "project")
                .ifPresent(projectNode -> inputProcessor.getChild(projectNode, args.poll())
                        .ifPresent(projectCommand -> inputProcessor.executeCommand(args, projectCommand)));

    }

    private void projectHelp(Queue<String> args)
    {
        if (args.isEmpty())
        {
            show(asExp(Arrays.asList("The following are the project commands that can be executed:"
                    , "project add \"[Project name-Soft Planner], [Description-Helps in the planning the development of an app]\""
                    , "project list - list all saved projects."
                    , "project open <Project ID> - opens project of the passed id."
                    , "project current - shows the active open project."
                    , "project edit \"[Project name-Soft Planner], [Description-Helps in the planning the development of an app]\" - edits the currently open project. i.e. the project that will be described when you type command: \"project current\"."
                    , "project tree - Shows the project tree."
                    , "project schedule - shows the scheduling of the project."
                    , "project delete <Project ID> - deletes the project of the passed id from the repository."
                    , "project component list - shows the project's component list."
                    , "project component add <Component data> - adds a component to the project"
                    , "project component open <Component ID> - opens the component with the passed id."
                    , "project component current - shows the active open component."
                    , "project component edit <Component data> - adds a component to the project"
                    , "project component delete <Component ID> - deletes a component from the current component."
            )).selectToObject(new StringBuilder(), (sb, c) -> sb.append(c).append("\n")).toString());

        }
        else show(String.format("Couldn't understand command. Extra arguments %s not necessary.",
                asExp(args).selectToObject(new StringBuilder(), StringBuilder::append).toString()));

    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public static final String FACTORY = "helpFactory";

    public void startAsConsole()
    {
        show("Enter 'project help' to know more about project commands.");
        inputProcessor.processInput("Invalid help command");

    }


    @Lazy
    @Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<Help>
    {
        private ConsoleOutputService outputService;
        private ComponentBase.Builder baseBuilder;

        private InputProcessor inputProcessor;
        private Factory<InputProcessor> inputProcessorFactory;

        private Builder()
        {
            super(Help.class);
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
        public Builder inputProcessorFactory(@Named(InputProcessor.FACTORY) Factory<InputProcessor> inputProcessorFactory)
        {
            this.inputProcessorFactory = inputProcessorFactory;
            return this;
        }


        public synchronized Help build()
        {
            inputProcessor = inputProcessorFactory.createProduct(); /*I had to do this to resolve a chicken-egg scenario
            between this builder and inputProcessor so don't touch this unless you have eliminated that problem.*/

            Help h = new Help(this);
            h.initialize();
            return h;
        }

    }
}

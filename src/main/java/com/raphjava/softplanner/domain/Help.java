package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.components.InputProcessor;
import com.raphjava.softplanner.services.ConsoleOutputService;
import net.raphjava.qumbuqa.commons.trees.interfaces.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
        if(args.isEmpty())
        {
            show("Printing project help...");
        }
        else show("Couldn't understand command. Extra arguments not necessary.");

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
        public Builder inputProcessor(InputProcessor inputProcessor)
        {
            this.inputProcessor = inputProcessor;
            return this;
        }

        public synchronized Help build()
        {
            Help h = new Help(this);
            h.initialize();
            return h;
        }

    }
}

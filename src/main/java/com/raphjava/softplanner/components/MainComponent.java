package com.raphjava.softplanner.components;

import com.raphjava.softplanner.annotations.Named;
import com.raphjava.softplanner.components.interfaces.Factory;
import com.raphjava.softplanner.data.models.*;
import com.raphjava.softplanner.domain.*;
import com.raphjava.softplanner.services.ConsoleOutputService;
import net.raphjava.qumbuqa.commons.trees.interfaces.TreeNode;
import net.raphjava.raphtility.collectionmanipulation.LinkedHashSet;
import net.raphjava.raphtility.collectionmanipulation.interfaces.Explorable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class MainComponent extends ComponentBase
{


    private final Help help;

    private MainComponent(Builder builder)
    {
        super(builder.baseBuilder);
        outputService = builder.outputService;
//        inputService = builder.inputService;
        projectsFactory = builder.projectsFactory;
        projectRemovalFactory = builder.projectRemovalFactory;
        projectSelection = builder.projectSelection;
        projectAccessFactory = builder.projectAccessFactory;
        projectAdditionFactory = builder.projectAdditionFactory;
        help = builder.help;
        inputProcessor = builder.inputProcessor;
        finishTagging("Main");
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }


    @SuppressWarnings("InfiniteLoopStatement")
    public void start()
    {
        debug(String.format("Starting [%s] with console interface.", this));
        while (true)
        {
            show("Enter command: (Enter \"help\" to get help)");
            inputProcessor.process("Command doesn't exist. Type 'help' to see options.");

        }
        /* while (Optional.of(

         *//*If input has data it returns it otherwise returns empty string*//*
                !input.orElse("")

                        *//*If input data is q loop stops, otherwise it continues.*//*
                        .equalsIgnoreCase("q")).orElse(true))
        {
//            System.out.println("Enter command. (Enter \"commands\" to see available commands.):");
            show("Enter command. (Enter \"commands\" to see available commands.):");
            input = inputService.getInput();
            boolean[] commandExists = new boolean[1];
            input.flatMap(this::getCommand).ifPresent(c ->
            {
                c.getAction().run();
                commandExists[0] = true;
            });

            if(!commandExists[0] && !input.orElse("").equalsIgnoreCase("q")) show("Command doesn't exist.");
        }*/
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
        loadCommandTree();

    }

    private InputProcessor inputProcessor;


    private void loadCommandTree()
    {
        addProjectCommands();
    }

    private void addProjectCommands()
    {
        TreeNode<InputProcessor.Command> projectNode = inputProcessor.newTreeItem();
        projectNode.getValue().setDescription("Project").setAction(this::projectActions);
        inputProcessor.getCommandTree().getRoot().getChildren().add(projectNode);

        TreeNode<InputProcessor.Command> listNode = inputProcessor.newTreeItem();
        listNode.getValue().setDescription("List").setAction(this::showProjects);
        projectNode.getChildren().add(listNode);

        TreeNode<InputProcessor.Command> addNode = inputProcessor.newTreeItem();
        addNode.getValue().setDescription("Add").setAction(this::addNewProject);
        projectNode.getChildren().add(addNode);

        TreeNode<InputProcessor.Command> openNode = inputProcessor.newTreeItem();
        openNode.getValue().setDescription("Open").setAction(this::openProject);
        projectNode.getChildren().add(openNode);

        TreeNode<InputProcessor.Command> currentProjectNode = inputProcessor.newTreeItem();
        currentProjectNode.getValue().setDescription("Current").setAction(this::currentOpenProject);
        projectNode.getChildren().add(currentProjectNode);

        TreeNode<InputProcessor.Command> editProjectNode = inputProcessor.newTreeItem();
        editProjectNode.getValue().setDescription("Edit").setAction(this::editProject);
        projectNode.getChildren().add(editProjectNode);

        TreeNode<InputProcessor.Command> deleteProjectNode = inputProcessor.newTreeItem();
        deleteProjectNode.getValue().setDescription("Delete").setAction(this::deleteProject);
        projectNode.getChildren().add(deleteProjectNode);

        TreeNode<InputProcessor.Command> componentNode = inputProcessor.newTreeItem();
        componentNode.getValue().setDescription("Component").setAction(this::componentActions);
        projectNode.getChildren().add(componentNode);

        TreeNode<InputProcessor.Command> helpNode = inputProcessor.newTreeItem();
        helpNode.getValue().setDescription("Help").setAction(this::projectHelpActions);
        projectNode.getChildren().add(helpNode);

        addComponentCommands(componentNode);

    }

    private void projectHelpActions(Queue<String> data)
    {
        if(data.isEmpty()) inputProcessor.getHelp().projectHelp(data);
        else show("Couldn't be able to understand arguments. Did you enter arguments that may not be needed for this command?");
    }

    private void componentActions(Queue<String> data)
    {
        inputProcessor.getChild(inputProcessor.getCommandTree().getRoot(), "project")
                .ifPresent(projectNode -> visitCommandTree(projectNode, data, "component"));

    }

    private void addComponentCommands(TreeNode<InputProcessor.Command> componentNode)
    {
        TreeNode<InputProcessor.Command> listNode = inputProcessor.newTreeItem();
        listNode.getValue().setDescription("List").setAction(this::showComponents);
        componentNode.getChildren().add(listNode);

        TreeNode<InputProcessor.Command> addNode = inputProcessor.newTreeItem();
        addNode.getValue().setDescription("Add").setAction(this::addNewComponent);
        componentNode.getChildren().add(addNode);

        TreeNode<InputProcessor.Command> openNode = inputProcessor.newTreeItem();
        openNode.getValue().setDescription("Open").setAction(this::openComponent);
        componentNode.getChildren().add(openNode);

        TreeNode<InputProcessor.Command> currentComponentNode = inputProcessor.newTreeItem();
        currentComponentNode.getValue().setDescription("Current").setAction(this::currentOpenComponent);
        componentNode.getChildren().add(currentComponentNode);

        TreeNode<InputProcessor.Command> editComponentNode = inputProcessor.newTreeItem();
        editComponentNode.getValue().setDescription("Edit").setAction(this::editComponent);
        componentNode.getChildren().add(editComponentNode);

        TreeNode<InputProcessor.Command> deleteComponentNode = inputProcessor.newTreeItem();
        deleteComponentNode.getValue().setDescription("Delete").setAction(this::deleteComponent);
        componentNode.getChildren().add(deleteComponentNode);
    }

    private void deleteComponent(Queue<String> data)
    {
        show("Deleting component...");
    }

    private void editComponent(Queue<String> data)
    {
        actOnSelectedProject(pa -> pa.editSelectedComponent(data));
    }

    private void currentOpenComponent(Queue<String> data)
    {
        actOnSelectedProject(pa -> pa.showSelectedComponent(data));
    }

    private void openComponent(Queue<String> data)
    {
        actOnSelectedProject(pa -> pa.openComponent(data));
    }

    private void addNewComponent(Queue<String> data)
    {
        actOnSelectedProject(pa -> pa.addComponentToProject(data));
    }

    private void showComponents(Queue<String> data)
    {
        actOnSelectedProject(ProjectAccess::showComponents);
    }

    private void editProject(Queue<String> data)
    {
        actOnSelectedProject(pa -> pa.editProject(data));
    }


    private void actOnSelectedProject(Consumer<ProjectAccess> projectAccessAction)
    {
        ifPresent(selectedProjectAccess, projectAccessAction).wasAbsent(() -> show("You haven't opened a project. Open one first."));
    }


    private ProjectAccess selectedProjectAccess;

    public void setSelectedProjectAccess(ProjectAccess selectedProjectAccess)
    {
        this.selectedProjectAccess = selectedProjectAccess;
    }


    private void currentOpenProject(Queue<String> data)
    {
        if (!data.isEmpty()) show("This command doesn't need any arguments.");
        actOnSelectedProject(ProjectAccess::startAsConsole);
        /*ifPresent(Optional.ofNullable(selectedProjectAccess), ProjectAccess::startAsConsole)
                .wasAbsent(() -> show("There's currently no open project."));*/

    }

    private void projectActions(Queue<String> args)
    {
        visitCommandTree(inputProcessor.getCommandTree().getRoot(), args, "project");
    }

    private void visitCommandTree(TreeNode<InputProcessor.Command> parent, Queue<String> args, String childDescription)
    {
        inputProcessor.getChild(parent, childDescription).ifPresent(projectNode -> inputProcessor
                .getChild(projectNode, args.poll()).ifPresent(projectCommand -> inputProcessor
                        .executeCommand(args, projectCommand)));
    }


//    private ConsoleInput inputService;


    private void printCommands()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("***** Available commands shown below: *****\n\n");
        Set<String> commands = new HashSet<>();
        if (!currentContent.equals(this))
        {
            currentContent.getCommands().forEach(c -> commands.add(c.getCommandDescription()));
        }
        getCommands().forEach(c -> commands.add(c.getCommandDescription()));
        commands.add("q - to quit.");
        List<String> sorted = Arrays.asList(Stream.of(commands.toArray()).sorted().toArray(String[]::new));
        sorted.forEach(n -> sb.append(n).append("\n"));
        sb.append("\n\n***** Available commands.*****\n\n\n");
        show(sb.toString());
    }


    private Factory<Projects> projectsFactory;

    private void showProjects(Queue<String> args)
    {
        if (args.isEmpty())
        {
            projectsFactory.createProduct().get(path(Project.ROOT, com.raphjava.softplanner.data.models.Component.SUB_COMPONENT_DETAIL)).ifPresent(ps ->
            {
                if (ps.isEmpty()) show("There are no existing projects.");
                else
                {
                    StringBuilder projectDescriptions = new StringBuilder();
                    ps.forEach(p -> projectDescriptions.append(String.format("%s. Project ID: %s", p.getName(), p.getId())).append("\n"));
                    show(String.format("The following are the currently saved projects:\n\n%s", projectDescriptions));
                }
            });
        }
        else show("Error. Command doesn't require any arguments.");

    }

    private Factory<ProjectRemoval> projectRemovalFactory;

    private void deleteProject(Queue<String> data)
    {
        Queue<String> deleteData = new LinkedList<>(data);
        ProjectRemoval pr = projectRemovalFactory.createProduct();
        if (pr.startAsConsole(data)) parseID(deleteData).ifPresent(id -> Optional.ofNullable(selectedProjectAccess)
                .ifPresent(op ->
                {
                    if (op.getProject().getId() == id.intValue()) setSelectedProjectAccess(null);
                }));
    }

    private ProjectSelection projectSelection;


    private Factory<ProjectAccess> projectAccessFactory;


    private Explorable<ProjectAccess> openProjects = new LinkedHashSet<>();


    private void openProject(Queue<String> args)
    {
        parseID(args)./*proceeds if id was parsed successfully.*/ifPresent(id -> ifPresent(openProjects.firstOrDefault(op ->
                id.intValue() == op.getProject().getId()), this::openProject)/*Opens project if it was opened earlier.*/
                /*Opens project for the first time.*/.wasAbsent(() -> loadProjectAccess(id.intValue())
                        /*Opens project if it exists in the repository.*/.ifPresent(this::openProject)));

    }

    private void openProject(ProjectAccess projectAccess)
    {
        setSelectedProjectAccess(projectAccess);
        projectAccess.startAsConsole();
    }

    private Optional<ProjectAccess> loadProjectAccess(int id)
    {
        final Project[] project = new Project[1];
        boolean[] failed = new boolean[1];
        dataService.read(r -> r.get(Project.class, id).eagerLoad(e -> e
                .include(path(Project.ROOT, Component.SUB_COMPONENTS, SubComponent.SUB_COMPONENT_DETAIL,
                        SubComponentDetail.COMPONENT)))
                .onSuccess(p -> project[0] = p)
                .onFailure(() ->
                {
                    failed[0] = true;
                    show("An error occurred while fetching project from the repository");
                }));

        if (project[0] != null)
        {
            ProjectAccess pa = projectAccessFactory.createProduct().setProject(project[0]);
            openProjects.add(pa);
            return Optional.of(pa);
        }
        else
        {
            if (!failed[0]) show("Project with that id doesn't exist.");
            return Optional.empty();
        }
    }

    private Optional<Double> parseID(Queue<String> args)
    {
        if (args.size() != 1)
        {
            if (args.isEmpty()) throw new IllegalArgumentException("project id data absent.");
            else throw new IllegalArgumentException("Excess arguments.");
        }

        try
        {
            return Optional.of(Double.valueOf(args.poll()));
        }
        catch (Exception e)
        {
            show(String.format("Error parsing project id data. Extra information: %s", e.getMessage()));
            return Optional.empty();
        }
    }


    private Factory<ProjectAddition> projectAdditionFactory;


    private void addNewProject(Queue<String> args)
    {
        ProjectAddition pa = projectAdditionFactory.createProduct();
        pa.addProject(args);

    }

    private ComponentBase currentContent;


    private Optional<Action> getCommand(String commandId)
    {
        String id = commandId.trim();
        Action comm = currentContent.getCommands().firstOrDefault(c -> c.getCommandDescription().equalsIgnoreCase(id));
        if (comm == null)
        {
            comm = getCommands().firstOrDefault(c -> c.getCommandDescription().equalsIgnoreCase(id));
        }
        return comm != null ? Optional.of(comm) : Optional.empty();
    }

    @Lazy
    @org.springframework.stereotype.Component
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<MainComponent>
    {

        private ComponentBase.Builder baseBuilder;
        private ConsoleInput inputService;
        private Factory<Projects> projectsFactory;
        private Factory<ProjectRemoval> projectRemovalFactory;
        private ProjectSelection projectSelection;
        private Factory<ProjectAccess> projectAccessFactory;
        private Factory<ProjectAddition> projectAdditionFactory;
        private ConsoleOutputService outputService;
        private Help help;
        private InputProcessor inputProcessor;

        @Autowired
        public Builder setInputProcessor(InputProcessor inputProcessor)
        {
            this.inputProcessor = inputProcessor;
            return this;
        }

        @Autowired
        public Builder setHelp(Help help)
        {
            this.help = help;
            return this;
        }

        private Builder()
        {
            super(MainComponent.class);
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
        public Builder projectsFactory(@Named(Projects.FACTORY) Factory<Projects> projectsFactory)
        {
            this.projectsFactory = projectsFactory;
            return this;
        }

        @Autowired
        public Builder projectRemovalFactory(@Named(ProjectRemoval.FACTORY) Factory<ProjectRemoval> projectRemovalFactory)
        {
            this.projectRemovalFactory = projectRemovalFactory;
            return this;
        }

        @Autowired
        public Builder projectSelection(ProjectSelection projectSelection)
        {
            this.projectSelection = projectSelection;
            return this;
        }

        @Autowired
        public Builder projectAccessFactory(@Named(ProjectAccess.FACTORY) Factory<ProjectAccess> projectAccessFactory)
        {
            this.projectAccessFactory = projectAccessFactory;
            return this;
        }


        @Autowired
        public Builder projectAdditionFactory(@Named(ProjectAddition.FACTORY) Factory<ProjectAddition> projectAdditionFactory)
        {
            this.projectAdditionFactory = projectAdditionFactory;
            return this;
        }

        public MainComponent build()
        {
            MainComponent ip = new MainComponent(this);
            ip.initialize();
            return ip;
        }

        @Autowired
        public Builder outputService(ConsoleOutputService outputService)
        {
            this.outputService = outputService;
            return this;
        }
    }
}

package com.raphjava.softplanner.components;

import com.raphjava.softplanner.annotations.Named;
import com.raphjava.softplanner.components.interfaces.Factory;
import com.raphjava.softplanner.data.models.Notification;
import com.raphjava.softplanner.data.models.Project;
import com.raphjava.softplanner.domain.*;
import com.raphjava.softplanner.services.ConsoleOutputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;
import java.util.zip.CheckedOutputStream;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class MainComponent extends ComponentBase
{


    private MainComponent(Builder builder)
    {
        super(builder.baseBuilder);
        outputService = builder.outputService;
        inputService = builder.inputService;
        projectsFactory = builder.projectsFactory;
        projectRemovalFactory = builder.projectRemovalFactory;
        projectSelection = builder.projectSelection;
        projectAccessFactory = builder.projectAccessFactory;
        projectAdditionFactory = builder.projectAdditionFactory;
        TAG = String.format("%s-%s", SoftPlannerConsole.class.getSimpleName(), "Main");
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
        }
        sendMessage(Notification.CleanUp);
        show("Exiting application");
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
        System.out.println(sb.toString());
    }


    private void loadCommands()
    {
        Action printCommands = actionFactory.createProduct();
        printCommands.setCommandDescription("Commands");
        printCommands.setAction(this::printCommands);
        getCommands().add(printCommands);

        Action addNewProject = actionFactory.createProduct();
        addNewProject.setCommandDescription("Add New Project");
        addNewProject.setAction(this::addNewProject);
        getCommands().add(addNewProject);

        Action openProject = actionFactory.createProduct();
        openProject.setCommandDescription("Open Project");
        openProject.setAction(this::openProject);
        getCommands().add(openProject);

        Action deleteProject = actionFactory.createProduct();
        deleteProject.setCommandDescription("Delete Project");
        deleteProject.setAction(this::deleteProject);
        getCommands().add(deleteProject);

        Action showProjects = actionFactory.createProduct();
        showProjects.setCommandDescription("Show projects");
        showProjects.setAction(this::showProjects);
        getCommands().add(showProjects);

    }

    private Factory<Projects> projectsFactory;

    private void showProjects()
    {
        projectsFactory.createProduct().get(path(Project.ROOT, com.raphjava.softplanner.data.models.Component.SUB_COMPONENT_DETAIL)).ifPresent(ps ->
        {
            if(ps.isEmpty()) System.out.println("There are no existing projects.");
            else
            {
                StringBuilder projectDescriptions = new StringBuilder();
                ps.forEach(p -> projectDescriptions.append(String.format("%s. Project ID: %s", p.getName(), p.getId())).append("\n"));
                System.out.println(String.format("The following are the currently saved projects:\n\n%s", projectDescriptions));
            }
        });

    }

    private Factory<ProjectRemoval> projectRemovalFactory;

    private void deleteProject()
    {
        ProjectRemoval pr = projectRemovalFactory.createProduct();
        pr.startAsConsole();
    }

    private ProjectSelection projectSelection;
    private Factory<ProjectAccess> projectAccessFactory;

    private void openProject()
    {
        projectSelection.setSelectionPurpose("open");
        projectSelection.startAsConsole().ifPresent(p ->
        {
            ProjectAccess pa = projectAccessFactory.createProduct();
            pa.setProject(p);
            setCurrentContent(pa);
            pa.startAsConsole();
        });


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
        String id = commandId.trim();
        Action comm = currentContent.getCommands().firstOrDefault(c -> c.getCommandDescription().equalsIgnoreCase(id));
        if (comm == null)
        {
            comm = getCommands().firstOrDefault(c -> c.getCommandDescription().equalsIgnoreCase(id));
        }
        return comm != null ? Optional.of(comm) : Optional.empty();
    }

    @Lazy
    @Component
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

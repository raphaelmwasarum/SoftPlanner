package com.raphjava.softplanner.components;

import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.ComponentDetail;
import com.raphjava.softplanner.data.models.Project;
import net.raphjava.raphtility.collectionmanipulation.ArrayList;
import net.raphjava.raphtility.collectionmanipulation.interfaces.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ProjectAddition extends ComponentBase
{


    private ProjectAddition(Builder builder)
    {
        super(builder.baseBuilder);
        inputService = builder.inputService;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public static final String FACTORY = "projectAdditionFactory";

    private String projectDataTemplate = "[Project name-Soft Planner], [Description-Helps in the planning the development of an app]";

    private ConsoleInput inputService;


    public void startAsConsole()
    {
        System.out.println(String.format("Enter new project details in the following format: %s", projectDataTemplate));
        String data = inputService.getInput();
        processData(data).ifPresent(this::addNewProject);

    }

    private void addNewProject(Project project)
    {

        System.out.println(String.format("Adding new project with the following details: Project name: %s. " +
                "Project description: %s. Please wait...", project.getName(), project.getRoot().getDetail().getDescription()));

        dataService.write(w -> w
            .add(project.getRoot().getDetail())
            .add(project.getRoot(), e -> e.include(Component.DETAIL))
            .add(project, e -> e.include(Project.ROOT))
            .commit()
            .onSuccess(() ->
            {
                dataService.read(r -> r
                    .get(Project.class, e -> e.equation().path("id").constant(project.getId()))
                        .eagerLoad(l -> l.include(path(Project.ROOT, Component.DETAIL)))
                        .onSuccess(ps ->
                        {
                            String failureMessage = "Adding of project failed.";
                            if(ps.isEmpty()) System.out.println(failureMessage);
                            else
                            {
                                Project p = ps.iterator().next();
                                if(p == null) System.out.println(failureMessage);
                            }

                        }));
                System.out.println("Adding of project successful.");
            })
            .onFailure(() -> System.out.println("Adding of project failed.")));

        System.out.println("Adding new project complete.");


    }



    private List<String> splitToProperties(String rawData)
    {
        return split(rawData, ",");

    }

    private List<String> split(String data, String delimiter)
    {
        Collection<String> rawResults = Arrays.asList(data.split(delimiter));
        return asExp(rawResults).where(s -> !s.isEmpty()).list();
    }


    private Optional<Project> processData(String rawData)
    {
        //Validate data.
        //Parse data.
        //instantiate new project object.

        ArrayList<Boolean> validData = new ArrayList<>();
        List<String> propertyData = splitToProperties(rawData);

        Project project = new Project();
        project.setId(getKey());

        com.raphjava.softplanner.data.models.Component root = new com.raphjava.softplanner.data.models.Component();
        root.setId(getKey());

        project.setRoot(root);

        ComponentDetail rootDetail = new ComponentDetail();
        rootDetail.setId(getKey());
        root.setDetail(rootDetail);
        rootDetail.setComponent(root);

        ArrayList<String> errorMessages = new ArrayList<>();
        boolean nameIsValid = setName(propertyData, project);
        validData.add(nameIsValid);

        if (!nameIsValid) errorMessages.add("Check " + PROJECT_NAME_DATA_KEY);
        Boolean middleNameIsValid = setDescription(propertyData, root);
        validData.add(middleNameIsValid);

        if (validData.all(b -> b)) return Optional.of(project);
        else
        {
            System.out.println("Parsing and processing of student data failed.");
            errorMessages.forEach(System.out::println);
            return Optional.empty();
        }

    }

    private final static String PROJECT_DESCRIPTION_DATA_KEY = "Description";

    private Boolean setDescription(List<String> propertyData, com.raphjava.softplanner.data.models.Component root)
    {
        String data = propertyData.firstOrDefault(d -> d.contains(PROJECT_DESCRIPTION_DATA_KEY));
        List<String> sData = split(data, "-");
        String rawFirstName = sData.firstOrDefault(d -> !d.contains(PROJECT_DESCRIPTION_DATA_KEY));
        String fn = rawFirstName.replace("]", "");
        if (fn.isEmpty()) return false;
        fn = fn.trim();
        root.getDetail().setDescription(fn);
        return true;

    }

    private String PROJECT_NAME_DATA_KEY = "Project name";

    private boolean setName(List<String> propertyData, Project project)
    {
        String data = propertyData.firstOrDefault(d -> d.contains(PROJECT_NAME_DATA_KEY));
        List<String> sData = split(data, "-");
        String rawFirstName = sData.firstOrDefault(d -> !d.contains(PROJECT_NAME_DATA_KEY));
        String fn = rawFirstName.replace("]", "");
        if (fn.isEmpty()) return false;
        fn = fn.trim();
        project.setName(fn);
        project.getRoot().getDetail().setName(fn);
        return true;

    }

    @Lazy
    @org.springframework.stereotype.Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ProjectAddition>
    {
        private ComponentBase.Builder baseBuilder;
        private ConsoleInput inputService;

        private Builder()
        {
            super(ProjectAddition.class);
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

        public ProjectAddition build()
        {
            return new ProjectAddition(this);
        }
    }
}

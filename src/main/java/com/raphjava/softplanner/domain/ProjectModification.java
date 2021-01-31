package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.Action;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.components.ConsoleInput;
import com.raphjava.softplanner.data.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Queue;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ProjectModification extends ComponentBase
{

    private ConsoleInput inputProcessor;

    private ProjectModification(Builder builder)
    {
        super(builder.baseBuilder);
        inputProcessor = builder.inputProcessor;
        projectDataParser = builder.projectDataParser;
        projectSelection = builder.projectSelection;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    private void initialization()
    {
        Action anp = actionFactory.createProduct();
        anp.setCommandDescription("Edit project");
        anp.setAction(this::startAsConsole);
        getCommands().add(anp);
    }

    private Project project;

    public void setProject(Project project)
    {
        this.project = project;
    }

    private String projectDataTemplate = "[Project name-Soft Planner], [Description-Helps in the planning the development of an app]";

    private ProjectDataParser projectDataParser;

    private ProjectSelection projectSelection;

    public boolean startAsConsole()
    {
        boolean[] success = new boolean[1];
        if(project == null)
        {
            projectSelection.setSelectionPurpose("edit");
            projectSelection.startAsConsole().ifPresent(p ->
            {
                setProject(p);
                System.out.println(String.format("Enter new project details in the following format: %s", projectDataTemplate));
                inputProcessor.getInput().flatMap(projectDataParser::processData).ifPresent(pr -> success[0] = editProject(pr));
            });
        }
        else
        {
            System.out.println(String.format("Enter new project details in the following format: %s", projectDataTemplate));
            inputProcessor.getInput().flatMap(projectDataParser::processData).ifPresent(pr -> success[0] = editProject(pr));
        }

        return success[0];
    }

    public boolean editProject(Queue<String> data)
    {
        if(data.isEmpty()) throw new IllegalArgumentException("You didn't enter any new project data.");
        else if(data.size() > 1)
        {
            throw new IllegalArgumentException("You've entered excess documents.");
        }
        else
        {
            boolean[] success = new boolean[1];
            projectDataParser.processData(data.poll()).ifPresent(pr -> success[0] = editProject(pr));
            return success[0];
        }

    }

    private boolean editProject(Project newData)
    {
        boolean[] success = new boolean[1];
        project.setName(newData.getName());
        project.getRoot().setDescription(newData.getRoot().getDescription());
        project.getRoot().setPseudoCode(newData.getRoot().getPseudoCode());
        System.out.println(String.format("Editing project with the following details: Project name: %s. " +
                "Project description: %s. Please wait...", newData.getName(), newData.getRoot().getDescription()));

        dataService.write(w -> w
//                .update(project.getRoot().getSubComponentDetail())
                .update(project.getRoot()/*, e -> e.include(com.raphjava.softplanner.data.models.Component.SUB_COMPONENT_DETAIL)*/)
                .update(project, e -> e.include(Project.ROOT))
                .commit()
                .onSuccess(() ->
                {
                    dataService.read(r -> r
                            .get(Project.class, e -> e.equation().path("id").constant(project.getId()))
                            .eagerLoad(l -> l.include(path(Project.ROOT, com.raphjava.softplanner.data.models.Component.SUB_COMPONENT_DETAIL)))
                            .onSuccess(ps ->
                            {
                                success[0] = true;
                                String failureMessage = "Updating of project failed.";
                                if(ps.isEmpty())
                                {
                                    System.out.println(failureMessage);
                                    success[0] = false;
                                }
                                else
                                {
                                    Project p = ps.iterator().next();
                                    if(p == null)
                                    {
                                        System.out.println(failureMessage);
                                        success[0] = false;
                                    }
                                    else if(p.getId() != project.getId())
                                    {
                                        System.out.println(failureMessage + " the repository copy is not equal to the domain copy.");
                                        success[0] = false;
                                    }
                                    //You may want to make this code more robust in future.
                                }

                            }));
                    if(success[0]) System.out.println("Updating of project successful.");

                })
                .onFailure(() ->
                {
                    System.out.println("Updating of project failed.");
                    success[0] = false;
                }));

//        System.out.println("Adding new project complete.");
        return success[0];
    }

    public final static String FACTORY = "projectModificationFactory";


    @Lazy
    @Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ProjectModification>
    {

        private ComponentBase.Builder baseBuilder;

        private ConsoleInput inputProcessor;
        private ProjectDataParser projectDataParser;
        private ProjectSelection projectSelection;

        private Builder()
        {
            super(ProjectModification.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        @Autowired
        public Builder inputProcessor(ConsoleInput inputProcessor)
        {
            this.inputProcessor = inputProcessor;
            return this;
        }

        @Autowired
        public Builder projectDataParser(ProjectDataParser projectDataParser)
        {
            this.projectDataParser = projectDataParser;
            return this;
        }

        @Autowired
        public Builder projectSelection(ProjectSelection projectSelection)
        {
            this.projectSelection = projectSelection;
            return this;
        }

        public synchronized ProjectModification build()
        {
            ProjectModification pm = new ProjectModification(this);
            pm.initialization();
            return pm;
        }

    }
}

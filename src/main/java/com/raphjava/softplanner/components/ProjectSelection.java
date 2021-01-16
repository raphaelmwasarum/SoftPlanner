package com.raphjava.softplanner.components;

import com.raphjava.softplanner.data.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ProjectSelection extends ComponentBase
{

    private ProjectSelection(Builder builder)
    {
        super(builder.baseBuilder);
        inputProcessor = builder.inputProcessor;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    private ConsoleInput inputProcessor;

    private String selectionPurpose;

    public void setSelectionPurpose(String selectionPurpose)
    {
        this.selectionPurpose = selectionPurpose;
    }

    public Optional<Project> startAsConsole()
    {
        System.out.println("Fetching saved projects. Please wait...");
        dataService.read(r -> r
                .getAll(Project.class)
                .eagerLoad(e -> e.include(path(Project.ROOT, com.raphjava.softplanner.data.models.Component.DETAIL)))
                .onSuccess(ps ->
                {
                    if(ps.isEmpty()) System.out.println("There are no existing projects.");
                    else
                    {
                        StringBuilder projectDescriptions = new StringBuilder();
                        ps.forEach(p -> projectDescriptions.append(String.format("%s. Project ID: %s", p.getName(), p.getId())).append("\n"));
                        System.out.println(String.format("The following are the existing projects: %s. \n\nEnter the id of the project you want to %s: ", projectDescriptions, selectionPurpose));
                        inputProcessor.getInput().ifPresent(i ->
                        {
                            Project sp = asExp(ps).firstOrDefault(p -> String.valueOf(p.getId()).equals(i.trim()));
                            if(sp == null) System.out.println("Error in selection");
                            else setSelectedProject(sp);
                        });
                    }
                })
                .onFailure(() -> System.out.println("Failure fetching saved projects. See log for details.")));

        Optional<Project> rez = selectedProject != null ? Optional.of(selectedProject) : Optional.empty();
        setSelectedProject(null);
        return rez;
    }

    private Project selectedProject;

    public void setSelectedProject(Project selectedProject)
    {
        this.selectedProject = selectedProject;
    }

    public static final String FACTORY = "ProjectSelectionFactory";


    @Lazy
    @Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ProjectSelection>
    {
        private ComponentBase.Builder baseBuilder;
        private ConsoleInput inputProcessor;

        private Builder()
        {
            super(ProjectSelection.class);
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

        public ProjectSelection build()
        {
            return new ProjectSelection(this);
        }
    }
}

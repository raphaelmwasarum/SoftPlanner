package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.annotations.Named;
import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.components.ConsoleInput;
import com.raphjava.softplanner.components.interfaces.Factory;
import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.Project;
import com.raphjava.softplanner.data.models.SubComponent;
import com.raphjava.softplanner.data.models.SubComponentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.Optional;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ProjectSelection extends ComponentBase
{

    private ProjectSelection(Builder builder)
    {
        super(builder.baseBuilder);
        inputProcessor = builder.inputProcessor;
        projectsFactory = builder.projectsFactory;
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

    private Factory<Projects> projectsFactory;

    public Optional<Project> startAsConsole()
    {
        show("Fetching saved projects. Please wait...");
        projectsFactory.createProduct().get(path(Project.ROOT, Component.SUB_COMPONENTS, SubComponent.SUB_COMPONENT_DETAIL, SubComponentDetail.COMPONENT))
                .ifPresent(ps ->
                {
                    if (ps.isEmpty()) show("There are no existing projects.");
                    else
                    {
                        StringBuilder projectDescriptions = new StringBuilder();
                        ps.forEach(p -> projectDescriptions.append(String.format("%s. Project ID: %s", p.getName(), p.getId())).append("\n"));
                        show(String.format("The following are the existing projects: %s. \n\nEnter the id of the project you want to %s: ", projectDescriptions, selectionPurpose));
                        inputProcessor.getInput().ifPresent(i ->
                        {
                            Project sp = asExp(ps).firstOrDefault(p -> String.valueOf(p.getId()).equals(i.trim()));
                            if (sp == null) show("Error in selection");
                            else setSelectedProject(sp);
                        });
                    }
                });

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
    @org.springframework.stereotype.Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ProjectSelection>
    {
        private ComponentBase.Builder baseBuilder;
        private ConsoleInput inputProcessor;
        private Factory<Projects> projectsFactory;

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

        @Autowired
        public Builder projectsFactory(@Named(Projects.FACTORY) Factory<Projects> projectsFactory)
        {
            this.projectsFactory = projectsFactory;
            return this;
        }

        public ProjectSelection build()
        {
            return new ProjectSelection(this);
        }
    }
}

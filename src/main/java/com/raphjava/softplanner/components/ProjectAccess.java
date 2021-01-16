package com.raphjava.softplanner.components;

import com.raphjava.softplanner.data.models.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ProjectAccess extends ComponentBase
{

    private Project project;
    public void setProject(Project project)
    {
        this.project = project;
    }

    public Project getProject()
    {
        return project;
    }

    private ProjectAccess(Builder builder)
    {
        super(builder.baseBuilder);
        projectModification = builder.projectModification;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }



    private void initialize()
    {
        Action anp = actionFactory.createProduct();
        anp.setCommandDescription("Edit project");
        anp.setAction(this::editProject);
        getCommands().add(anp);
    }

    private ProjectModification projectModification;


    private void editProject()
    {
        projectModification.setProject(project);
        if (projectModification.startAsConsole())
        {
            dataService.read(r -> r.get(Project.class, project.getId())
                    .eagerLoad(e -> e.include(path(Project.ROOT, com.raphjava.softplanner.data.models.Component.DETAIL)))
                    .onSuccess(p ->
                    {
                        setProject(p);
                        startAsConsole();
                    })
                    .onFailure(() -> System.out.println(String.format("Failure refreshing project data in %s", this))));
        }
    }

    public void startAsConsole()
    {
        ensureProjectLoaded();
        System.out.println(String.format("Project with the following details is now open: Project name: %s. " +
                "Project description: %s", project.getName(), project.getRoot().getDetail().getDescription()));

    }

    private void ensureProjectLoaded()
    {
        Objects.requireNonNull(project, "Project to be accessed has not been set.");
    }

    public static final String FACTORY = "projectAccessFactory";

    @Lazy
    @Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ProjectAccess>
    {


        private ComponentBase.Builder baseBuilder;
        private ProjectModification projectModification;

        private Builder()
        {
            super(ProjectAccess.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        @Autowired
        public Builder projectModification(ProjectModification projectModification)
        {
            this.projectModification = projectModification;
            return this;
        }

        public synchronized ProjectAccess build()
        {
            ProjectAccess pa = new ProjectAccess(this);
            pa.initialize();
            return pa;
        }

    }
}

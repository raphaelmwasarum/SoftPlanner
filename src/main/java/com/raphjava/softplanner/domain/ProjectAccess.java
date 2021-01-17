package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.annotations.Named;
import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.Action;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.components.interfaces.Factory;
import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.Project;
import com.raphjava.softplanner.data.models.SubComponent;
import com.raphjava.softplanner.data.models.SubComponentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.Collection;
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
        componentAdditionFactory = builder.componentAdditionFactory;
        projectRemovalFactory = builder.projectRemovalFactory;
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

        Action deleteProject = actionFactory.createProduct();
        deleteProject.setCommandDescription("Delete Project");
        deleteProject.setAction(this::deleteProject);
        getCommands().add(deleteProject);

        Action addComponent = actionFactory.createProduct();
        addComponent.setCommandDescription("Add Component To Project");
        addComponent.setAction(this::addComponentToProject);
        getCommands().add(addComponent);

        Action showComponents = actionFactory.createProduct();
        showComponents.setCommandDescription("Show Project Components");
        showComponents.setAction(this::showComponents);
        getCommands().add(showComponents);

        myEntities.addAll(Arrays.asList(Project.class, Component.class, SubComponent.class, SubComponentDetail.class));
    }

    @Override
    protected void handleRepositoryChanges(Collection<Class> changedEntities)
    {
        super.handleRepositoryChanges(changedEntities);
        System.out.println("Refreshing current project data in line with recent repository changes. Please wait...");
        dataService.read(r -> r.get(Project.class, project.getId()).eagerLoad(e -> e.include(path(Project.ROOT,
                Component.SUB_COMPONENTS, SubComponent.SUB_COMPONENT_DETAIL, SubComponentDetail.COMPONENT)))
                .onSuccess(project1 ->
                {
                    setProject(project1);
                    System.out.println("Project refresh action successful.");
                })
                .onFailure(() -> System.out.println("Failed to refresh project. Current project details may not be" +
                        " in sync with repository state. You may need to restart app.")));

    }

    private void showComponents()
    {
        StringBuilder sb = new StringBuilder("\nProject's components:\n\n");
        project.getRoot().getSubComponents().forEach(sc ->
        {
            com.raphjava.softplanner.data.models.Component x = sc.getSubComponentDetail().getComponent();
            sb.append(String.format("%s. ID: %s", x.getName(), x.getId())).append("\n\n");
        });
        sb.append("Project's components end of list.");
        System.out.println(sb.toString());

    }

    private Factory<ComponentAddition> componentAdditionFactory;


    private void addComponentToProject()
    {
        System.out.println("Adding component to project...");
        ComponentAddition ca = componentAdditionFactory.createProduct();
        ca.setParent(project.getRoot());
        ca.startAsConsole();
    }


    private Factory<ProjectRemoval> projectRemovalFactory;


    private void deleteProject()
    {
        ProjectRemoval pr = projectRemovalFactory.createProduct();
        pr.setProject(project);
        pr.startAsConsole();
    }

    private ProjectModification projectModification;


    private void editProject()
    {
        projectModification.setProject(project);
        if (projectModification.startAsConsole())
        {
            dataService.read(r -> r.get(Project.class, project.getId())
                    .eagerLoad(e -> e.include(path(Project.ROOT, com.raphjava.softplanner.data.models.Component.SUB_COMPONENT_DETAIL)))
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
                "Project description: %s", project.getName(), project.getRoot().getDescription()));

    }

    private void ensureProjectLoaded()
    {
        Objects.requireNonNull(project, "Project to be accessed has not been set.");
    }

    public static final String FACTORY = "projectAccessFactory";

    @Lazy
    @org.springframework.stereotype.Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ProjectAccess>
    {


        private ComponentBase.Builder baseBuilder;
        private Factory<ComponentAddition> componentAdditionFactory;
        private Factory<ProjectRemoval> projectRemovalFactory;
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
        public Builder componentAdditionFactory(@Named(ComponentAddition.FACTORY) Factory<ComponentAddition> componentAdditionFactory)
        {
            this.componentAdditionFactory = componentAdditionFactory;
            return this;
        }

        @Autowired
        public Builder projectRemovalFactory(@Named(ProjectRemoval.FACTORY) Factory<ProjectRemoval> projectRemovalFactory)
        {
            this.projectRemovalFactory = projectRemovalFactory;
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

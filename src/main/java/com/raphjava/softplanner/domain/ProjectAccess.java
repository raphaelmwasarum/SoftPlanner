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

import java.util.*;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ProjectAccess extends ComponentBase
{

    private Project project;

    public ProjectAccess setProject(Project project)
    {
        this.project = project;
        return this;
    }

    public Project getProject()
    {
        return project;
    }

    private ProjectAccess(Builder builder)
    {
        super(builder.baseBuilder);
        componentSelectionFactory = builder.componentSelectionFactory;
        componentAccessFactory = builder.componentAccessFactory;
        componentAdditionFactory = builder.componentAdditionFactory;
        projectRemovalFactory = builder.projectRemovalFactory;
        projectModification = builder.projectModification;
        finishTagging(getClass().getSimpleName());
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }


    private void initialize()
    {
        loadCommands();
        myEntities.addAll(Arrays.asList(Project.class, Component.class, SubComponent.class, SubComponentDetail.class));
    }

    private void loadCommands()
    {
        /*Action anp = actionFactory.createProduct();
        anp.setCommandDescription("Edit project");
        anp.setAction(this::editProject);
        getCommands().add(anp);*/

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

        Action openComponent = actionFactory.createProduct();
        openComponent.setCommandDescription("Open Component");
        openComponent.setAction(this::openComponent);
        getCommands().add(openComponent);
    }

    private Factory<ComponentSelection> componentSelectionFactory;

    private Factory<ComponentAccess> componentAccessFactory;

    private Map<Component, ComponentAccess> openComponents = new HashMap<>();

    private void openComponent()
    {
        componentSelectionFactory.createProduct().setComponent(project.getRoot()).setSelectionPurpose("open")
                .startAsConsole().ifPresent(c -> openComponents.computeIfAbsent(c, key -> componentAccessFactory
                .createProduct().setComponent(key)).startAsConsole());
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
            Component x = sc.getSubComponentDetail().getComponent();
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

    public void editProject(Queue<String> data)
    {
        projectModification.setProject(project);
        if (projectModification.editProject(data))
        {
            dataService.read(r -> r.get(Project.class, project.getId())
                    .eagerLoad(e -> e.include(path(Project.ROOT, Component.SUB_COMPONENT_DETAIL)))
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
        show(String.format("Project with the following details is now open: Project ID: %s. Project name: %s. " +
                "Project description: %s.", project.getId(), project.getName(), project.getRoot().getDescription()));

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
        private Factory<ComponentSelection> componentSelectionFactory;
        private Factory<ComponentAccess> componentAccessFactory;
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
        public Builder componentSelectionFactory(@Named(ComponentSelection.FACTORY) Factory<ComponentSelection> componentSelectionFactory)
        {
            this.componentSelectionFactory = componentSelectionFactory;
            return this;
        }

        @Autowired
        public Builder componentAccessFactory(@Named(ComponentAccess.FACTORY) Factory<ComponentAccess> componentAccessFactory)
        {
            this.componentAccessFactory = componentAccessFactory;
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

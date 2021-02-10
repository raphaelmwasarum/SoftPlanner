package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.annotations.Named;
import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.Action;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.components.TreeObjectVisitor;
import com.raphjava.softplanner.components.interfaces.Factory;
import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.Project;
import com.raphjava.softplanner.data.models.SubComponent;
import com.raphjava.softplanner.data.models.SubComponentDetail;
import net.raphjava.raphtility.interfaceImplementations.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.*;
import java.util.function.Consumer;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ProjectAccess extends ComponentBase
{


    private Project project;

    private Property<Project> projectProperty;

    public Property<Project> projectProperty()
    {
        if (projectProperty == null) projectProperty = setupProperty((nv, o, n) -> project = n, project);
        return projectProperty;
    }

    public ProjectAccess setProject(Project project)
    {
        if (set(projectProperty, project)) return this;
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
        bind(projectProperty()).onChange(this::handleProjectPropertyChanges);
    }

    private void handleProjectPropertyChanges(Project n)
    {
        if (n == null)
            throwNotImplementedEx("New project is null. Haven't handled this possibility yet. Time to do it now.");
        else
        {
            ifPresent(n.getRoot(), root ->
            {
                ComponentAccess ca = componentAccessFactory.createProduct();
                ca.setProjectRoot(true);
                ca.setComponent(root);
                setSelectedComponent(refreshSelectedComponent(root));

            }).wasAbsent(() -> show("Illegal state. New project does not have a root."));

        }

    }

    private ComponentAccess refreshSelectedComponent(Component root)
    {
        if (selectedComponent == null)
        {
            return componentAccessFactory.createProduct().setComponent(root).setProjectRoot(true);
        }

        final ComponentAccess[] sc = new ComponentAccess[1];
        final TreeObjectVisitor[] visitor = new TreeObjectVisitor[1];
        visitor[0] = TreeObjectVisitor.<Component, Collection<Component>>newBuilder().root(root).itemAction(n ->
        {
            if (n.equals(selectedComponent.getComponent()))
            {
                sc[0] = componentAccessFactory.createProduct().setComponent(n);
                visitor[0].setStopVisitation(true);
            }
        }).childrenGetter(nd -> asExp(nd.getSubComponents()).select(scm -> scm.getSubComponentDetail().getComponent()).list())
                .build();
        visitor[0].visit();
        show(String.format("\n\tOld selected component: [%s].\n\tNew selected: [%s].", selectedComponent.describe(), sc[0].describe()));
        return sc[0];
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

      /*  Action addComponent = actionFactory.createProduct();
        addComponent.setCommandDescription("Add Component To Project");
        addComponent.setAction(this::addComponentToProject);
        getCommands().add(addComponent);*/

        Action showComponents = actionFactory.createProduct();
        showComponents.setCommandDescription("Show Project Components");
        showComponents.setAction(this::showComponents);
        getCommands().add(showComponents);

        /*Action openComponent = actionFactory.createProduct();
        openComponent.setCommandDescription("Open Component");
        openComponent.setAction(this::openComponent);
        getCommands().add(openComponent);*/
    }

    private Factory<ComponentSelection> componentSelectionFactory;

    private Factory<ComponentAccess> componentAccessFactory;

    private Map<Component, ComponentAccess> openComponents = new HashMap<>();

    public void openComponent(Queue<String> data)
    {
        actOnSelectedComponent(sc ->
        {
            ifPresent(parseID(data), d -> ifPresent(asExp(sc.getComponent().getSubComponents()).firstOrDefault(suc ->
                    suc.getSubComponentDetail().getComponent().getId() == d.intValue()), childComponent ->
            {
                ComponentAccess ca = openComponents.computeIfAbsent(childComponent.getSubComponentDetail().getComponent()
                        , key -> componentAccessFactory.createProduct().setComponent(key));
                setSelectedComponent(ca);
                actOnSelectedComponent(newSc -> show(String.format("Newly selected component: %s.", newSc.describe())));

            }).wasAbsent(() -> show("The selected component does not have any child component of that id")))
                    .wasAbsent(() -> show("Error parsing component id data"));

           /* componentSelectionFactory.createProduct().setComponent(project.getRoot()).setSelectionPurpose("open")
                    .startAsConsole().ifPresent(c -> openComponents.computeIfAbsent(c, key -> componentAccessFactory
                    .createProduct().setComponent(key)).startAsConsole());*/
        });
    }

    private Double parseID(Queue<String> args)
    {
        if (args.size() != 1)
        {
            if (args.isEmpty()) throw new IllegalArgumentException("Component id data absent.");
            else throw new IllegalArgumentException("Excess arguments.");
        }

        try
        {
            return Double.valueOf(args.poll());
        }
        catch (Exception e)
        {
            show(String.format("Error parsing component id data. Extra information: %s", e.getMessage()));
            return null;
        }
    }


    @Override
    protected void handleRepositoryChanges(Collection<Class> changedEntities)
    {
        super.handleRepositoryChanges(changedEntities);
        show("Refreshing current project data in line with recent repository changes. Please wait...");
        dataService.read(r -> r.get(Project.class, project.getId()).eagerLoad(e -> e.include(path(Project.ROOT,
                Component.SUB_COMPONENTS, SubComponent.SUB_COMPONENT_DETAIL, SubComponentDetail.COMPONENT)))
                .onSuccess(project1 ->
                {
                    setProject(project1);
                    show("Project refresh action successful.");
                })
                .onFailure(() -> show("Failed to refresh project. Current project details may not be" +
                        " in sync with repository state. You may need to restart app.")));

    }

    public void showComponents()
    {
        actOnSelectedComponent(sc ->
        {

            if (sc.getComponent().getSubComponents().isEmpty())
            {
                show("Selected component has no child components.");
                return;
            }
            StringBuilder sb = new StringBuilder("\nSelected component's child components:\n\n");
            sc.getComponent().getSubComponents().forEach(suc ->
            {
                Component x = suc.getSubComponentDetail().getComponent();
                sb.append(String.format("%s. ID: %s", x.getName(), x.getId())).append("\n\n");
            });
            sb.append("Selected component's child components end of list.");
            show(sb.toString());
        });

    }

    private Factory<ComponentAddition> componentAdditionFactory;

    private ComponentAccess selectedComponent;

    public void setSelectedComponent(ComponentAccess selectedComponent)
    {
        this.selectedComponent = selectedComponent;
    }

    private void actOnSelectedComponent(Consumer<ComponentAccess> selectedComponentAction)
    {
        ifPresent(selectedComponent, selectedComponentAction).wasAbsent(() -> show("There's no " +
                "selected component. Select one first."));
    }

    public void addComponentToProject(Queue<String> data)
    {
        actOnSelectedComponent(sc ->
        {
            show("Adding component to project...");
            ComponentAddition ca = componentAdditionFactory.createProduct();
            ca.setParent(sc.getComponent());
//            ca.startAsConsole();
            ca.addComponent(data);

        });
    }


    private Factory<ProjectRemoval> projectRemovalFactory;


    private void deleteProject()
    {
       /* ProjectRemoval pr = projectRemovalFactory.createProduct();
        pr.setProject(project);
        pr.startAsConsole(data);*/
        throwNotImplementedEx();
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
                    .onFailure(() -> show(String.format("Failure refreshing project data in %s", this))));
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

    public void showSelectedComponent(Queue<String> data)
    {
        actOnSelectedComponent(sc -> show(String.format("Current component: Component name: %s. " +
                "Component description: %s.", sc.getComponent().getName(), sc.getComponent().getDescription())));
    }

    public void editSelectedComponent(Queue<String> data)
    {
        actOnSelectedComponent(sc -> sc.editComponent(data));
    }

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

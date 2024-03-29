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

public class ComponentAccess extends ComponentBase
{

    private Component component;

    public ComponentAccess setComponent(Component component)
    {
        this.component = component;
        return this;
    }

    public Component getComponent()
    {
        return component;
    }

    private ComponentAccess(Builder builder)
    {
        super(builder.baseBuilder);
        componentAdditionFactory = builder.componentAdditionFactory;
        componentRemovalFactory = builder.componentRemovalFactory;
        componentModification = builder.componentModification;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }


    private void initialize()
    {
        finishTagging(getClass().getSimpleName());
        loadCommands();
        myEntities.addAll(Arrays.asList(Project.class, Component.class, SubComponent.class, SubComponentDetail.class));
    }

    private void loadCommands()
    {
        Action addComponent = actionFactory.createProduct();
        addComponent.setCommandDescription("Add new Sub-component");
        addComponent.setAction(this::addSubComponentToComponent);
        getCommands().add(addComponent);

        Action showComponents = actionFactory.createProduct();
        showComponents.setCommandDescription("Show Component's Sub-components");
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
        componentSelectionFactory.createProduct().setComponent(component).setSelectionPurpose("open")
                .startAsConsole().ifPresent(c -> openComponents.computeIfAbsent(c, key -> componentAccessFactory
                .createProduct().setComponent(key)).startAsConsole());
    }

    @Override
    protected void handleRepositoryChanges(Collection<Class> changedEntities)
    {
        super.handleRepositoryChanges(changedEntities);
        show("Refreshing component data in line with recent repository changes. Please wait...");
       /* dataService.read(r -> r.get(Project.class, component.getId()).eagerLoad(e -> e.include(path(Project.ROOT,
                Component.SUB_COMPONENTS, SubComponent.SUB_COMPONENT_DETAIL, SubComponentDetail.COMPONENT)))
                .onSuccess(project1 ->
                {
                    setComponent(project1);
                    show("Project refresh action successful.");
                })
                .onFailure(() -> show("Failed to refresh project. Current project details may not be" +
                        " in sync with repository state. You may need to restart app.")));*/

    }

    private void showComponents()
    {
        StringBuilder sb = new StringBuilder("\nProject's components:\n\n");
        component.getSubComponents().forEach(sc ->
        {
            Component x = sc.getSubComponentDetail().getComponent();
            sb.append(String.format("%s. ID: %s", x.getName(), x.getId())).append("\n\n");
        });
        sb.append("Project's components end of list.");
        show(sb.toString());

    }

    private Factory<ComponentAddition> componentAdditionFactory;


    private void addSubComponentToComponent()
    {
        show("Adding sub-component to component...");
        ComponentAddition ca = componentAdditionFactory.createProduct();
        ca.setParent(component);
        ca.startAsConsole();
    }


    private Factory<ComponentRemoval> componentRemovalFactory;


    private void deleteComponent()
    {
        ComponentRemoval pr = componentRemovalFactory.createProduct();
        pr.setComponent(component);
        pr.startAsConsole();
    }

    private ComponentModification componentModification;

    private boolean projectRoot;

    public ComponentAccess setProjectRoot(boolean projectRoot)
    {
        this.projectRoot = projectRoot;
        return this;
    }


    void editComponent(Queue<String> data)
    {
        if(projectRoot)
        {
            show("Current component is the project root and it is not editable through this command.");/*
            The root of the project's component tree should not be editable like the other component nodes because I
            considered it an extension of the project object itself rather than a component.*/
            return;
        }

        componentModification.setComponent(component);
        if(projectRoot) componentModification.setRoot(true);
        else componentModification.setParent(component.getSubComponentDetail().getSubComponent().getParentComponent());
        if (componentModification.startAsConsole(data))
        {
            dataService.read(r -> r.get(Component.class, component.getId())
                    .onSuccess(p ->
                    {
                        setComponent(p);
                        startAsConsole();
                    })
                    .onFailure(() -> show(String.format("Failure refreshing component data in %s", this))));
        }
    }

    String describe()
    {
        return String.format("Component Access for this component: Component name: %s. " +
                "Component description: %s", component.getName(), component.getDescription());
    }


    public void startAsConsole()
    {
        ensureProjectLoaded();
        show(describe());

    }

    private void ensureProjectLoaded()
    {
        Objects.requireNonNull(component, "Component to be accessed has not been set.");
    }

    public static final String FACTORY = "componentAccessFactory";

    @Lazy
    @org.springframework.stereotype.Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ComponentAccess>
    {


        private ComponentBase.Builder baseBuilder;
        private Factory<ComponentAddition> componentAdditionFactory;
        private Factory<ComponentRemoval> componentRemovalFactory;
        private ComponentModification componentModification;

        private Builder()
        {
            super(ComponentAccess.class);
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
        public Builder projectRemovalFactory(@Named(ComponentRemoval.FACTORY) Factory<ComponentRemoval> projectRemovalFactory)
        {
            this.componentRemovalFactory = projectRemovalFactory;
            return this;
        }

        @Autowired
        public Builder projectModification(ComponentModification projectModification)
        {
            this.componentModification = projectModification;
            return this;
        }

        public synchronized ComponentAccess build()
        {
            ComponentAccess pa = new ComponentAccess(this);
            pa.initialize();
            return pa;
        }

    }
}

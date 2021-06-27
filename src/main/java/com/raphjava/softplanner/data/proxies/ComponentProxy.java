package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.Project;
import com.raphjava.softplanner.data.models.SubComponent;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public class ComponentProxy extends Component
{

    private ProxyAssistant assistant;

    private ProxyFactory_Template proxyFactory;

    private ComponentProxy(Builder builder)
    {
        this.assistant = builder.assistant;
    }

    private Supplier<Boolean> loadProjectFromModel = this::loadProjectFromModel;

    private Runnable loadProjectFromRepository = this::loadProjectFromRepository;

    private Supplier<Boolean> loadSubComponentsFromModel = this::loadSubComponentsFromModel;

    private Runnable loadSubComponentsFromRepository = this::loadSubComponentsFromRepository;

    private void loadProjectFromRepository()
    {
        Optional.ofNullable(((Component) assistant.get(getClass().getSuperclass(), getId(), false, e -> e
                .include(Component.PROJECT))))
                .flatMap(component -> Optional.ofNullable(component.getProject()))
                .ifPresent(project -> setProject(proxyFactory.proxy(project)));
    }

    private void loadSubComponentsFromRepository()
    {
        Optional.ofNullable(((Component) assistant.get(getClass().getSuperclass(), getId(), false, e -> e
                .include(Component.SUB_COMPONENTS))))
                .ifPresent(component ->
                {
                    for (SubComponent subComponent : component.getSubComponents())
                    {
                        addSubComponent(proxyFactory.proxy(subComponent));
                    }
                });
    }

    @Override
    public Collection<SubComponent> getSubComponents()
    {
        boolean[] f = new boolean[1];
        assistant.ensureLoaded(Component.SUB_COMPONENTS, f, loadSubComponentsFromModel, loadSubComponentsFromRepository);
        return super.getSubComponents();
    }


    public Collection<SubComponent> getSubComponents(boolean force)
    {
        boolean[] f = { force };
        assistant.ensureLoaded(Component.SUB_COMPONENTS, f, loadSubComponentsFromModel, loadSubComponentsFromRepository);
        return super.getSubComponents();
    }


    private Boolean loadSubComponentsFromModel()
    {
        return !getSubComponents().isEmpty();
    }

    @Override
    public Project getProject()
    {
        boolean[] f = new boolean[1];
        assistant.ensureLoaded(Component.PROJECT, f, loadProjectFromModel, loadProjectFromRepository);
        return super.getProject();

    }

    public Project getProject(boolean force)
    {
        boolean[] f = { force };
        assistant.ensureLoaded(Component.PROJECT, f, loadProjectFromModel, loadProjectFromRepository);
        return super.getProject();
    }


    //TODO write template code for a collection property and now you will be set to know how to write the generation code for a class such as this.


    private Boolean loadProjectFromModel()
    {
        return getProject() != null;
    }

    public static Builder builder()
    {
        return new Builder();
    }


    public static class Builder
    {
        private ProxyAssistant assistant;

        private Builder()
        {

        }


        public Builder setAssistant(ProxyAssistant assistant)
        {
            this.assistant = assistant;
            return this;
        }


        public ComponentProxy build()
        {
            return new ComponentProxy(this);
        }
    }
}

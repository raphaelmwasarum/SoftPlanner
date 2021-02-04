package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.Action;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.Project;
import com.raphjava.softplanner.data.models.SubComponent;
import com.raphjava.softplanner.data.models.SubComponentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.Collection;

import static com.raphjava.softplanner.annotations.Scope.Singleton;


public class ComponentRemoval extends ComponentBase
{

    private Component component;

    public void setComponent(Component component)
    {
        this.component = component;
    }

    public Component getComponent()
    {
        return component;
    }

    private ComponentRemoval(Builder builder)
    {
        super(builder.baseBuilder);
    }



    public boolean startAsConsole()
    {
        boolean[] success = new boolean[1];
        if(component == null)
        {
//            projectSelection.setSelectionPurpose("delete");
//            projectSelection.startAsConsole().ifPresent(this::setComponent);
//            success [0] = deleteComponent(component);
            throw new RuntimeException("Component field cannot be null.");
        }
        else success[0] = deleteComponent(component);

        return success[0];
    }

    private void initialize()
    {
        Action anp = actionFactory.createProduct();
        anp.setCommandDescription("Delete project");
        anp.setAction(this::startAsConsole);
        getCommands().add(anp);

    }

    private boolean deleteComponent(Component project)
    {
        boolean[] success = new boolean[1];
        dataService.write(r ->
        {
            dataService.read(re -> re.get(Project.class, project.getId())
                    .eagerLoad(e -> e.include(path(Project.ROOT, com.raphjava.softplanner.data.models.Component.SUB_COMPONENTS,
                            SubComponent.SUB_COMPONENT_DETAIL, SubComponentDetail.COMPONENT)))
                    .onSuccess(p -> p.getRoot().getSubComponents().forEach(subComponent ->
                    {
                        Collection<SubComponent> unSuccessfulDeletions = new ArrayList<>();
                        if(!deleteSubComponent(subComponent)) unSuccessfulDeletions.add(subComponent);
                        StringBuilder sb = new StringBuilder("Sub components of the following ids deletions failed: ");
                        unSuccessfulDeletions.forEach(ud -> sb.append(ud.getId()));
                        sb.append(". Manual deletions may have to be done by accessing the database directly.");
                        debug(sb.toString());
                    })));
            r.remove(project).commit().onSuccess(() ->
            {
                show("Project deleted successfully.");
                success[0] = true;
            })
                    .onFailure(() -> show("Project deletion failed.")); //TODO Test this method.
        });
        return success[0];
    }

    private boolean deleteSubComponent(SubComponent subComponent)
    {
        boolean[] success = new boolean[1];
        dataService.write(w -> w.remove(subComponent).remove(subComponent.getSubComponentDetail())
                .remove(subComponent.getSubComponentDetail().getComponent()).commit()
                .onSuccess(() -> success[0] = true).onFailure(() ->
                {
                    String m = String.format("Subcomponent of id: %s and its associated data couldn't be deleted", subComponent.getId());
                    debug(m);
                    show(m);
                    success[0] = false;
                }));
        return success[0];
    }


    public static Builder newBuilder()
    {
        return new Builder();
    }


    public static final String FACTORY = "componentRemovalFactory";

    @Lazy
    @org.springframework.stereotype.Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ComponentRemoval>
    {
        private ComponentBase.Builder baseBuilder;

        private Builder()
        {
            super(ComponentRemoval.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        public synchronized ComponentRemoval build()
        {
            ComponentRemoval pr = new ComponentRemoval(this);
            pr.initialize();
            return pr;
        }

    }
}

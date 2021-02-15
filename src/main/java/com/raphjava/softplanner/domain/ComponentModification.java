package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.components.ConsoleInput;
import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.Notification;
import com.raphjava.softplanner.data.models.SubComponent;
import com.raphjava.softplanner.data.models.SubComponentDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.util.Arrays;
import java.util.Objects;
import java.util.Queue;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ComponentModification extends ComponentBase
{

    private boolean root;

    public void setRoot(boolean root)
    {
        this.root = root;
    }

    public boolean isRoot()
    {
        return root;
    }

    private Component parent;
    private Component component;


    public void setParent(Component parent)
    {
        this.parent = parent;
    }

    public Component getParent()
    {
        return parent;
    }

    private ComponentModification(Builder builder)
    {
        super(builder.baseBuilder);
        componentDataParser = builder.componentDataParser;
        finishTagging(getClass().getSimpleName());
    }

    private ComponentDataParser componentDataParser;

    public boolean startAsConsole(Queue<String> data)
    {
        boolean[] success = new boolean[1];
        if(data.size() != 1) throw new IllegalArgumentException("Error in arguments. See help information to see how to format your arguments.");
        componentDataParser.processData(data.poll()).ifPresent(c -> success[0] = editComponent(c));
        return success[0];
    }

    private boolean editComponent(Component newData)
    {
        //TODO Continue from here. Decide whether project root should be editable as component or it should be edited when the project is edited.
        component.setName(newData.getName());
        component.setDescription(newData.getDescription());
        component.setPseudoCode(newData.getPseudoCode());
        if(!root) ensureParentExists();
        boolean[] success = new boolean[1];
        dataService.write(w ->
        {
            w.update(component);

            //Persist parent relationship
            if(!root)
            {
                //Nullify existing relationship.
                w.remove(component.getSubComponentDetail().getSubComponent())
                        .remove(component.getSubComponentDetail());

                SubComponentDetail scd = new SubComponentDetail();
                scd.setId(getKey());
                scd.setComponent(component);
                component.setSubComponentDetail(scd);
                w.add(scd, e -> e.include(SubComponentDetail.COMPONENT));
                SubComponent sc = new SubComponent();
                sc.setId(getKey());
                parent.addSubComponent(sc);
                sc.setSubComponentDetail(scd);
                w.add(sc, e -> e.include(SubComponent.PARENT_COMPONENT).include(SubComponent.SUB_COMPONENT_DETAIL))
                        .commit()
                        .onSuccess(() -> success[0] = true);
            }
            else w.commit().onSuccess(() -> success[0] = true);



        });

        if(success[0])
        {
            successFailedNotificationResult(true, "Modification of component");
            sendMessage(Notification.RepositoryHasChanged, Arrays.asList(Component.class, SubComponent.class, SubComponentDetail.class));
        }

        return success[0];
    }

    private void ensureParentExists()
    {
        Objects.requireNonNull(parent, "A sub-component must have a parent component.");
    }


    public static Builder newBuilder()
    {
        return new Builder();
    }

    public static final String FACTORY = "componentModificationFactory";

    public ComponentModification setComponent(Component component)
    {
        this.component = component;
        return this;
    }


    @Lazy
    @org.springframework.stereotype.Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ComponentModification>
    {

        private ComponentBase.Builder baseBuilder;
        private ComponentDataParser componentDataParser;
        private ConsoleInput inputService;

        private Builder()
        {
            super(ComponentModification.class);
        }


        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }


        @Autowired
        public Builder componentDataParser(ComponentDataParser componentDataParser)
        {
            this.componentDataParser = componentDataParser;
            return this;
        }


        @Autowired
        public Builder inputService(ConsoleInput inputService)
        {
            this.inputService = inputService;
            return this;
        }


        public synchronized ComponentModification build()
        {
            return new ComponentModification(this);
        }
    }
}

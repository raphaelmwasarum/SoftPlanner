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

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ComponentModification extends ComponentBase
{

    private Component parent;


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
        inputService = builder.inputService;
    }

    private ComponentDataParser componentDataParser;

    private ConsoleInput inputService;

    private String componentDataTemplate = "[Component name-Input processor], [Description-Processes input entered by the user], [PseudoCode-Enter your pseudo code here.]";

    public void startAsConsole()
    {
        System.out.println(String.format("Enter new component details in the following format: %s", componentDataTemplate));
        inputService.getInput().flatMap(componentDataParser::processData).ifPresent(this::editComponent);

    }

    private void editComponent(Component component)
    {
        ensureParentExists();
        boolean[] success = new boolean[1];
        dataService.write(w ->
        {
            w.update(component);

            //Persist parent relationship
            SubComponentDetail scd = new SubComponentDetail();
            scd.setId(getKey());
            scd.setComponent(component);
            component.setSubComponentDetail(scd);
            w.update(scd, e -> e.include(SubComponentDetail.COMPONENT));

            SubComponent sc = new SubComponent();
            sc.setId(getKey());
            parent.addSubComponent(sc);
            sc.setSubComponentDetail(scd);
            w.update(sc, e -> e.include(SubComponent.PARENT_COMPONENT).include(SubComponent.SUB_COMPONENT_DETAIL))
                    .commit()
                    .onSuccess(() -> success[0] = true);

        });

        if(success[0])
        {
            successFailedNotificationResult(true, "Modification of component");
            sendMessage(Notification.RepositoryHasChanged, Arrays.asList(Component.class, SubComponent.class, SubComponentDetail.class));
        }
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

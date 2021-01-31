package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.Action;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.data.models.Project;
import com.raphjava.softplanner.data.models.SubComponent;
import com.raphjava.softplanner.data.models.SubComponentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Queue;

import static com.raphjava.softplanner.annotations.Scope.Singleton;


public class ProjectRemoval extends ComponentBase
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

    private ProjectRemoval(Builder builder)
    {
        super(builder.baseBuilder);
        projectSelection = builder.projectSelection;
    }

    private ProjectSelection projectSelection;

    private Optional<Double> parseID(Queue<String> args)
    {
        if (args.size() != 1)
        {
            if (args.isEmpty()) throw new IllegalArgumentException("project id data absent.");
            else throw new IllegalArgumentException("Excess arguments.");
        }

        try
        {
            return Optional.of(Double.valueOf(args.poll()));
        }
        catch (Exception e)
        {
            show(String.format("Error parsing project id data. Extra information: %s", e.getMessage()));
            return Optional.empty();
        }
    }

    public boolean startAsConsole(Queue<String> data)
    {
        boolean[] success = new boolean[1];
        ifPresent(parseID(data), id -> dataService.read(r -> r.get(Project.class, id.intValue())
                .onSuccess(p -> dataService.write(w -> w.remove(p).commit()
                        .onSuccess(() ->
                        {
                            success[0] = true;
                            show(String.format("Project of id: %s successfully deleted from the repository.", id.intValue()));
                        })
                        .onFailure(() -> show(String.format("Error deleting project of id: %s from the repository.", id.intValue())))))
                .onFailure(() -> show("Error trying to see if project of id: " + id + " is in the repository before deleting it."))))
                .wasAbsent(() -> show("Error parsing id data."));

        return success[0];
    }

    private void initialize()
    {

    }

    private boolean deleteProject(Project project)
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
                        if (!deleteSubComponent(subComponent)) unSuccessfulDeletions.add(subComponent);
                        StringBuilder sb = new StringBuilder("Sub components of the following ids deletions failed: ");
                        unSuccessfulDeletions.forEach(ud -> sb.append(ud.getId()));
                        sb.append(". Manual deletions may have to be done by accessing the database directly.");
                        debug(sb.toString());
                    })));
            r.remove(project).commit().onSuccess(() ->
            {
                System.out.println("Project deleted successfully.");
                success[0] = true;
            })
                    .onFailure(() -> System.out.println("Project deletion failed.")); //TODO Test this method.
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
                    System.out.println(m);
                    success[0] = false;
                }));
        return success[0];
    }


    public static Builder newBuilder()
    {
        return new Builder();
    }


    public static final String FACTORY = "projectRemovalFactory";

    @Lazy
    @Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ProjectRemoval>
    {
        private ComponentBase.Builder baseBuilder;

        private ProjectSelection projectSelection;

        private Builder()
        {
            super(ProjectRemoval.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        @Autowired
        public Builder projectSelection(ProjectSelection projectSelection)
        {
            this.projectSelection = projectSelection;
            return this;
        }

        public synchronized ProjectRemoval build()
        {
            ProjectRemoval pr = new ProjectRemoval(this);
            pr.initialize();
            return pr;
        }

    }
}

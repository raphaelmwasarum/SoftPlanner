package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.components.ConsoleInput;
import com.raphjava.softplanner.data.models.Project;
import net.raphjava.raphtility.collectionmanipulation.ArrayList;
import net.raphjava.raphtility.collectionmanipulation.interfaces.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class ProjectDataParser extends ComponentBase
{


    private ProjectDataParser(Builder builder)
    {
        super(builder.baseBuilder);
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    private List<String> splitToProperties(String rawData)
    {
        return split(rawData, ",");

    }

    private final static String PROJECT_DESCRIPTION_DATA_KEY = "Description";

    private Boolean setDescription(List<String> propertyData, com.raphjava.softplanner.data.models.Component root)
    {
        String data = propertyData.firstOrDefault(d -> d.contains(PROJECT_DESCRIPTION_DATA_KEY));
        if(data == null) return false;
        List<String> sData = split(data, "-");
        String rawFirstName = sData.firstOrDefault(d -> !d.contains(PROJECT_DESCRIPTION_DATA_KEY));
        if(rawFirstName == null) return false;
        String fn = rawFirstName.replace("]", "");
        if (fn.isEmpty()) return false;
        fn = fn.trim();
        root.setDescription(fn);
        return true;

    }

    private String PROJECT_NAME_DATA_KEY = "Project name";

    private boolean setName(List<String> propertyData, Project project)
    {
        String data = propertyData.firstOrDefault(d -> d.contains(PROJECT_NAME_DATA_KEY));
        if(data == null) return false;
        List<String> sData = split(data, "-");
        String rawFirstName = sData.firstOrDefault(d -> !d.contains(PROJECT_NAME_DATA_KEY));
        if(rawFirstName == null) return false;
        String fn = rawFirstName.replace("]", "");
        if (fn.isEmpty()) return false;
        fn = fn.trim();
        project.setName(fn);
        project.getRoot().setName(fn);
        return true;

    }

    public Optional<Project> processData(String rawData)
    {
        //Validate data.
        //Parse data.
        //instantiate new project object.

        ArrayList<Boolean> validData = new ArrayList<>();
        List<String> propertyData = splitToProperties(rawData);

        Project project = new Project();
        project.setId(getKey());

        com.raphjava.softplanner.data.models.Component root = new com.raphjava.softplanner.data.models.Component();
        root.setId(getKey());

        project.setRoot(root);

     /*   SubComponentDetail rootDetail = new SubComponentDetail();
        rootDetail.setId(getKey());
        root.setSubComponentDetail(rootDetail);
        rootDetail.setComponent(root);*/

        ArrayList<String> errorMessages = new ArrayList<>();
        boolean nameIsValid = setName(propertyData, project);
        validData.add(nameIsValid);

        if (!nameIsValid) errorMessages.add("Check " + PROJECT_NAME_DATA_KEY);

        Boolean descriptionIsValid = setDescription(propertyData, root);
        validData.add(descriptionIsValid);

        if (!descriptionIsValid) errorMessages.add("Check " + PROJECT_DESCRIPTION_DATA_KEY);

        if (validData.all(b -> b)) return Optional.of(project);
        else
        {
            show("Parsing and processing of project data failed.");
            errorMessages.forEach(System.out::println);
            return Optional.empty();
        }

    }

    private static final String FACTORY = "projectDataParserFactory";

    @Lazy
    @Component(FACTORY)
    public static final class Builder extends AbFactoryBean<ProjectDataParser>
    {
        private ComponentBase.Builder baseBuilder;
        private ConsoleInput inputService;

        private Builder()
        {
            super(ProjectDataParser.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        @Autowired
        public Builder inputService(ConsoleInput inputService)
        {
            this.inputService = inputService;
            return this;
        }


        public synchronized ProjectDataParser build()
        {
            return new ProjectDataParser(this);
        }
    }
}

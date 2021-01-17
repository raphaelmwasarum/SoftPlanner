package com.raphjava.softplanner.domain;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.components.ComponentBase;
import com.raphjava.softplanner.components.ConsoleInput;
import com.raphjava.softplanner.data.models.SubComponentDetail;
import net.raphjava.raphtility.collectionmanipulation.ArrayList;
import net.raphjava.raphtility.collectionmanipulation.interfaces.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class ComponentDataParser extends ComponentBase
{


    private ComponentDataParser(Builder builder)
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



    private final static String PROJECT_PSEUDO_CODE_DATA_KEY = "[PseudoCode";

    private Boolean setPseudoCode(List<String> propertyData, com.raphjava.softplanner.data.models.Component root)
    {
        String data = propertyData.firstOrDefault(d -> d.contains(PROJECT_PSEUDO_CODE_DATA_KEY));
        if(data == null) return false;
        List<String> sData = split(data, "-");
        String rawFirstName = sData.firstOrDefault(d -> !d.contains(PROJECT_PSEUDO_CODE_DATA_KEY));
        if(rawFirstName == null) return false;
        String fn = rawFirstName.replace("]", "");
        if (fn.isEmpty()) return false;
        fn = fn.trim();
        root.setPseudoCode(fn);
        return true;

    }

    private final static String PROJECT_DESCRIPTION_DATA_KEY = "[Description";

    private Boolean setDescription(List<String> propertyData, com.raphjava.softplanner.data.models.Component component)
    {
        String data = propertyData.firstOrDefault(d -> d.contains(PROJECT_DESCRIPTION_DATA_KEY));
        if(data == null) return false;
        List<String> sData = split(data, "-");
        String rawFirstName = sData.firstOrDefault(d -> !d.contains(PROJECT_DESCRIPTION_DATA_KEY));
        if(rawFirstName == null) return false;
        String fn = rawFirstName.replace("]", "");
        if (fn.isEmpty()) return false;
        fn = fn.trim();
        component.setDescription(fn);
        return true;

    }

    private String COMPONENT_NAME_DATA_KEY = "[Component name";

    private boolean setName(List<String> propertyData, com.raphjava.softplanner.data.models.Component component)
    {
        String data = propertyData.firstOrDefault(d -> d.contains(COMPONENT_NAME_DATA_KEY));
        if(data == null) return false;
        List<String> sData = split(data, "-");
        String rawFirstName = sData.firstOrDefault(d -> !d.contains(COMPONENT_NAME_DATA_KEY));
        if(rawFirstName == null) return false;
        String fn = rawFirstName.replace("]", "");
        if (fn.isEmpty()) return false;
        fn = fn.trim();
        component.setName(fn);
        return true;

    }

    public Optional<com.raphjava.softplanner.data.models.Component> processData(String rawData)
    {
        //Validate data.
        //Parse data.
        //instantiate new project object.

        ArrayList<Boolean> validData = new ArrayList<>();
        List<String> propertyData = splitToProperties(rawData);

        com.raphjava.softplanner.data.models.Component component = new com.raphjava.softplanner.data.models.Component();
        component.setId(getKey());

        SubComponentDetail rootDetail = new SubComponentDetail();
        rootDetail.setId(getKey());
        component.setSubComponentDetail(rootDetail);
        rootDetail.setComponent(component);

        ArrayList<String> errorMessages = new ArrayList<>();
        boolean nameIsValid = setName(propertyData, component);
        validData.add(nameIsValid);

        if (!nameIsValid) errorMessages.add("Check " + COMPONENT_NAME_DATA_KEY);

        Boolean descriptionIsValid = setDescription(propertyData, component);
        validData.add(descriptionIsValid);

        if (!descriptionIsValid) errorMessages.add("Check " + PROJECT_DESCRIPTION_DATA_KEY);


        boolean pseudoCodeIsValid = setPseudoCode(propertyData, component);
        validData.add(pseudoCodeIsValid);

        if (!pseudoCodeIsValid) errorMessages.add("Check " + PROJECT_PSEUDO_CODE_DATA_KEY);

        if (validData.all(b -> b)) return Optional.of(component);
        else
        {
            System.out.println("Parsing and processing of component data failed.");
            errorMessages.forEach(System.out::println);
            return Optional.empty();
        }

    }

    public static final String FACTORY = "componentDataParserFactory";

    @Lazy
    @Component(FACTORY)
    @Scope(Singleton)
    public static final class Builder extends AbFactoryBean<ComponentDataParser>
    {
        private ComponentBase.Builder baseBuilder;
        private ConsoleInput inputService;

        private Builder()
        {
            super(ComponentDataParser.class);
        }

        @Autowired
        public Builder baseBuilder(ComponentBase.Builder baseBuilder)
        {
            this.baseBuilder = baseBuilder;
            return this;
        }

        public synchronized ComponentDataParser build()
        {
            return new ComponentDataParser(this);
        }
    }
}

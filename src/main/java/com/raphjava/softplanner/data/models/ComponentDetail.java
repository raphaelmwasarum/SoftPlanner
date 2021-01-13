package com.raphjava.softplanner.data.models;

public class ComponentDetail extends EntityBase
{

    public static final String SUB_COMPONENT = "subComponent";

    public static final String COMPONENT = "component";

    private String name;

    private String description;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getPseudoCode()
    {
        return pseudoCode;
    }

    public void setPseudoCode(String pseudoCode)
    {
        this.pseudoCode = pseudoCode;
    }

    private String pseudoCode;

    private Component component;

    public Component getComponent()
    {
        return component;
    }

    public void setComponent(Component component)
    {
        this.component = component;
    }

    public SubComponent getSubComponent()
    {
        return subComponent;
    }

    public void setSubComponent(SubComponent subComponent)
    {
        this.subComponent = subComponent;
    }

    private SubComponent subComponent;
}

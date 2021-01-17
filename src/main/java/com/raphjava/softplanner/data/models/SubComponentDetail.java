package com.raphjava.softplanner.data.models;

public class SubComponentDetail extends EntityBase
{

    public static final String SUB_COMPONENT = "subComponent";

    public static final String COMPONENT = "component";



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

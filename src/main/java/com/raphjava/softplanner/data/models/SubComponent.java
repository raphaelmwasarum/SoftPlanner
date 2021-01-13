package com.raphjava.softplanner.data.models;

public class SubComponent extends EntityBase
{
    public static final String DETAIL = "detail";

    public static final String PARENT_COMPONENT = "parentComponent";


    private Component parentComponent;

    private ComponentDetail detail;

    public Component getParentComponent()
    {
        return parentComponent;
    }

    public void setParentComponent(Component parentComponent)
    {
        this.parentComponent = parentComponent;
    }

    public ComponentDetail getDetail()
    {
        return detail;
    }

    public void setDetail(ComponentDetail detail)
    {
        this.detail = detail;
    }
}

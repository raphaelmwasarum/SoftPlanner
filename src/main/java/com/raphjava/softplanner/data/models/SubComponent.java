package com.raphjava.softplanner.data.models;

public class SubComponent extends EntityBase
{
    public static final String SUB_COMPONENT_DETAIL = "subComponentDetail";

    public static final String PARENT_COMPONENT = "parentComponent";


    private Component parentComponent;

    private SubComponentDetail subComponentDetail;

    public Component getParentComponent()
    {
        return parentComponent;
    }

    public void setParentComponent(Component parentComponent)
    {
        this.parentComponent = parentComponent;
    }

    public SubComponentDetail getSubComponentDetail()
    {
        return subComponentDetail;
    }

    public void setSubComponentDetail(SubComponentDetail subComponentDetail)
    {
        this.subComponentDetail = subComponentDetail;
    }
}

package com.raphjava.softplanner.data.models;

import net.raphjava.raphtility.collectionmanipulation.LinkedHashSet;

import java.util.Collection;

public class Component extends EntityBase
{

    public static final String PROJECT = "project";
    public static final String SUB_COMPONENTS = "subComponents";
    public static final String DETAIL = "detail";

    private Project project;

    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    private Collection<SubComponent> subComponents = new LinkedHashSet<>();

    private ComponentDetail detail;

    public Collection<SubComponent> getSubComponents()
    {
        return subComponents;
    }

    public void setSubComponents(Collection<SubComponent> subComponents)
    {
        this.subComponents = subComponents;
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

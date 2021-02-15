package com.raphjava.softplanner.data.models;

import com.raphjava.softplanner.data.proxies.annotations.Proxied;
import net.raphjava.raphtility.collectionmanipulation.LinkedHashSet;

import java.util.Collection;

@Proxied
public class Component extends EntityBase
{

    public static final String PROJECT = "project";
    public static final String SUB_COMPONENTS = "subComponents";
    public static final String SUB_COMPONENT_DETAIL = "subComponentDetail";

    private Project project;

    @Proxied
    public Project getProject()
    {
        return project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    private Collection<SubComponent> subComponents = new LinkedHashSet<>();

    private SubComponentDetail subComponentDetail;

    @Proxied
    public Collection<SubComponent> getSubComponents()
    {
        return subComponents;
    }

    public void setSubComponents(Collection<SubComponent> subComponents)
    {
        this.subComponents = subComponents;
    }

    public SubComponentDetail getSubComponentDetail()
    {
        return subComponentDetail;
    }

    public void setSubComponentDetail(SubComponentDetail subComponentDetail)
    {
        this.subComponentDetail = subComponentDetail;
    }

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

    public void addSubComponent(SubComponent subComponent)
    {
        subComponents.add(subComponent);
        subComponent.setParentComponent(this);
    }
}

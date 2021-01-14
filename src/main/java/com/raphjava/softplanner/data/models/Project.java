package com.raphjava.softplanner.data.models;

public class Project extends EntityBase
{

    public static final String ROOT = "root";

    private Component root;

    public Component getRoot()
    {
        return root;
    }

    public void setRoot(Component root)
    {
        this.root = root;
    }

    private String name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}

package com.raphjava.softplanner.data.models;

import com.raphjava.softplanner.data.proxies.annotations.Proxied;

@Proxied
public class Project extends EntityBase
{

    public static final String ROOT = "root";

    private Component root;

    @Proxied
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

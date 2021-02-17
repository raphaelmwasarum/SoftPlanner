package com.raphjava.softplanner.components;

import java.util.LinkedHashSet;

public class UserDirectoryResolver
{
    private String fileSeparator;

    public String getCurrentOSFileSeparator()
    {
        return fileSeparator != null ? fileSeparator : loadAppropriateFileSeparator();
    }


    private String loadAppropriateFileSeparator()
    {
        if(System.getProperty("os.name").toLowerCase().contains("windows"))
        {
            fileSeparator = "\\";
        }
        else fileSeparator = "/";
        return fileSeparator;
    }

     public String getAppRunningRootLocation()
    {
        String separator = getCurrentOSFileSeparator();
        return System.getProperty("user.dir") + separator;

    }

    public String buildPath(LinkedHashSet<String> pathLets, boolean...excludeLastSeparator)
    {
        StringBuilder sb = new StringBuilder();
        for(String pathLet : pathLets) sb.append(pathLet).append(getCurrentOSFileSeparator());
        if(excludeLastSeparator.length != 0 && excludeLastSeparator[0])
        {
            int start = sb.lastIndexOf(getCurrentOSFileSeparator());
            sb.replace(start, start + 1, "");
        }
        return sb.toString();
    }
}

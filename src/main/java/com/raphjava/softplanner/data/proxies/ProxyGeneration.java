package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.components.UserDirectoryResolver;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class ProxyGeneration
{

    public static void main(String[] args)
    {
        ProxyGenerator proxyGenerator = ProxyGenerator.newBuilder().proxyAnnotationProcessor(ProxyAnnotationProcessor
                .newBuilder().entitiesModelPackage(buildEntitiesModelPackageName()).build())
                .proxiesDirectory(buildProxyDirectoryPath()).build();

        proxyGenerator.generateProxies();

    }

    private static UserDirectoryResolver userDirResolver = new UserDirectoryResolver();


    private static String buildProxyDirectoryPath()
    {
        return userDirResolver.buildPath(new LinkedHashSet<>(Arrays.asList("src", "main", "java", "com", "raphjava"
                , "softplanner", "data", "proxies")));
    }

    private static String buildEntitiesModelPackageName()
    {
        return "com.raphjava.softplanner.data.models";
    }
}

package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.components.UserDirectoryResolver;
import com.raphjava.softplanner.data.services.DefaultIOService;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class ProxyGeneration
{

    public static void main(String[] args)
    {
        ProxyGenerator proxyGenerator = ProxyGenerator.newBuilder().ioService(new DefaultIOService())
                .proxyAnnotationProcessor(ProxyAnnotationProcessor.newBuilder()
                        .entitiesModelPackage(buildEntitiesModelPackageName()).build())
                .proxiesPackageName(buildProxyPackageName())
                .proxiesDirectory(buildProxyDirectoryPath()).build();

        proxyGenerator.generateProxies();

    }

    private static String buildProxyPackageName()
    {
        return userDirResolver.buildPath(orderedPackageData, true).replace("\\", ".");
    }

    private static LinkedHashSet<String> orderedPackageData = new LinkedHashSet<>(Arrays.asList("com", "raphjava",
            "softplanner", "data", "proxies"));

    private static UserDirectoryResolver userDirResolver = new UserDirectoryResolver();


    private static String buildProxyDirectoryPath()
    {
        LinkedHashSet<String> dirData = new LinkedHashSet<>(Arrays.asList("src", "main", "java"));
        dirData.addAll(orderedPackageData);
        return userDirResolver.buildPath(dirData);
    }

    private static String buildEntitiesModelPackageName()
    {
        return "com.raphjava.softplanner.data.models";
    }
}

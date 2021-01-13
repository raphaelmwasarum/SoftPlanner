package com.raphjava.softplanner.services;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.data.services.StudeeFileBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

@Lazy
@Component
@Scope(Singleton)
public class FileBuilder extends AbFactoryBean<StudeeFileBuilder>
{


    private final StudeeFileBuilder.Builder builder;

    public FileBuilder()
    {
        super(StudeeFileBuilder.class);
        builder = StudeeFileBuilder.newBuilder().root(String.format("%s%s", getAppLocation(), "user_data"));
    }

    private String getAppLocation()
    {
        return String.format("%s%s", System.getProperty("user.dir"), File.separator);
    }

    @Override
    public synchronized StudeeFileBuilder build()
    {
        return builder.build();
    }
}

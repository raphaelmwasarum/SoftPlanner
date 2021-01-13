package com.raphjava.softplanner.components.interfaces;

public interface ValueConverter<Source, Target>
{
    Target convert(Source sourceData);

    Source convertBack(Target targetData);
}

package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.ValueConverter;
import net.raphjava.raphtility.collectionmanipulation.interfaces.Explorable;

import java.util.Collection;

public abstract class ConverterBase<Source, Target> implements ValueConverter<Source, Target>
{
    public abstract Target convert(Source sourceData);

    public abstract Source convertBack(Target targetData);

    protected void log(Exception e)
    {
        System.out.println(getClass().getSimpleName() + " conversion error. [Silent exception]. Details: " + e.getMessage());
    }
    @Override
    public String toString()
    {
        return getClass().getSimpleName();
    }

    protected Integer parse(String sourceData)
    {
        try
        {
            return Integer.valueOf(sourceData);
        }
        catch (NumberFormatException e)
        {
            log(e);
            return null;
        }
    }

    protected <T> Explorable<T> asExp(Collection<T> coll)
    {
        return new net.raphjava.raphtility.collectionmanipulation.ArrayList<T>(coll);
    }
}

package com.raphjava.softplanner.components;


import static net.raphjava.raphtility.utils.Utils.isBlank;

public class StringToDoubleConverter extends ConverterBase<String, Double>
{
    @Override
    public Double convert(String sourceData)
    {
        return (sourceData != null) ? (isBlank(sourceData) ? null : parseD(sourceData)) : null;
    }

    protected Double parseD(String sourceData)
    {
        try
        {
            return Double.valueOf(sourceData);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    @Override
    public String convertBack(Double data)
    {
        if(data == null) return null;
        return String.valueOf(data);
    }
}

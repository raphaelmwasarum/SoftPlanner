package com.raphjava.softplanner.components;


public class StringToIntConverter extends ConverterBase<String, Integer>
{
    @Override
    public Integer convert(String sourceData)
    {
        if(sourceData == null || sourceData.isEmpty()) return null;
        return parse(sourceData);
    }



    @Override
    public String convertBack(Integer targetData)
    {
        if(targetData == null) return null;
        return String.valueOf(targetData);
    }
}

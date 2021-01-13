package com.raphjava.softplanner.data;

import net.raphjava.qumbuqa.core.interfaces.Mapper;
import net.raphjava.qumbuqa.core.interfaces.PropertyMapper;

import java.time.LocalDateTime;
import java.util.function.Consumer;
import java.util.function.Function;

public class SPEntityToDatabaseMapper implements Consumer<Mapper>
{

    public void accept(Mapper mapper)
    {
        Function<String, Consumer<PropertyMapper>> primaryKeyBuilder = (columnName) ->
        {
            return (pm) -> {
                pm.name("id").propertyType(Integer.TYPE).column((c) -> {
                    c.name(columnName).databaseType("INT").isNull(false).isUnique(true);
                }).isPrimaryKey();
            };
        };
        Function<Integer, String> varcharC = (i) ->
        {
            return "VARCHAR(" + i + ")";
        };
        String varchar = (String)varcharC.apply(50);
        String text = "TEXT";
        String dInt = "INT";
        String doub = "DOUBLE";

    }

}

package com.raphjava.softplanner.data;

import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.ComponentDetail;
import com.raphjava.softplanner.data.models.Project;
import com.raphjava.softplanner.data.models.SubComponent;
import net.raphjava.qumbuqa.core.interfaces.Mapper;
import net.raphjava.qumbuqa.core.interfaces.PropertyMapper;

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


        /*
        *  Entities:

	- Project
		- Name
		- root

	- Component
		- subComponents
		- detail


	* - ComponentDetail
	*   - Name
	*   - Description
	*   - PseudoCode
	*   - Component
	*   - SubComponent

    * - SubComponent
    *   - parent
    *   - detail

		* */

        mapper
                .entity(ComponentDetail.class, em -> em.mapToTable("componentdetail")
                        .property(primaryKeyBuilder.apply("ComponentDetail_ID"))
                        .property(pm -> pm.name("name").propertyType(String.class).column(c -> c.name("Name")
                                .databaseType(varchar)))
                        .property(pm -> pm.name("description").propertyType(String.class).column(c -> c.name("Description")
                                .databaseType(text)))
                        .property(pm -> pm.name("pseudoCode").propertyType(String.class).column(c -> c.name("PseudoCode")
                                .databaseType(text))))

                .entity(SubComponent.class, em -> em.mapToTable("subcomponent")
                        .property(primaryKeyBuilder.apply("SubComponent_ID"))
                        .oneToOne(oto -> oto.with(ComponentDetail.class).myProperty(SubComponent.DETAIL).relativeProperty(ComponentDetail.SUB_COMPONENT)))

                .entity(Component.class, em -> em.mapToTable("component")
                        .property(primaryKeyBuilder.apply("Component_ID"))
                        .oneToMany(otm -> otm.with(SubComponent.class).myProperty(Component.SUB_COMPONENTS).relativeProperty(SubComponent.PARENT_COMPONENT))
                        .oneToOne(oto -> oto.with(ComponentDetail.class).myProperty(Component.DETAIL).relativeProperty(ComponentDetail.COMPONENT)))

                .entity(Project.class, em -> em.mapToTable("project")
                        .property(primaryKeyBuilder.apply("Project_ID"))
                        .property(pm -> pm.name("name").propertyType(String.class).column(c -> c.name("Name").databaseType(varchar)))
                        .oneToOne(oto -> oto.with(Component.class).myProperty(Project.ROOT).relativeProperty(Component.PROJECT)));
    }

}

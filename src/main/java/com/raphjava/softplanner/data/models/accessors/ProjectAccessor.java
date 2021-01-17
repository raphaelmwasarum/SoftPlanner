package com.raphjava.softplanner.data.models.accessors;

import com.raphjava.softplanner.data.models.accessors.EntityAccessorBase;
/*******************************************************************************************************************************************************************

 
 
 
 The code for this class has been generated, therefore any changes to it will be overwritten when its generated again.
 
 Generation done on 17/01/2021 at 0243 hrs by Coder. The Coder was programmed by Raphael Mwasaru Mwangangi.
 
 
 *******************************************************************************************************************************************************************/
import net.raphjava.raphtility.lambda.LambdaSettable;
import net.raphjava.raphtility.models.EntityBase;
import net.raphjava.qumbuqa.commons.components.interfaces.Accessor;
import com.raphjava.softplanner.data.models.Project;
import com.raphjava.softplanner.data.models.Component;
import java.lang.String;


public class ProjectAccessor extends EntityAccessorBase implements Accessor
{
	public ProjectAccessor()
	{
	}

	public Object invoke(Object entity, String propertyName)
	{
		return this.invoke((Project) entity, propertyName, null);
	}

	public Object invoke(Object entity, String propertyName, Object parameter)
	{
		return this.invoke((Project) entity, propertyName, parameter);
	}

	public Object invoke(Project entity, String propertyName, Object parameter)
	{
		if(entity instanceof EntityBase)
		{
			LambdaSettable<Boolean> baseCall = new LambdaSettable<Boolean>(false);
			Object rez = this.invokeEntityBase((EntityBase) entity, propertyName, parameter, baseCall);
			if(baseCall.getItem())
			{
				return rez;
			}
		}
		switch (propertyName)
		{
			case "setId":
				entity.setId((int) parameter);
				break;
			case "getId":
				return entity.getId();
			case "setName":
				entity.setName((java.lang.String) parameter);
				break;
			case "getName":
				return entity.getName();
			case "setRoot":
				entity.setRoot((Component) parameter);
				break;
			case "getRoot":
				return entity.getRoot();
				default:

		}

		return null;
	}

}


package com.raphjava.softplanner.data.models.accessors;

import com.raphjava.softplanner.data.models.accessors.EntityAccessorBase;
/*******************************************************************************************************************************************************************

 
 
 
 The code for this class has been generated, therefore any changes to it will be overwritten when its generated again.
 
 Generation done on 13/01/2021 at 1946 hrs by Coder. The Coder was programmed by Raphael Mwasaru Mwangangi.
 
 
 *******************************************************************************************************************************************************************/
import net.raphjava.raphtility.lambda.LambdaSettable;
import net.raphjava.raphtility.models.EntityBase;
import net.raphjava.qumbuqa.commons.components.interfaces.Accessor;
import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.ComponentDetail;
import java.util.Collection;
import com.raphjava.softplanner.data.models.Project;


public class ComponentAccessor extends EntityAccessorBase implements Accessor
{
	public ComponentAccessor()
	{
	}

	public Object invoke(Object entity, String propertyName)
	{
		return this.invoke((Component) entity, propertyName, null);
	}

	public Object invoke(Object entity, String propertyName, Object parameter)
	{
		return this.invoke((Component) entity, propertyName, parameter);
	}

	public Object invoke(Component entity, String propertyName, Object parameter)
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
			case "setDetail":
				entity.setDetail((ComponentDetail) parameter);
				break;
			case "getDetail":
				return entity.getDetail();
			case "setSubComponents":
				entity.setSubComponents((Collection) parameter);
				break;
			case "getSubComponents":
				return entity.getSubComponents();
			case "setSelectedProject":
				entity.setProject((Project) parameter);
				break;
			case "getProject":
				return entity.getProject();
				default:

		}

		return null;
	}

}


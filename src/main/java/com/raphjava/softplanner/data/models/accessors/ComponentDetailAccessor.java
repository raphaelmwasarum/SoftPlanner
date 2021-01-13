package com.raphjava.softplanner.data.models.accessors;

import com.raphjava.softplanner.data.models.accessors.EntityAccessorBase;
/*******************************************************************************************************************************************************************

 
 
 
 The code for this class has been generated, therefore any changes to it will be overwritten when its generated again.
 
 Generation done on 13/01/2021 at 1946 hrs by Coder. The Coder was programmed by Raphael Mwasaru Mwangangi.
 
 
 *******************************************************************************************************************************************************************/
import net.raphjava.raphtility.lambda.LambdaSettable;
import net.raphjava.raphtility.models.EntityBase;
import net.raphjava.qumbuqa.commons.components.interfaces.Accessor;
import com.raphjava.softplanner.data.models.ComponentDetail;
import java.lang.String;
import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.SubComponent;


public class ComponentDetailAccessor extends EntityAccessorBase implements Accessor
{
	public ComponentDetailAccessor()
	{
	}

	public Object invoke(Object entity, String propertyName)
	{
		return this.invoke((ComponentDetail) entity, propertyName, null);
	}

	public Object invoke(Object entity, String propertyName, Object parameter)
	{
		return this.invoke((ComponentDetail) entity, propertyName, parameter);
	}

	public Object invoke(ComponentDetail entity, String propertyName, Object parameter)
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
			case "setDescription":
				entity.setDescription((java.lang.String) parameter);
				break;
			case "getDescription":
				return entity.getDescription();
			case "setPseudoCode":
				entity.setPseudoCode((java.lang.String) parameter);
				break;
			case "getPseudoCode":
				return entity.getPseudoCode();
			case "setSubComponent":
				entity.setSubComponent((SubComponent) parameter);
				break;
			case "getSubComponent":
				return entity.getSubComponent();
			case "setComponent":
				entity.setComponent((Component) parameter);
				break;
			case "getComponent":
				return entity.getComponent();
				default:

		}

		return null;
	}

}


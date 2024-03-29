package com.raphjava.softplanner.data.models.accessors;

import com.raphjava.softplanner.data.models.accessors.EntityAccessorBase;
/*******************************************************************************************************************************************************************

 
 
 
 The code for this class has been generated, therefore any changes to it will be overwritten when its generated again.
 
 Generation done on 17/01/2021 at 0243 hrs by Coder. The Coder was programmed by Raphael Mwasaru Mwangangi.
 
 
 *******************************************************************************************************************************************************************/
import net.raphjava.raphtility.lambda.LambdaSettable;
import net.raphjava.raphtility.models.EntityBase;
import net.raphjava.qumbuqa.commons.components.interfaces.Accessor;
import com.raphjava.softplanner.data.models.Component;
import java.lang.String;
import com.raphjava.softplanner.data.models.SubComponentDetail;
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
			case "setSubComponentDetail":
				entity.setSubComponentDetail((SubComponentDetail) parameter);
				break;
			case "getSubComponentDetail":
				return entity.getSubComponentDetail();
			case "setSubComponents":
				entity.setSubComponents((Collection) parameter);
				break;
			case "getSubComponents":
				return entity.getSubComponents();
			case "setProject":
				entity.setProject((Project) parameter);
				break;
			case "getProject":
				return entity.getProject();
				default:

		}

		return null;
	}

}


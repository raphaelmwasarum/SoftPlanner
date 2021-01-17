package com.raphjava.softplanner.data.models.accessors;

import net.raphjava.raphtility.models.EntityBase;
/*******************************************************************************************************************************************************************

 
 
 
 The code for this class has been generated, therefore any changes to it will be overwritten when its generated again.
 
 Generation done on 17/01/2021 at 0243 hrs by Coder. The Coder was programmed by Raphael Mwasaru Mwangangi.
 
 
 *******************************************************************************************************************************************************************/
import net.raphjava.raphtility.lambda.LambdaSettable;
import java.time.LocalDateTime;
import net.raphjava.raphtility.primitives.EntityState;


public class EntityAccessorBase
{
	protected Object invokeEntityBase(EntityBase entity, String propertyName, Object parameter, LambdaSettable<Boolean> isAnEntityBaseCall)
	{
		isAnEntityBaseCall.setItem(true);
		switch (propertyName)
		{
			case "getId":
				return entity.getId();
			case "setId":
				entity.setId((int) parameter);
				break;
			case "getDateCreated":
				return entity.getDateCreated();
			case "setDateCreated":
				entity.setDateCreated((LocalDateTime) parameter);
				break;
			case "setDateModified":
				entity.setDateModified((LocalDateTime) parameter);
				break;
			case "getDateModified":
				return entity.getDateModified();
			case "getEntityState":
				return entity.getEntityState();
			case "setEntityState":
				entity.setEntityState((EntityState) parameter);
				break;
			case "isInRepository":
				return entity.isInRepository();
			case "setInRepository":
				entity.setInRepository((boolean) parameter);
				break;
			case "getClass":
				return entity.getClass();
				default:
					isAnEntityBaseCall.setItem(false);

		}

		return null;
	}

	protected void throwStrangeEntityAccess(Class<?> entityClass)
	{
		try
		{
			throw new Exception("You haven't written access code for entity: " + entityClass.getSimpleName());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	protected void throwStrangeMemberCallException(Class entityClass, String propertyName)
	{
		try
		{
			throw new Exception("You haven't written access code for member: " + propertyName + " in entity " + entityClass.getSimpleName());
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

}


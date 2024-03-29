package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.data.interfaces.DataService;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ProxyAssistant
{
	private DataService dataService;
	
	
	
	
	private String exceptionMessagePrefix = this.getClass().getSimpleName();
	
	
	
	
	private Map<String, PropertyLoader> propertyLoaders = new HashMap<>();
	
	
	
	
	private String proxyName = this.getClass().getSimpleName();
	
	
	
	
	private String modelName;
	
	
	
	
	public ProxyAssistant(String modelName)
	{
		this.modelName = modelName;
	}
	
	
	
	
	
	public static class PropertyLoader 
	{
		/*Returns true if the load from model is successful.
		*/private Supplier<Boolean> loadFromModel;
		
		
		private boolean propertyLoaded;
		
		
		private Runnable loadFromRepository;
		
		
		public boolean isPropertyLoaded()
		{
			return propertyLoaded;
		}
		
		
		
		protected void ensureLoaded(boolean force)
		{
			if(force)
			{
				loadFromRepository.run();
			}
			else if(!propertyLoaded)
			{
				if(!loadFromModel.get())
				{
					loadFromRepository.run();
				}
				
			}
			
			propertyLoaded = true;
		}
		
		
		
	}
	
	
	
	
	
	
	
}



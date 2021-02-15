package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.data.models.Component;
import com.raphjava.softplanner.data.models.Project;
import com.raphjava.softplanner.data.models.SubComponent;
import com.raphjava.softplanner.data.models.SubComponentDetail;
import net.raphjava.raphtility.collectionmanipulation.LinkedHashSet;

import java.util.Collection;

public class Component_Proxy extends Component
{


    private Component component;


    /*  Annotations
        - Entity should have a lazy loading proxy -> @Proxied.
        - Method should be lazy loaded -> @Proxied.

        API
        Entity1 entity1Proxy = dataService.newProxy(entity1);

        //TODO Continue from here. Add newProxy method to the DataService interface.

        Inside dataService.newProxy(entity1) method:
            return proxyFactory.newProxy(entity1);

        Proxy creation inside a ProxyFactory.
        Entity1 proxy = getProxy();


        getProxy implementation pseudo code.
        return new Entity1()
        {

        }


        Proxy generation:
            Configuration
            - Entity models package name
            - Proxy directory

        Create a main class that will do the generation.
            Generation pseudo code:
            - Instantiate the annotation processor
            - data from the annotation processor will then guide the generator.

        Generator
        - Receive data from annotation processor/ or its instance[annotation processor] instead.
        - Clear the Proxies directory.
        - Create the ProxyFactory class expression
        - For each entity class that should be proxied.
            - Get the ProxyFactory class expression
            - Add the initial code expressions.
            - Create the method declaration of its proxy factory creation method in the ProxyFactory
            - Add the code expression that will create and load the proxy factory into the proxyFactories, (which is a field
             inside ProxyFactory). This code should be inline code for the ProxyFactory.createFactories method.
            - For each relationship property that requires lazy loading from the current entity in the loop:
                - create its anonymous class expression
                - override the getter and add the following code:
                - add code expressions for the declaration of methods in the Outer class. These will be called by the
                code expression below (loadFromModel and loadFromRepo)
                - proxyAssistant.ensureLoaded method call.
                - return relationship property field.
    */



}

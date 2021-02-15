package com.raphjava.softplanner.data.proxies;

public class ProxyGenerator
{

    private ProxyAnnotationProcessor proxyAnnotationProcessor;

    private String proxiesDirectory;

    private ProxyGenerator(Builder builder)
    {
        proxyAnnotationProcessor = builder.proxyAnnotationProcessor;
        proxiesDirectory = builder.proxiesDirectory;
    }

    private void initialize()
    {
        proxyAnnotationProcessor.processAnnotations();

        System.out.println("");
        System.out.println("");
        proxyAnnotationProcessor.getProxyEntities().forEach(System.out::println);
        System.out.println("");

    }

    public static Builder newBuilder()
    {
        return new Builder();
    }


    /*
    * Generator
        - Receive data from annotation processor.
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

    public void generateProxies()
    {
        /*clearProxies();
        generateNewProxies();
        generateProxyFactory();*/
        System.out.println("Generation of proxies to be done here.");

    }

    public static final class Builder
    {

        private ProxyAnnotationProcessor proxyAnnotationProcessor;
        private String proxiesDirectory;

        private Builder()
        {
        }

        public Builder proxyAnnotationProcessor(ProxyAnnotationProcessor proxyAnnotationProcessor)
        {
            this.proxyAnnotationProcessor = proxyAnnotationProcessor;
            return this;
        }

        public Builder proxiesDirectory(String proxiesDirectory)
        {
            this.proxiesDirectory = proxiesDirectory;
            return this;
        }

        public ProxyGenerator build()
        {
            ProxyGenerator pg = new ProxyGenerator(this);
            pg.initialize();
            return pg;
        }

    }
}

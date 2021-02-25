package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.data.interfaces.IOService;
import net.raphjava.expression.*;
import net.raphjava.expression.interfaces.Writer;
import net.raphjava.qumbuqa.databasedesign.interfaces.MappingManager;
import net.raphjava.raphtility.reflection.interfaces.ReflectionHelper;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

public class
ProxyGenerator
{

    private ReflectionHelper reflectionHelper;
    private IOService ioService;
    private Map<String, ClassExpression> proxyClassExpressions = new HashMap<>();
    private MappingManager mappingManager;


    private ProxyAnnotationProcessor proxyAnnotationProcessor;

    private String proxiesDirectory;

    private ProxyGenerator(Builder builder)
    {
        ioService = builder.ioService;
        proxyAnnotationProcessor = builder.proxyAnnotationProcessor;
        proxiesDirectory = builder.proxiesDirectory;
        proxiesPackageName = builder.proxiesPackageName;
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
        generateNewProxies();
        persist();

    }

    private void persist()
    {
        for (Map.Entry<String, ClassExpression> proxyClassData : proxyClassExpressions.entrySet())
        {
            Writer w = new WriterImp();
            proxyClassData.getValue().build(w);
            ioService.writeToFile(resolveAbsoluteFilePath(proxyClassData.getKey()), w.getExpression());
        }

    }

    private String resolveAbsoluteFilePath(String proxyClassName)
    {
        return String.format("%s%s%s.java", proxiesDirectory, File.separator, proxyClassName);
    }

    private void generateNewProxies()
    {
        generateProxyAssistantClassExpression();
        for (ProxyAnnotationProcessor.ProxyMetaData mData : proxyAnnotationProcessor.getProxyEntities())
        {
            generateNewProxy(mData);
        }

    }

    private final String PROXY_ASSISTANT_CLASS_NAME = "ProxyAssistant";

    private String proxiesPackageName;

    private Expression.AccessModifier _public = Expression.AccessModifier.Public;

    private void generateProxyAssistantClassExpression()
    {
        ClassExpression proxyAssistant = proxyClassExpressions.computeIfAbsent(PROXY_ASSISTANT_CLASS_NAME, key ->
        {
            ClassExpression c = new ClassExpression();
            c.access(_public).name(n -> n.name(key)).package_(px -> px.paths(pl -> pl.value(ea -> ea.constant(proxiesPackageName))));
            return c;
        });

        addProxyAssistantFieldExpressions(proxyAssistant);


    }

    private Expression.AccessModifier _private = Expression.AccessModifier.Private;

    private void addFullyBuiltPackageExpression(ClassExpression classExpression, String fullyBuiltPackage)
    {
        classExpression.import_(ix -> ix.paths(dsvx -> dsvx.value(vx -> vx.constant(fullyBuiltPackage))));
    }

    private void addProxyAssistantFieldExpressions(ClassExpression proxyAssistant)
    {
        /*
            private DataService dataService;
            protected String exceptionMessagePrefix = getClass().getSimpleName();

            protected Map<String, PropertyLoader> propertyLoaders = new HashMap<>();
            modelName
            proxyName

            */

        Consumer<MethodCallExpression> getClass = gcx -> gcx.objectReference(ea -> ea
                .constant("this")).name(nx -> nx.constant("getClass"));

        BiFunction<ExpressionAssistant, String, Expression> eaConstant = ExpressionAssistant::constant;

        addFullyBuiltPackageExpression(proxyAssistant, "com.raphjava.softplanner.data.interfaces.DataService");
        addFullyBuiltPackageExpression(proxyAssistant, "java.util.HashMap");
        addFullyBuiltPackageExpression(proxyAssistant, "java.util.Map");
        addFullyBuiltPackageExpression(proxyAssistant, "java.util.function.Supplier");

        String propertyLoaded = "propertyLoaded";
        String loadFromRepo = "loadFromRepository";
        String bool = "boolean";
        proxyAssistant

                //private DataService dataService;
                .field(fx -> fx.access(_private).type(t -> buildType(t, "DataService"))
                        .name(n -> n.constant("dataService")))

                //protected String exceptionMessagePrefix = getClass().getSimpleName();
                .field(fx -> fx.access(_private).type(t -> buildType(t, "String"))
                        .name(n -> n.constant("exceptionMessagePrefix"))
                        .assignment(ax -> ax.methodCall(mcx -> mcx.objectReference(ea -> ea.methodCall(getClass))
                                .name(nx -> nx.constant("getSimpleName")))))

                //protected Map<String, PropertyLoader> propertyLoaders = new HashMap<>();
                .field(fx -> fx.access(_private).type(tx -> tx.name("Map").genericNotation(gnx -> gnx.parameters(csvx ->
                        csvx.value(ea -> eaConstant.apply(ea, "String")).value(ea -> eaConstant.apply(ea, "PropertyLoader")))))
                        .name(ea -> eaConstant.apply(ea, "propertyLoaders"))
                        .assignment(ax -> ax.constructorCall(ccx -> ccx.name(ea -> eaConstant.apply(ea, "HashMap"))
                                .genericNotation(gnx -> gnx.parameters(csvx -> csvx.value(x -> eaConstant.apply(x, ""/*to end up with <>*/)))))))

                /*protected static class PropertyLoader
                {
                /**
                 * Returns true if the load from model is successful.
                 *//*
        public Supplier<Boolean> loadFromModel;

        private boolean propertyLoaded;

        public boolean isPropertyLoaded()
        {
            return propertyLoaded;
        }

        private Runnable loadFromRepository;

        protected void ensureLoaded(boolean force)
        {
            if (force) loadFromRepository.run();
            else if (!propertyLoaded)
            {
                if (!loadFromModel.get())
                {
                    loadFromRepository.run();
                }
            }
            propertyLoaded = true;
        }
    }*/
                .nestedClass(ncx -> ncx.access(_public).static_().name(tx -> tx.name("PropertyLoader"))

                        .field(fx -> fx.comment(cx -> cx.location(Expression.CommentLocation.Up).comment(ea -> eaConstant
                                .apply(ea, "Returns true if the load from model is successful.")))
                                .access(_private).type(tx -> tx.name("Supplier").genericNotation(gnx -> gnx
                                        .parameters(px -> px.value(vx -> eaConstant.apply(vx, "Boolean")))))
                                .name(nx -> eaConstant.apply(nx, "loadFromModel")))


                        .field(fx -> fx.access(_private).type(tx -> tx.name(bool)).name(nx -> eaConstant
                                .apply(nx, propertyLoaded)))


                        .method(mx -> mx.access(_public).returnType(tx -> tx.name(bool)).name(nx -> eaConstant
                                .apply(nx, "isPropertyLoaded")).inlineCode(icx -> icx.return_(rx -> rx
                                .statement(stx -> eaConstant.apply(stx, propertyLoaded))).withSemiColon()))


                        .field(fx -> fx.access(_private).type(tx -> tx.name("Runnable")).name(nx -> eaConstant
                                .apply(nx, loadFromRepo)))


                        .method(mx -> mx.access(_protected).returnType(tx -> tx.name("void")).name(nx -> eaConstant
                                .apply(nx, "ensureLoaded")).parameterDeclarations(px -> px.value(ea -> ea.variable(vx -> vx
                        .type(tx -> tx.name(bool)).name(e -> eaConstant.apply(e, "force"))))).inlineCode(icx -> icx
                            .if_(ifex -> ifex.if_(ifx -> ifx.condition(cx -> eaConstant.apply(cx, "force"))
                                    .inlineCode(ix -> ix.methodCall(mcx -> mcx.objectReference(ea -> eaConstant
                                            .apply(ea, loadFromRepo)).name(ea -> eaConstant.apply(ea, "run")).withSemiColon())))))))

        ;


    }

    private Expression.AccessModifier _protected = Expression.AccessModifier.Protected;

    private void buildType(TypeExpression typeExpression, String typeName)
    {
        typeExpression.name(typeName);
    }


    private void generateNewProxy(ProxyAnnotationProcessor.ProxyMetaData proxyMetaData)
    {

    }


    public static final class Builder extends AbFactoryBean<ProxyGenerator>
    {

        private ProxyAnnotationProcessor proxyAnnotationProcessor;
        private String proxiesDirectory;
        private String proxiesPackageName;
        private IOService ioService;

        private Builder()
        {
            super(ProxyGenerator.class);
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

        public Builder proxiesPackageName(String proxiesPackageName)
        {
            this.proxiesPackageName = proxiesPackageName;
            return this;
        }

        public ProxyGenerator build()
        {
            ProxyGenerator pg = new ProxyGenerator(this);
            pg.initialize();
            return pg;
        }

        public Builder ioService(IOService ioService)
        {
            this.ioService = ioService;
            return this;
        }
    }
}

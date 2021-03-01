package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.components.AbFactoryBean;
import com.raphjava.softplanner.data.interfaces.IOService;
import net.raphjava.expression.*;
import net.raphjava.expression.interfaces.Writer;
import net.raphjava.qumbuqa.databasedesign.interfaces.MappingManager;
import net.raphjava.raphtility.reflection.interfaces.ReflectionHelper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

        BiFunction<ExpressionAssistant, String, Expression> constX = ExpressionAssistant::constant;

        addFullyBuiltPackageExpression(proxyAssistant, "com.raphjava.softplanner.data.interfaces.DataService");
        addFullyBuiltPackageExpression(proxyAssistant, "java.util.HashMap");
        addFullyBuiltPackageExpression(proxyAssistant, "java.util.Map");
        addFullyBuiltPackageExpression(proxyAssistant, "java.util.function.Supplier");

        String propertyLoaded = "propertyLoaded";
        String loadFromRepo = "loadFromRepository";
        String bool = "boolean";
        String loadFromModel = "loadFromModel";

        Supplier<String> loadFromModelExpression = () ->
        {
            MethodCallExpression mcx = new MethodCallExpression();
            mcx.objectReference(ox -> constX.apply(ox, loadFromModel)).name(nx -> constX.apply(nx, "get"));
            Writer w = new WriterImp();
            mcx.build(w);
            return w.getExpression();
        };


        Consumer<TypeExpression> stringType = tx -> tx.name("String");
        String proxyName = "proxyName";
        String modelName = "modelName";
        Consumer<TypeExpression> voidType = tx -> tx.name("void");
        String propertyName = "propertyName";
        String force = "force";
        String propertyLoaders = "propertyLoaders";
        String boolArray = "boolean[]";
        String newPropertyLoader = "newPropertyLoader";
        String propertyLoader = "PropertyLoader";
        String boolSupplier = "Supplier<Boolean>";
        Consumer<VariableDeclarationExpression> loadFromModelVar = v -> v.type(tx -> tx.name(boolSupplier))
                .name(nx -> constX.apply(nx, loadFromModel));

        Consumer<VariableDeclarationExpression> loadFromRepoVar = v -> v.type(tx -> tx.name("Runnable"))
                .name(nx -> constX.apply(nx, loadFromRepo));

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
                        csvx.value(ea -> constX.apply(ea, "String")).value(ea -> constX.apply(ea, propertyLoader)))))
                        .name(ea -> constX.apply(ea, propertyLoaders))
                        .assignment(ax -> ax.constructorCall(ccx -> ccx.name(ea -> constX.apply(ea, "HashMap"))
                                .genericNotation(gnx -> gnx.parameters(csvx -> csvx.value(x -> constX.apply(x, ""/*to end up with <>*/)))))))

                //PropertyLoader
                .nestedClass(ncx -> ncx.access(_public).static_().name(tx -> tx.name(propertyLoader))

                                .field(fx -> fx.comment(cx -> cx.location(Expression.CommentLocation.Up).comment(ea -> constX
                                        .apply(ea, "Returns true if the load from model is successful.")))
                                        .access(_private).type(tx -> tx.name("Supplier").genericNotation(gnx -> gnx
                                                .parameters(px -> px.value(vx -> constX.apply(vx, "Boolean")))))
                                        .name(nx -> constX.apply(nx, "loadFromModel")))


                                .field(fx -> fx.access(_private).type(tx -> tx.name(bool)).name(nx -> constX.apply(nx, propertyLoaded)))

                                .method(mx -> mx.access(_public).returnType(tx -> tx.name(bool)).name(nx -> constX
                                        .apply(nx, "isPropertyLoaded")).inlineCode(icx -> icx.return_(rx -> rx
                                        .statement(stx -> constX.apply(stx, propertyLoaded))).withSemiColon()))


                                .field(fx -> fx.access(_private).type(tx -> tx.name("Runnable")).name(nx -> constX.apply(nx, loadFromRepo)))

                                .method(mx -> mx.access(_protected).returnType(voidType).name(nx -> constX.apply(nx, "ensureLoaded"))
                                        .parameterDeclarations(px -> px.value(ea -> ea.variable(vx -> vx.type(tx -> tx.name(bool))
                                                .name(e -> constX.apply(e, force))))).inlineCode(icx -> icx.if_(ifex -> ifex
                                                .if_(ifx -> ifx.condition(cx -> constX.apply(cx, force)).inlineCode(ix -> ix
                                                        .methodCall(mcx -> mcx.objectReference(ea -> constX.apply(ea, loadFromRepo))
                                                                .name(ea -> constX.apply(ea, "run")).withSemiColon())))
                                                .elseIf_(eifx -> eifx.condition(cx -> constX.apply(cx, "!" + propertyLoaded))
                                                        .inlineCode(ix -> ix.if_(ifex1 -> ifex1.if_(ifx -> ifx.condition(cx ->
                                                                constX.apply(cx, "!" + loadFromModelExpression.get()))
                                                                .inlineCode(in -> in.methodCall(mcx -> mcx.objectReference(ox ->
                                                                        constX.apply(ox, loadFromRepo)).name(nx -> constX
                                                                        .apply(nx, "run")).withSemiColon())))))))).inlineCode(icx ->
                                                icx.equation(ex -> ex.left(lx -> constX.apply(lx, propertyLoaded)).right(rx ->
                                                        constX.apply(rx, "true")).withSemiColon())))

                       /* .field(fx -> fx.access(_protected).type(tx -> tx.name("Map").genericNotation(gx -> gx.parameters(px ->
                                px.value(v -> constX.apply(v, "String")).value(v -> constX
                                        .apply(v, propertyLoader))))).name(nx -> constX.apply(nx, propertyLoaders))
                                .assignment(ax -> ax.constructorCall(cx -> cx.name(nx -> nx
                                        .type(tx -> tx.name("HashMap").genericNotation(gx -> gx.parameters(px -> px.value(v -> constX
                                                .apply(v, ""*//*To end up with this -> <> *//*)))))))))*/

                )

                .method(mx -> mx.access(_protected).returnType(tx -> tx.name("boolean")).name(nx -> constX.apply(nx, force))
                        .parameterDeclarations(px -> px.value(v -> v.variable(vx -> vx.type(tx -> tx.name(boolArray))
                                .name(nx -> constX.apply(nx, force))))).inlineCode(ix -> ix.if_(ifex -> ifex
                                .if_(ifx -> ifx.condition(cx -> cx.equality(ex -> ex.left(lx -> constX.apply(lx, force))
                                        .right(rx -> constX.apply(rx, "null")))).inlineCode(inx -> inx.return_(r -> r
                                        .statement(sx -> constX.apply(sx, "false"))).withSemiColon())))).inlineCode(ix -> ix
                                .return_(rx -> rx.statement(sx -> sx.comparison(cx -> cx.symbol("&&").left(l ->
                                        l.comparison(c -> c.symbol("!=").left(lx -> lx.fieldAccess(fx -> fx
                                                .instance(i -> constX.apply(i, force)).field(f -> constX
                                                        .apply(f, "length")))).right(r -> constX.apply(r, "0"))))
                                        .right(rxx -> constX.apply(rxx, String.format("%s[0]", force)))))).withSemiColon()))

                //private String proxyName = getClass().getSimpleName();
                .field(fx -> fx.access(_private).type(stringType).name(nx -> constX.apply(nx, proxyName)).assignment(ax ->
                        ax.methodCall(mcx -> mcx.objectReference(ox -> ox.methodCall(mcx1 -> mcx1.objectReference(ox1 ->
                                constX.apply(ox1, "this")).name(nx -> constX.apply(nx, "getClass")))).name(nx ->
                                constX.apply(nx, "getSimpleName")))))

                //private String modelName;
                .field(fx -> fx.access(_private).type(stringType).name(nx -> constX.apply(nx, modelName)))

                //Constructor
                .constructor(cx -> cx.access(_public).parameters(px -> px.value(v -> v.variable(vx -> vx.type(stringType)
                        .name(n -> constX.apply(n, modelName))))).inlineCode(icx -> icx.equation(ex -> ex.left(e -> e
                        .fieldAccess(fx -> fx.dis().field(ea -> constX.apply(ea, modelName)))).right(r -> constX
                        .apply(r, modelName))).withSemiColon()))


                //EnsureLoaded
                .method(mx -> mx.access(_protected).returnType(voidType).name(nx -> constX.apply(nx, "ensureLoaded"))
                        .parameterDeclarations(px -> px.value(vx -> vx.variable(v -> v.type(stringType).name(nx ->
                                constX.apply(nx, propertyName)))).value(vx -> vx.variable(v -> v.type(tx -> tx
                                .name(boolArray)).name(nx -> constX.apply(nx, force)))).value(vx -> vx.variable(loadFromModelVar))
                                .value(vx -> vx.variable(loadFromRepoVar))).inlineCode(ix -> ix.methodCall(mcx -> mcx
                                .objectReference(ixx -> ixx.methodCall(mccx -> mccx.objectReference(ox -> constX
                                        .apply(ox, propertyLoaders)).name(nx -> constX.apply(nx, "computeIfAbsent"))
                                        .parameter(px -> px.value(v -> constX.apply(v, propertyName)).value(v -> v
                                                .lambda(lx -> lx.simple().parameters(csx -> csx.value(p -> constX
                                                        .apply(p, "key"))).body(b -> b.methodCall(mcx1 -> mcx1
                                                        .dis().name(nx -> constX.apply(nx, newPropertyLoader))
                                                        .parameter(px1 -> px1.value(x -> constX.apply(x, loadFromModel))
                                                                .value(y -> constX.apply(y, loadFromRepo))))))))))
                                .name(nx -> constX.apply(nx, "ensureLoaded")).parameter(csv -> csv.value(v -> v
                                        .methodCall(m -> m.dis().name(nx -> constX.apply(nx, force)).parameter(p -> p
                                                .value(x -> constX.apply(x, force))))))).withSemiColon()))

                //newPropertyLoader method
                .method(mcx ->
                {
                    String pl = "pl";
                    mcx.access(_public).returnType(rx -> rx.name(propertyLoader)).name(nx -> constX.apply(nx,
                            newPropertyLoader)).parameterDeclarations(px -> px.value(v -> v.variable(loadFromModelVar))
                            .value(v -> v.variable(loadFromRepoVar))).inlineCode(ix -> ix.equation(ex -> ex.left(lx -> lx
                            .variable(v -> v.type(t -> t.name(propertyLoader)).name(nx -> constX.apply(nx, pl)))).right(rx -> rx
                            .constructorCall(cx -> cx.name(nx -> constX.apply(nx, propertyLoader)))).withSemiColon()))
                            .inlineCode(ix -> ix.equation(ex -> ex.left(lx -> lx.fieldAccess(fx -> fx.instance(i -> constX
                                    .apply(i, pl)).field(f -> constX.apply(f, loadFromModel)))).right(rx -> constX
                                    .apply(rx, loadFromModel))).withSemiColon())
                            .inlineCode(ix -> ix.equation(ex -> ex.left(lx -> lx.fieldAccess(fx -> fx.instance(i -> constX
                                    .apply(i, pl)).field(f -> constX.apply(f, loadFromRepo)))).right(rx -> constX.apply(rx
                                    , loadFromRepo))).withSemiColon())
                            .inlineCode(ix -> ix.return_(rx -> rx.statement(sx -> constX.apply(sx, pl))).withSemiColon());
                })

                .method(mcx ->
                {

                    String t = "T";
                    String clVar = "cl";
                    String id = "id";
                    String withRel = "withRelatives";
                    mcx.access(_protected).genericNotation(g -> g.parameters(p -> p.value(v -> constX.apply(v, t))))
                            .returnType(rx -> rx.name(t)).name(nx -> constX.apply(nx, "get")).parameterDeclarations(p -> p
                            .value(v -> v.variable(vx -> vx.type(tx -> tx.name(String.format("Class<%s>", t))).name(nx -> constX
                                    .apply(nx, clVar)))).value(v -> v.variable(vx -> vx.type(tx -> tx.name("int")).name(nx -> constX
                                    .apply(nx, id)))).value(v -> v.variable(vx -> vx.type(tx -> tx.name(bool)).name(nx ->
                                    constX.apply(nx, withRel)))));
                })


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

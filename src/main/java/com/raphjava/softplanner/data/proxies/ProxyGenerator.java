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
import java.util.function.Consumer;
import java.util.function.Function;
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
        generateTheProxyFactoryClassExpression();
        generateProxyAssistantClassExpression();
        generateNewProxies();
        persist();

    }

    private String proxyFactoryInterfaceName = "ProxyFactory";

    private void persist()
    {
        Writer pfWriter = new WriterImp();
        getClassExpression(proxyFactoryInterfaceName).build(pfWriter);
        ioService.writeToFile(resolveAbsoluteFilePath(proxyFactoryInterfaceName), pfWriter.getExpression());
        for (Map.Entry<String, ClassExpression> proxyClassData : proxyClassExpressions.entrySet())
        {
            Writer w = new WriterImp();
            proxyClassData.getValue().build(w);
            ioService.writeToFile(resolveAbsoluteFilePath(proxyClassData.getKey()), w.getExpression());
        }

    }

    private String resolveAbsoluteFilePath(String className)
    {
        String dir = className.equals(proxyFactoryInterfaceName) ? String.format("%s%sinterfaces" , proxiesDirectory
                , File.separator) : proxiesDirectory;
        return String.format("%s%s%s.java", dir, File.separator, className);
    }

    private void generateNewProxies()
    {

        for (ProxyAnnotationProcessor.ProxyMetaData mData : proxyAnnotationProcessor.getProxyEntities())
        {
            generateNewProxy(mData);
        }

    }


    private void generateTheProxyFactoryClassExpression()
    {
        ClassExpression pfClassExpression = getClassExpression(proxyFactoryInterfaceName);

    }


    private final String PROXY_ASSISTANT_CLASS_NAME = "ProxyAssistant";

    private String proxiesPackageName;

    private Expression.AccessModifier _public = Expression.AccessModifier.Public;



    private ClassExpression getClassExpression(String className)
    {
        return proxyClassExpressions.computeIfAbsent(className, key ->
        {
            ClassExpression c = new ClassExpression();
            c.access(_public).name(n -> n.name(key)).package_(px -> px.paths(pl -> pl.value(consX.apply(className
                    .equals(proxyFactoryInterfaceName) ? String.format("%s.interfaces", proxiesPackageName)
                    : proxiesPackageName))));
            return c;
        });
    }


    private void generateProxyAssistantClassExpression()
    {
        ClassExpression proxyAssistant = getClassExpression(PROXY_ASSISTANT_CLASS_NAME);
        addProxyAssistantFieldExpressions(proxyAssistant);


    }


    private Expression.AccessModifier _private = Expression.AccessModifier.Private;

    private void addFullyBuiltPackageExpression(ClassExpression classExpression, String fullyBuiltPackage)
    {
        classExpression.import_(ix -> ix.paths(dsvx -> dsvx.value(vx -> vx.constant(fullyBuiltPackage))));
    }

    private Consumer<MethodCallExpression> getClass = gcx -> gcx.objectReference(ea -> ea.constant("this"))
            .name(nx -> nx.constant("getClass"));

    private Function<String, Function<ExpressionAssistant, Expression>> consX = _const -> x -> x.constant(_const);


    private void addProxyAssistantFieldExpressions(ClassExpression proxyAssistant)
    {
        /*
            private DataService dataService;
            protected String exceptionMessagePrefix = getClass().getSimpleName();

            protected Map<String, PropertyLoader> propertyLoaders = new HashMap<>();
            modelName
            proxyName

            */


//        BiFunction<ExpressionAssistant, String, Expression> constX = ExpressionAssistant::constant;

        addFullyBuiltPackageExpression(proxyAssistant, "com.raphjava.softplanner.data.interfaces.DataService");
        addFullyBuiltPackageExpression(proxyAssistant, "java.util.HashMap");
        addFullyBuiltPackageExpression(proxyAssistant, "java.util.Map");
        addFullyBuiltPackageExpression(proxyAssistant, "java.util.function.Supplier");
        addFullyBuiltPackageExpression(proxyAssistant, "java.util.function.Consumer");
        addFullyBuiltPackageExpression(proxyAssistant, "com.raphjava.softplanner.data.interfaces.EagerLoader");

        String propertyLoaded = "propertyLoaded";
        String loadFromRepo = "loadFromRepository";
        String bool = "boolean";
        String loadFromModel = "loadFromModel";

        Supplier<String> loadFromModelExpression = () ->
        {
            MethodCallExpression mcx = new MethodCallExpression();
//            mcx.objectReference(ox -> constX.apply(ox, loadFromModel)).name(nx -> constX.apply(nx, "get"));
            mcx.objectReference(consX.apply(loadFromModel)).name(consX.apply("get"));
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
//                .name(nx -> constX.apply(nx, loadFromModel));
                .name(consX.apply(loadFromModel));

        Consumer<VariableDeclarationExpression> loadFromRepoVar = v -> v.type(tx -> tx.name("Runnable"))
//                .name(nx -> constX.apply(nx, loadFromRepo));
                .name(consX.apply(loadFromRepo));

        String dataService = "dataService";
        proxyAssistant

                //private DataService dataService;
                .field(fx -> fx.access(_private).type(t -> buildType(t, "DataService"))
                        .name(n -> n.constant(dataService)))

                //protected String exceptionMessagePrefix = getClass().getSimpleName();
                .field(fx -> fx.access(_private).type(t -> buildType(t, "String"))
                        .name(n -> n.constant("exceptionMessagePrefix"))
                        .assignment(ax -> ax.methodCall(mcx -> mcx.objectReference(ea -> ea.methodCall(getClass))
                                .name(nx -> nx.constant("getSimpleName")))))

                //protected Map<String, PropertyLoader> propertyLoaders = new HashMap<>();
                .field(fx -> fx.access(_private).type(tx -> tx.name("Map").genericNotation(gnx -> gnx.parameters(csvx ->
//                        csvx.value(ea -> constX.apply(ea, "String")).value(ea -> constX.apply(ea, propertyLoader)))))
                        csvx.value(consX.apply("String")).value(consX.apply(propertyLoader)))))
//                        .name(ea -> constX.apply(ea, propertyLoaders))
                        .name(consX.apply(propertyLoaders))
//                        .assignment(ax -> ax.constructorCall(ccx -> ccx.name(ea -> constX.apply(ea, "HashMap"))
                        .assignment(ax -> ax.constructorCall(ccx -> ccx.name(consX.apply("HashMap"))
//                                .genericNotation(gnx -> gnx.parameters(csvx -> csvx.value(x -> constX.apply(x, ""/*to end up with <>*/)))))))
                                .genericNotation(gnx -> gnx.parameters(csvx -> csvx.value(consX.apply(""/*to end up with <>*/)))))))

                //PropertyLoader
                .nestedClass(ncx -> ncx.access(_public).static_().name(tx -> tx.name(propertyLoader))

//                                .field(fx -> fx.comment(cx -> cx.location(Expression.CommentLocation.Up).comment(ea -> constX
//                                        .apply(ea, "Returns true if the load from model is successful.")))
                                .field(fx -> fx.comment(cx -> cx.location(Expression.CommentLocation.Up)
                                        .comment(consX.apply("Returns true if the load from model is successful.")))
                                        .access(_private).type(tx -> tx.name("Supplier").genericNotation(gnx -> gnx
                                                .parameters(px -> px.value(consX.apply("Boolean")))))
                                        .name(consX.apply(loadFromModel)))


//                                .field(fx -> fx.access(_private).type(tx -> tx.name(bool)).name(nx -> constX.apply(nx, propertyLoaded)))
                                .field(fx -> fx.access(_private).type(tx -> tx.name(bool)).name(consX.apply(propertyLoaded)))

                                .method(mx -> mx.access(_public).returnType(tx -> tx.name(bool)).name(consX.apply("isPropertyLoaded"))
                                        .inlineCode(icx -> icx.return_(rx -> rx.statement(consX.apply(propertyLoaded)))
                                                .withSemiColon()))

//                                .field(fx -> fx.access(_private).type(tx -> tx.name("Runnable")).name(nx -> constX.apply(nx, loadFromRepo)))
                                .field(fx -> fx.access(_private).type(tx -> tx.name("Runnable")).name(consX.apply(loadFromRepo)))

//                                .method(mx -> mx.access(_protected).returnType(voidType).name(nx -> constX.apply(nx, "ensureLoaded"))
                                .method(mx -> mx.access(_protected).returnType(voidType).name(consX.apply("ensureLoaded"))
                                        .parameterDeclarations(px -> px.value(ea -> ea.variable(vx -> vx.type(tx -> tx.name(bool))
//                                                .name(e -> constX.apply(e, force))))).inlineCode(icx -> icx.if_(ifex -> ifex
                                                .name(consX.apply(force))))).inlineCode(icx -> icx.if_(ifex -> ifex
//                                                .if_(ifx -> ifx.condition(cx -> constX.apply(cx, force)).inlineCode(ix -> ix
                                                .if_(ifx -> ifx.condition(consX.apply(force)).inlineCode(ix -> ix
//                                                        .methodCall(mcx -> mcx.objectReference(ea -> constX.apply(ea, loadFromRepo))
                                                        .methodCall(mcx -> mcx.objectReference(consX.apply(loadFromRepo))
//                                                                .name(ea -> constX.apply(ea, "run")).withSemiColon())))
                                                                .name(consX.apply("run")).withSemiColon())))
//                                                .elseIf_(eifx -> eifx.condition(cx -> constX.apply(cx, "!" + propertyLoaded))
                                                .elseIf_(eifx -> eifx.condition(consX.apply(String.format("!%s", propertyLoaded)))
                                                        .inlineCode(ix -> ix.if_(ifex1 -> ifex1.if_(ifx -> ifx
//                                                                .condition(cx -> constX.apply(cx, "!" + loadFromModelExpression.get()))
                                                                .condition(consX.apply(String.format("!%s"
                                                                        , loadFromModelExpression.get())))
                                                                .inlineCode(in -> in.methodCall(mcx -> mcx
                                                                        .objectReference(consX.apply(loadFromRepo))
//                                                                        .name(nx -> constX.apply(nx, "run"))
                                                                        .name(consX.apply("run"))
                                                                        .withSemiColon())))))))).inlineCode(icx ->
//                                                icx.equation(ex -> ex.left(lx -> constX.apply(lx, propertyLoaded)).right(rx ->
                                                icx.equation(ex -> ex.left(consX.apply(propertyLoaded)).right(consX
                                                        .apply("true")).withSemiColon())))

                       /* .field(fx -> fx.access(_protected).type(tx -> tx.name("Map").genericNotation(gx -> gx.parameters(px ->
                                px.value(v -> constX.apply(v, "String")).value(v -> constX
                                        .apply(v, propertyLoader))))).name(nx -> constX.apply(nx, propertyLoaders))
                                .assignment(ax -> ax.constructorCall(cx -> cx.name(nx -> nx
                                        .type(tx -> tx.name("HashMap").genericNotation(gx -> gx.parameters(px -> px.value(v -> constX
                                                .apply(v, ""*//*To end up with this -> <> *//*)))))))))*/

                )

//                .method(mx -> mx.access(_protected).returnType(tx -> tx.name("boolean")).name(nx -> constX.apply(nx, force))
                .method(mx -> mx.access(_protected).returnType(tx -> tx.name("boolean")).name(consX.apply(force))
                        .parameterDeclarations(px -> px.value(v -> v.variable(vx -> vx.type(tx -> tx.name(boolArray))
//                                .name(nx -> constX.apply(nx, force))))).inlineCode(ix -> ix.if_(ifex -> ifex
                                .name(consX.apply(force))))).inlineCode(ix -> ix.if_(ifex -> ifex
//                                .if_(ifx -> ifx.condition(cx -> cx.equality(ex -> ex.left(lx -> constX.apply(lx, force))
                                .if_(ifx -> ifx.condition(cx -> cx.equality(ex -> ex.left(consX.apply(force))
//                                        .right(rx -> constX.apply(rx, "null")))).inlineCode(inx -> inx.return_(r -> r
                                        .right(consX.apply("null")))).inlineCode(inx -> inx.return_(r -> r
//                                        .statement(sx -> constX.apply(sx, "false"))).withSemiColon())))).inlineCode(ix -> ix
                                        .statement(consX.apply("false"))).withSemiColon())))).inlineCode(ix -> ix
                                .return_(rx -> rx.statement(sx -> sx.comparison(cx -> cx.symbol("&&").left(l ->
                                        l.comparison(c -> c.symbol("!=").left(lx -> lx.fieldAccess(fx -> fx
//                                                .instance(i -> constX.apply(i, force)).field(f -> constX
                                                .instance(consX.apply(force)).field(consX.apply("length"))))
//                                                .right(r -> constX.apply(r, "0")))).right(rxx -> constX.apply(rxx, String.format("%s[0]", force)))))).withSemiColon()))
                                                .right(consX.apply("0")))).right(consX.apply(String.format("%s[0]"
                                        , force)))))).withSemiColon()))

                //private String proxyName = getClass().getSimpleName();
                .field(fx -> fx.access(_private).type(stringType).name(consX.apply(proxyName)).assignment(ax ->
                        ax.methodCall(mcx -> mcx.objectReference(ox -> ox.methodCall(mcx1 -> mcx1.objectReference(consX
                                .apply("this")).name(consX.apply("getClass")))).name(consX.apply("getSimpleName")))))

                //private String modelName;
                .field(fx -> fx.access(_private).type(stringType).name(consX.apply(modelName)))

                //Constructor
                .constructor(cx -> cx.access(_public).parameters(px -> px.value(v -> v.variable(vx -> vx.type(stringType)
                        .name(consX.apply(modelName))))).inlineCode(icx -> icx.equation(ex -> ex.left(e -> e
                        .fieldAccess(fx -> fx.dis().field(consX.apply(modelName)))).right(consX.apply(modelName)))
                        .withSemiColon()))


                //EnsureLoaded
                .method(mx -> mx.access(_protected).returnType(voidType).name(consX.apply("ensureLoaded"))
                        .parameterDeclarations(px -> px.value(vx -> vx.variable(v -> v.type(stringType).name(consX
                                .apply(propertyName)))).value(vx -> vx.variable(v -> v.type(tx -> tx.name(boolArray))
                                .name(consX.apply(force)))).value(vx -> vx.variable(loadFromModelVar))
                                .value(vx -> vx.variable(loadFromRepoVar))).inlineCode(ix -> ix.methodCall(mcx -> mcx
                                .objectReference(ixx -> ixx.methodCall(mccx -> mccx.objectReference(consX.apply(propertyLoaders))
                                        .name(consX.apply("computeIfAbsent")).parameter(px -> px.value(consX.apply(propertyName))
                                                .value(v -> v.lambda(lx -> lx.simple().parameters(csx -> csx.value(consX
                                                        .apply("key"))).body(b -> b.methodCall(mcx1 -> mcx1.dis()
                                                        .name(consX.apply(newPropertyLoader)).parameter(px1 -> px1
                                                                .value(consX.apply(loadFromModel))
                                                                .value(consX.apply(loadFromRepo))))))))))
                                .name(consX.apply("ensureLoaded")).parameter(csv -> csv.value(v -> v.methodCall(m -> m
                                        .dis().name(consX.apply(force)).parameter(p -> p.value(consX.apply(force)))))))
                                .withSemiColon()))

                //newPropertyLoader method
                .method(mcx ->
                {
                    String pl = "pl";
                    mcx.access(_public).returnType(rx -> rx.name(propertyLoader)).name(consX.apply(newPropertyLoader))
                            .parameterDeclarations(px -> px.value(v -> v.variable(loadFromModelVar))
                                    .value(v -> v.variable(loadFromRepoVar))).inlineCode(ix -> ix.equation(ex -> ex.left(lx -> lx
                            .variable(v -> v.type(t -> t.name(propertyLoader)).name(consX.apply(pl)))).right(rx -> rx
                            .constructorCall(cx -> cx.name(consX.apply(propertyLoader)))).withSemiColon()))
                            .inlineCode(ix -> ix.equation(ex -> ex.left(lx -> lx.fieldAccess(fx -> fx.instance(consX.apply(pl))
                                    .field(consX.apply(loadFromModel)))).right(consX.apply(loadFromModel))).withSemiColon())
                            .inlineCode(ix -> ix.equation(ex -> ex.left(lx -> lx.fieldAccess(fx -> fx.instance(consX.apply(pl))
                                    .field(consX.apply(loadFromRepo)))).right(consX.apply(loadFromRepo))).withSemiColon())
                            .inlineCode(ix -> ix.return_(rx -> rx.statement(consX.apply(pl))).withSemiColon());
                })

                .method(mcx ->
                {

                    String t = "T";
                    String clVar = "cl";
                    String id = "id";
                    String withRel = "withRelatives";
                    String eagLoaderAction = "eagerLoaderAction";
                    String readAction = "v";
                    String lambdaParameter = "r";
                    String rez = "rez";
                    String en = "en";
                    mcx.access(_protected).genericNotation(g -> g.parameters(p -> p.value(consX.apply(t))))
                            .returnType(rx -> rx.name(t)).name(consX.apply("get")).parameterDeclarations(p -> p
                            .value(v -> v.variable(vx -> vx.type(tx -> tx.name(String.format("Class<%s>", t)))
                                    .name(consX.apply(clVar)))).value(v -> v.variable(vx -> vx.type(tx -> tx.name("int"))
                                    .name(consX.apply(id)))).value(v -> v.variable(vx -> vx.type(tx -> tx.name(bool))
                                    .name(consX.apply(withRel)))).value(v -> v.variable(vx -> vx.type(tx -> tx.name("Consumer")
                                    .genericNotation(gx -> gx.parameters(px -> px.value(vx1 -> vx1.type(a -> a.name(String.format("EagerLoader<%s>", t)))))))
                                    .name(consX.apply(eagLoaderAction)))))
                            .inlineCode(inx -> inx.equation(ex -> ex.left(lx -> lx
                                    .variable(vx -> vx.type(tx -> tx.name("Object[]")).name(consX.apply(rez))))
                                    .right(rx -> rx.constant("new Object[1]")).withSemiColon()))
                            .inlineCode(ix -> ix.methodCall(mx -> mx.objectReference(consX.apply(dataService))
                                    .name(consX.apply("read")).parameter(px -> px.value(vx -> vx.lambda(lx -> lx
                                            .parameters(lpx -> lpx.value(consX.apply(lambdaParameter)))
                                            .inlineCode(a -> a.equation(ex -> ex.left(lax -> lax.variable(vx1 -> vx1
                                                    .type(nx -> nx.name(String.format("DataService.EntityReadAction<%s>", t)))
                                                    .name(consX.apply(readAction)))).right(rx -> rx
                                                    .methodCall(mx1 -> mx1.objectReference(consX.apply(lambdaParameter))
                                                            .name(consX.apply("get")).parameter(px1 -> px1
                                                                    .value(consX.apply(clVar)).value(consX.apply(id))))))
                                                    .withSemiColon())
                                            .inlineCode(a -> a.if_(ifx -> ifx.if_(ifex -> ifex.condition(consX
                                                    .apply(withRel)).inlineCode(ix1 -> ix1.equation(ex -> ex.left(consX
                                                    .apply(readAction)).right(rx -> rx.methodCall(mx1 -> mx1
                                                    .objectReference(consX.apply(readAction)).name(consX.apply("withRelationships")))))
                                                    .withSemiColon()))))
                                            .inlineCode(a -> a.if_(ifx -> ifx.if_(ifex -> ifex.condition(cx -> cx
                                                    .comparison(cox -> cox.symbol("!=").left(consX.apply(eagLoaderAction))
                                                            .right(consX.apply("null")))).inlineCode(ix1 -> ix1
                                                    .methodCall(mx1 -> mx1.objectReference(bx -> bx.methodCall(mc -> mc
                                                            .objectReference(ox -> ox.methodCall(mc2 -> mc2
                                                                    .objectReference(consX.apply(readAction)).name(consX.apply("eagerLoad"))
                                                                    .parameter(pw -> pw.value(consX.apply(eagLoaderAction))))).name(consX.apply("onSuccess"))
                                                            .parameter(p -> p.value(vv -> vv.lambda(lax -> lax.simple()
                                                                    .parameters(i -> i.value(consX.apply(en))).body(box -> box
                                                                            .equation(elx -> elx.left(consX.apply(String.format("%s[0]", rez)))
                                                                                    .right(consX.apply(en)))))))))
                                                            .name(consX.apply("onFailure")).parameter(px1 -> px1.value(vx1 -> vx1
                                                                    .lambda(lx1 -> lx1.body(bx -> bx.methodCall(mx2 -> mx2
                                                                            .objectReference(ox -> ox.fieldAccess(fx -> fx
                                                                                    .instance(consX.apply("System")).field(consX
                                                                                            .apply("out")))).name(consX
                                                                                    .apply("println")).parameter(par -> par
                                                                                    .value(consX.apply("\"proxy data read failure.\"")))))
                                                                            .simple())))).withSemiColon()))))
                                            .bracketsInSeparateLines()))))
                                    .withSemiColon())
                            .inlineCode(ix -> ix.return_(rx -> rx.statement(sx -> sx.cast(cx -> cx.type_(tx -> tx.name(t))
                                    .value(consX.apply(String.format("%s[0]", rez)))))).withSemiColon());
                });


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

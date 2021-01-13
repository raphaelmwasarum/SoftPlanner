package com.raphjava.softplanner.main;

import com.raphjava.softplanner.components.ConsoleDispatcher;
import com.raphjava.softplanner.components.DispatcherWrapper;
import com.raphjava.softplanner.components.interfaces.DispatcherHelper;
import com.raphjava.softplanner.components.interfaces.KeyGenerator;
import com.raphjava.softplanner.data.QumbuqaComponent;
import com.raphjava.softplanner.data.SPEntityToDatabaseMapper;
import com.raphjava.softplanner.data.interfaces.DataService;
import com.raphjava.softplanner.data.models.EntityBase;
import com.raphjava.softplanner.services.LoggingQuDataService;
import net.raphjava.qumbuqa.core.QuMappingBuilder;
import net.raphjava.qumbuqa.core.Qumbuqa;
import net.raphjava.qumbuqa.databasedesign.interfaces.MappingInfo;
import net.raphjava.qumbuqa.databasedesign.interfaces.MappingManager;
import net.raphjava.raphtility.collectionmanipulation.ArrayList;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Collection;
import java.util.List;

public class AppConsole
{
    private static ApplicationContext container;

    public AppConsole(ApplicationContext container)
    {
        this.container = container;

        try
        {
            AppConsole.main(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    static Logger logger = Logger.getLogger(AppConsole.class.getSimpleName());


    @SuppressWarnings("unchecked")
    public static void main(String[] args)
    {
        ensureContainerInstantiation();
        debug("AppConsole is starting...");
        debug("Building startup components...");
        initializeQumbuqa();
        InstanceGetter instanceGetter = (InstanceGetter) container.getBean("instanceGetter");
        instanceGetter.setIocAccessor(s -> container.getBean(s));
        DispatcherWrapper dispatcherHelper = (DispatcherWrapper) container.getBean(DispatcherHelper.class);
        dispatcherHelper.setDispatcherHelper(new ConsoleDispatcher());
        SoftPlannerConsole spco = container.getBean(SoftPlannerConsole.class);
        debug("Building startup components successful.");
        spco.start();
    }

    private static void ensureContainerInstantiation()
    {
        if(container == null)
        {
            container = new ClassPathXmlApplicationContext("Beans_Annotation.xml");
        }

    }

    @SuppressWarnings("unchecked")
    private static void initializeQumbuqa()
    {
        KeyGenerator kg = container.getBean(KeyGenerator.class);
        List<Double> usedOnes = kg.getUsedKeys();
        Qumbuqa qumbuqa = container.getBean(QumbuqaComponent.class).getQumbuqa();
        qumbuqa.initialize();
        QuMappingBuilder mapper = new QuMappingBuilder();
        new SPEntityToDatabaseMapper().accept(mapper);
        MappingManager mappingManager = mapper.createMappingData();
        DataService dataService = container.getBean(LoggingQuDataService.class);
        for (MappingInfo m : mappingManager.getMappings())
        {
            debug("Getting all used ids by " + m.getEntityClass().getSimpleName() + " entities.");
            dataService.read(r -> r.getAll(m.getEntityClass())
                    .onSuccess(es -> usedOnes.addAll(new ArrayList<>((Collection<?>) es)
                            .select(e -> (double) ((EntityBase) e).getId()))));
            debug("Getting all used ids by " + m.getEntityClass().getSimpleName() + " entities successful.");
        }
    }


    private static void debug(String s)
    {
        logger.debug("[" + logger.getName() + "]" + " - " + s);
    }



}

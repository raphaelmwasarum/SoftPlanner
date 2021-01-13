package com.raphjava.softplanner.services;//package com.raphjava.softplanner.services;
//
//
//import data.services.DefaultIOService;
//import data.services.StudeeFileBuilder;
//import net.raphjava.raphtility.logging.interfaces.Log;
//import net.raphjava.raphtility.logging.interfaces.LoggerFactory;
//import com.raphjava.softplanner.components.AbFactoryBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import static com.raphjava.softplanner.annotations.Scope.Singleton;
//
//@Lazy
//@Component
//@Scope(Singleton)
//public class IOServiceBuilder extends AbFactoryBean<DefaultIOService>
//{
//    private StudeeFileBuilder fileBuilder;
//    private LoggerFactory loggerFactory;
//
//    @Autowired
//    public void setFileBuilder(StudeeFileBuilder fileBuilder)
//    {
//        this.fileBuilder = fileBuilder;
//    }
//
//    @Autowired
//    public void setLoggerFactory(LoggerFactory loggerFactory)
//    {
//        this.loggerFactory = loggerFactory;
//    }
//
//    public IOServiceBuilder()
//    {
//        super(DefaultIOService.class);
//    }
//
//    @Override
//    public synchronized DefaultIOService build()
//    {
//        return DefaultIOService.newBuilder().fileBuilder(fileBuilder).logger(getLogger()).build();
//    }
//
//    private Log getLogger()
//    {
//        return loggerFactory.createLogger(DefaultIOService.class.getSimpleName());
//    }
//}

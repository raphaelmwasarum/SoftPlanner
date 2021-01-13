package com.raphjava.softplanner.data;

import net.raphjava.qumbuqa.core.Qumbuqa;
import net.raphjava.raphtility.interfaceImplementations.KeyGenerator;
import net.raphjava.raphtility.reflection.ReflectionHelperImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static com.raphjava.softplanner.annotations.Scope.Singleton;


//import data.models.StudeeEntityToDatabaseMapper;

@Lazy
@Component
@Scope(Singleton)
public class QumbuqaComponent
{


    private static Qumbuqa qumbuqa;

    public synchronized Qumbuqa getQumbuqa()
    {
        return getQ();
    }

    private synchronized static Qumbuqa getQ()
    {
        if(qumbuqa == null)
        {
            String PW = "Sonywalkmanb143f";
            String databaseName = "softplannercrypt";
            SPEntityToDatabaseMapper mapperAction = new SPEntityToDatabaseMapper();
            String ROOTUSERNAME = "root";
            ReflectionHelperImpl reflectionHelper = new ReflectionHelperImpl();
            KeyGenerator keyGenerator = new KeyGenerator();
            HashMap<String, String> databasePWs = new HashMap<String, String>();
            databasePWs.put("information_schema", PW);
            databasePWs.put(databaseName, PW);
            qumbuqa = Qumbuqa.newBuilder()
                    .database(Qumbuqa.DATABASE.SQLite)
//                    .accessorDirectory("C:\\\\Users\\\\Raphael\\\\RaphGooDrive\\\\CodeWorld\\\\JWorld\\\\Studee\\\\src\\\\main\\\\java\\\\net\\\\raphjava\\\\studeeconsole\\\\data\\\\models\\\\accessors\\\\")
                    .accessorDirectory(String.format("%s%s", getAppLocation(), getAccessorsDirectory()))
                    .entityAccessorsPackage("com.raphjava.softplanner.data.models.accessors")
                    .mapperAction(mapperAction)
                    .databaseLifeCycleOptions(Qumbuqa.AUTO_OPTIONS.Ignore)
//                .databaseURL("jdbc:mysql://localhost/")
                    .databaseURL("jdbc:sqlite:" + getAppLocation())
                    .databaseName(databaseName)
                    .userName(ROOTUSERNAME)
                    .reflectionHelper(reflectionHelper)
                    .databasePasswords(databasePWs)
                    .password(PW)
                    .keyGenerator(keyGenerator)
                    .build();
        }

        return qumbuqa;
    }

    private static String getAccessorsDirectory()
    {
        StringBuilder sb = new StringBuilder();
        String s = getCurrentOSFileSeparator();
        sb.append("src").append(s).append("main").append(s).append("java").append(s).append("com").append(s)
                .append("raphjava").append(s).append("softplanner").append(s).append("data")
                .append(s).append("models").append(s).append("accessors").append(s);
        return sb.toString();
//        return "\\\\src\\\\main\\\\java\\\\com\\\\raphjava\\\\studentinformationsystem\\\\data\\\\models\\\\accessors\\\\";
    }

    private static String getAppLocation()
    {
        String separator = getCurrentOSFileSeparator();
        return System.getProperty("user.dir") + separator;

    }


    private static String fileSeparator;


    private static String getCurrentOSFileSeparator()
    {
        return fileSeparator != null ? fileSeparator : loadAppropriateFileSeparator();
    }

    private static String loadAppropriateFileSeparator()
    {
        if(System.getProperty("os.name").toLowerCase().contains("windows"))
        {
            fileSeparator = "\\";
        }
        else fileSeparator = "/";
        return fileSeparator;
    }

    public static void main(String[] args)
    {
        getQ().refresh();

    }
}

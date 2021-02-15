package com.raphjava.softplanner.data;

import com.raphjava.softplanner.components.UserDirectoryResolver;
import net.raphjava.qumbuqa.core.Qumbuqa;
import net.raphjava.raphtility.interfaceImplementations.KeyGenerator;
import net.raphjava.raphtility.reflection.ReflectionHelperImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;

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
                    .accessorDirectory(String.format("%s%s", userDirResolver.getAppRunningRootLocation(), getAccessorsDirectory()))
                    .entityAccessorsPackage("com.raphjava.softplanner.data.models.accessors")
                    .mapperAction(mapperAction)
                    .databaseLifeCycleOptions(Qumbuqa.AUTO_OPTIONS.Ignore)
//                .databaseURL("jdbc:mysql://localhost/")
                    .databaseURL("jdbc:sqlite:" + userDirResolver.getAppRunningRootLocation())
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
//        StringBuilder sb = new StringBuilder();
//        String s = userDirResolver.getCurrentOSFileSeparator();
        return userDirResolver.buildPath(new LinkedHashSet<>(Arrays.asList("src", "main", "java", "com", "raphjava"
                , "softplanner", "data", "models", "accessors")));
        /*sb.append("src").append(s).append("main").append(s).append("java").append(s).append("com").append(s)
                .append("raphjava").append(s).append("softplanner").append(s).append("data")
                .append(s).append("models").append(s).append("accessors").append(s);
        return sb.toString();*/
//        return "\\\\src\\\\main\\\\java\\\\com\\\\raphjava\\\\studentinformationsystem\\\\data\\\\models\\\\accessors\\\\";
    }

    private static UserDirectoryResolver userDirResolver = new UserDirectoryResolver();


    public static void main(String[] args)
    {
        getQ().refresh();

    }
}

package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import net.raphjava.raphtility.collectionmanipulation.Seq;
//import net.raphjava.raphtility.collectionmanipulation.interfaces.Itemizer;
//import net.raphjava.raphtility.utils.ReflectionHelper;
//import net.raphjava.studeeconsole.components.interfaces.KeyGenerator;
//import com.raphjava.softplanner.data.models.EntityBase;
//
//import java.io.File;
//import java.lang.reflect.Field;
//import java.util.*;
//
//public class TCUtils
//{
//
//    private final Itemizer look;
//
//    public Itemizer getLook()
//    {
//        return look;
//    }
//
//    public Seq getSeq()
//    {
//        return seq;
//    }
//
//    private final Seq seq;
//
//    public ReflectionHelper getReflectionHelper()
//    {
//        return reflectionHelper;
//    }
//
//    private final ReflectionHelper reflectionHelper;
//
//    public KeyGenerator getKeyGenerator()
//    {
//        return keyGenerator;
//    }
//
//    private final KeyGenerator keyGenerator;
//
//    private Map<Class, List<String>> entityClassPropertyNames = new HashMap<>();
//
//    public Map<Class, List<String>> getEntityClassPropertyNames()
//    {
//        return entityClassPropertyNames;
//    }
//
//    public TCUtils(KeyGenerator k, Raphtilities args)
//    {
//        keyGenerator = k;
//        reflectionHelper = args.getReflectionHelper();
//        seq = args.getSeq();
//        look = args.getItemizer();
//        loadEntityBasePropertyNames();
//    }
//
//    private void loadEntityBasePropertyNames()
//    {
//        String packageName = "net.com.raphjava.softplanner.data.models";
//        List<String> entityFileNames = getFileNames();
//        if(entityFileNames.size() < 1) return;
//        entityFileNames.forEach(entityFileName ->
//        {
//            if(entityFileName.contains("Repository") || entityFileName.contains("repository")) return;
//            StringBuffer sb = new StringBuffer(packageName).append(".").append(entityFileName);
//            /*String javaString = "/java/";
//            int javaIndex = sb.indexOf(javaString);
//            int afterJavaIndex = javaIndex + javaString.length();*/
//            String possibleEntityClassName = sb.toString().replace("/", ".")
//                    .replace(".java", "");
//            try
//            {
//                Class<?> klass = Class.forName(possibleEntityClassName);
//                loadEntityBasePropertyNames(klass);
//
//            }
//            catch (ClassNotFoundException e)
//            {
//                System.out.println("Cannot build class for this class file: " + entityFileName);
//                e.printStackTrace();
//            }
//        });
//        //TODO Use HibernateMapping code to load entityBaseNames. And then go back to your testing of the AddNewTopicVM.
//    }
//
//    private void loadEntityBasePropertyNames(Class<?> klass)
//    {
//        List<Field> fields = Arrays.asList(klass.getDeclaredFields());
//        if(fields.size() < 1) return;
//        List<String> entityPNames = getEntityPNamesList(klass);
//        fields.forEach(field ->
//        {
//            Class c = field.getType();
//            if(EntityBase.class.isAssignableFrom(c))
//            {
//                entityPNames.add(field.getName());
//                return;
//            }
//            if(Set.class.isAssignableFrom(c))
//            {
//                entityPNames.add(field.getName());
//            }
//        });
//    }
//
//    private List<String> getEntityPNamesList(Class<?> klass)
//    {
//        List<String> l = entityClassPropertyNames.get(klass);
//        if(l == null)
//        {
//            entityClassPropertyNames.put(klass, new ArrayList<>());
//            l = entityClassPropertyNames.get(klass);
//        }
//        return l;
//    }
//
//    private List<String> getFileNames()
//    {
//        String cwd = System.getProperty("user.dir");
//        String modelDir = cwd + "/src/main/java/net/net.raphjava/studeeconsole/models";
//        File modelDirectory = new File(modelDir);
//        List<String> list = asExp(Arrays.asList(Objects.requireNonNull(modelDirectory.listFiles()))).select(File::getName).list();
//        return list;
//    }
//}

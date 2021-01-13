package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import com.raphjava.softplanner.data.models.EntityBase;
//import org.javatuples.Pair;
//import org.javatuples.Triplet;
//
//import javax.xml.stream.XMLEventReader;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.events.Attribute;
//import javax.xml.stream.events.StartElement;
//import javax.xml.stream.events.XMLEvent;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class HibernateMapping
//{
//    private final String NAME = "name";
//    private final String CLASS = "class";
//    private final String PROPERTY = "property";
//    private final String COLUMN = "column";
//    private final String ID = "id";
//    private final String TABLE = "table";
//    private List<Pair<String, String>> propertyColumnMapping;
//    private Map<Triplet<String, String, String>, List<Pair<String, String>>> hibernateMapping;
//    private final String JOINEDSUBCLASS = "joined-subclass";
//    private String KEY = "key";
//    private String ONETOONE = "one-to-one";
//    private boolean baseIDLoaded;
//
//    public HibernateMapping()
//    {
//        propertyColumnMapping = new ArrayList<>();
//        hibernateMapping = new HashMap<>();
//    }
//
//
//    private void loadData()
//    {
//        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//        try
//        {
//            InputStream in = new FileInputStream(Objects.requireNonNull(getClass().getClassLoader().getResource("EntityBase.hbm.xml")).getFile());
//            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
//            Triplet<String, String, String> key = null;
//            while (eventReader.hasNext())
//            {
//                XMLEvent event = eventReader.nextEvent();
//                if(event.isStartElement())
//                {
//                    StartElement startElement = event.asStartElement();
//                    String localPart = startElement.getName().getLocalPart();
//                    if(localPart.equals(CLASS) || localPart.equals(JOINEDSUBCLASS))
//                    {
//                        key = loadClass(startElement.asStartElement());
//                    }
//                    else if(localPart.equals(ID) || localPart.equals(PROPERTY))
//                    {
//                        ensureNonNull(key);
//                        loadPropertyData(startElement.asStartElement(), key);
//                    }
//                    else if(localPart.equals(KEY))
//                    {
//                        ensureNonNull(key);
//                        loadJoinedSubClassPropertyData(startElement.asStartElement(), key);
//                    }
//                    else if(localPart.equals(ONETOONE))
//                    {
//                        ensureNonNull(key);
//                        loadOneToOneData(startElement.asStartElement(),key);
//
//                    }
//                }
//            }
//            updateSubClassesWithAppropriateColumnName();
//        }
//        catch (IOException | XMLStreamException e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    private void updateSubClassesWithAppropriateColumnName()
//    {
//        //Get entityBase mapping
//        Triplet<String, String, String> key = hibernateMapping.keySet().stream().filter(k -> k.getValue0().equals(EntityBase.class.getName())).findFirst().get();
//
//        //Get its id column name value
//        List<Pair<String, String>> list = hibernateMapping.get(key);
//        String columnID = list.stream().filter(p -> p.getValue0().equals(ID)).findFirst().get().getValue1();
//
//        //Get all keys that are subclasses
//        List<Triplet<String, String, String>> sub = hibernateMapping.keySet().stream().filter(k -> k.getValue2().equals(JOINEDSUBCLASS)).collect(Collectors.toList());
//
//        //Update their id values
//        sub.forEach(k ->
//        {
//            List<Pair<String, String>> mlist = hibernateMapping.get(k);
//            Pair<String, String> idMapping = mlist.stream().filter(m -> m.getValue0().equals(ID)).findFirst().get();
//            Pair<String, String> newIdMapping = idMapping.setAt1(columnID);
//            mlist.remove(idMapping);
//            mlist.add(newIdMapping);
//        });
//
//    }
//
//    private void loadOneToOneData(StartElement startElement, Triplet<String, String, String> key)
//    {
//        String name = null;
//        Iterator<Attribute> attributes = startElement.getAttributes();
//        while (attributes.hasNext())
//        {
//            Attribute att = attributes.next();
//            if(att.getName().getLocalPart().equals(NAME)) name = att.getValue();
//        }
//
//        Objects.requireNonNull(name);
//        List<Pair<String, String>> currentPropList = hibernateMapping.get(key);
//        currentPropList.add(Pair.with(name, name));
//    }
//
//    private void ensureNonNull(Triplet<String, String, String> key)
//    {
//        Objects.requireNonNull(key, "Cannot identify the class whose mapping data is about to be saved ");
//    }
//
//    public<T> List<Pair<String, String>> getClassPropertyColumnMapping(Class<T> entityClass)
//    {
//        if(hibernateMapping.size() < 1) loadData();
//        List<Triplet<String, String, String>> res = hibernateMapping.keySet().stream().filter(p -> p.getValue0().equals(entityClass.getName())).collect(Collectors.toList());
//        List<Pair<String, String>> list = res != null && res.size() > 0 ? hibernateMapping.get(res.get(0)) : null;
//        return list;
//    }
//
//    private Triplet<String, String, String> loadClass(StartElement startElement)
//    {
//        String className = null;
//        String tableName = null;
//        Iterator<Attribute> attributes = startElement.getAttributes();
//        while (attributes.hasNext())
//        {
//            Attribute att = attributes.next();
//            if(att.getName().getLocalPart().equals(NAME)) className = att.getValue();
//            else if( att.getName().getLocalPart().equals(TABLE)) tableName = att.getValue();
//            //if(!className.isBlank() && !tableName.isBlank()) break;
//        }
//        Objects.requireNonNull(className);
//        Objects.requireNonNull(tableName);
//        Triplet<String, String, String> key = Triplet.with(className, tableName, startElement.getName().getLocalPart());
//        hibernateMapping.put(key, new ArrayList<>());
//        return key;
//    }
//
//    private void loadJoinedSubClassPropertyData(StartElement startElement, Triplet<String, String, String> key)
//    {
//        List<Pair<String, String>> currentPropList = hibernateMapping.get(key);
//        currentPropList.add(Pair.with(ID, ID));
//    }
//
//    private void loadPropertyData(StartElement startElement, Triplet<String, String, String> key)
//    {
//        String propName = null;
//        String columnName = null;
//        Iterator<Attribute> attributes = startElement.getAttributes();
//        while (attributes.hasNext())
//        {
//            Attribute att = attributes.next();
//            if(att.getName().getLocalPart().equals(NAME)) propName = att.getValue();
//            else if(att.getName().getLocalPart().equals(COLUMN)) columnName = att.getValue();
//        }
//
//        Objects.requireNonNull(propName);
//        Objects.requireNonNull(columnName);
//        List<Pair<String, String>> currentPropList = hibernateMapping.get(key);
//        currentPropList.add(Pair.with(propName, columnName));
//    }
//}

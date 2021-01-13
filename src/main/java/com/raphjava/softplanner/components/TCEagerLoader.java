package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import net.raphjava.studeeconsole.components.interfaces.EagerLoader;
//import net.raphjava.studeeconsole.components.interfaces.KeyGenerator;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class TCEagerLoader<TEntity> implements EagerLoader<TEntity>
//{
//    //private final Session sessionSupplier;
//    private final String fromClause;
//    private final Class<TEntity> entityClass;
//    private final String criterionClause;
//    private final KeyGenerator keyGenerator;
//    private final TCCriteriaBuilder<TEntity> criteriaBuilder;
//    Map<String, AbstractMap.SimpleEntry<String, String>> data;
//    List<List<String>> parentPropertyAlias;
//    private String leftJoinFetch = "LEFT JOIN FETCH";
//    private String space = " ";
//    private String period =".";
//    private String root = "rootEntity";
//    private Map<Double, String> workSpace;
//    private String underScore = "_";
//    private Map<String, Object> parameters;
//
//    public Map<String, Object> getParameters()
//    {
//        return parameters;
//    }
//    /* public TCEagerLoader(Class<TEntity> entityClass, KeyGenerator k, String criterionClause)
//    {
//        keyGenerator = k;
//        //this.sessionSupplier = sessionSupplier;
//        this.entityClass = entityClass;
//        root = entityClass.getSimpleName().toLowerCase() + underScore + getKey();
//        data = new HashMap<>();
//        fromClause = "FROM" + space + this.entityClass.getSimpleName() + space + root;
//        this.criterionClause = criterionClause;
//        workSpace = new HashMap<>();
//        parentPropertyAlias = new ArrayList<>();
//
//
//    }*/
//
//    public TCEagerLoader(TCCriteriaBuilder<TEntity> cb, KeyGenerator k)
//    {
//        criteriaBuilder = cb;
//        root = cb.getRootAlias();
//        keyGenerator = k;
//        this.entityClass = cb.getEntityClass();
//        this.fromClause = "FROM" + space + this.entityClass.getSimpleName() + space + root;
//        this.criterionClause = cb.getCriterionClause();
//        workSpace = new HashMap<>();
//        parameters = cb.getQueryParameters();
//        parentPropertyAlias = new ArrayList<>();
//        data = new HashMap<>();
//    }
//
//    private int getKey()
//    {
//        return (int) keyGenerator.getKey();
//    }
//
//    @Override
//    public void include(String path)
//    {
//        Objects.requireNonNull(path);
//        if(!path.contains(period))
//        {
//            String alias = path + underScore + getKey();
//            if(AliasIsExistent(path, entityClass.getSimpleName(), 0))
//            {
//                alias = getAlias(path, entityClass.getSimpleName(), 0);
//                return;
//            }
//            String partQ = space + leftJoinFetch + space +  root + period + path + space + alias;
//            data.put(alias, new AbstractMap.SimpleEntry<>(partQ, root));
//            List<String> list = new ArrayList<>();
//            list.add(0, entityClass.getSimpleName());
//            list.add(1, path);
//            list.add(2, alias);
//            list.add(3, Integer.toString(0));
//            parentPropertyAlias.add(list);
//            return;
//        }
//        List<String> pathLets = Arrays.asList(path.split("\\."));
//        List<String> empties = pathLets.stream().filter(String::isEmpty).collect(Collectors.toList());
//        empties.forEach(pathLets::remove);
//        String currentParentTypeName = entityClass.getSimpleName();
//        String parentAlias = root;
//        int level = 0;
//        for(String pathLet : pathLets)
//        {
//            if(AliasIsExistent(pathLet, currentParentTypeName, level /*If you use indexOf method here you may not get the intended index because sometimes pathLets are equal.*/))
//            {
//                parentAlias = getAlias(pathLet, currentParentTypeName, level);
//                currentParentTypeName = pathLet;
//                continue;
//            }
//            parentAlias = createHQL(pathLet, parentAlias, currentParentTypeName, level);
//            currentParentTypeName = pathLet;
//            level++;
//        }
//    }
//
//    private String createHQL(String pathLet, String parentAlias, String parentTypeName, int level)
//    {
//        String alias = pathLet + underScore + getKey();
//        String partQ = space + leftJoinFetch + space +  parentAlias + period + pathLet + space + alias;
//        data.put(alias, new AbstractMap.SimpleEntry<>(partQ, parentAlias));
//        List<String> list = new ArrayList<>();
//        list.add(0, parentTypeName);
//        list.add(1, pathLet);
//        list.add(2, alias);
//        list.add(3, Integer.toString(level));
//        parentPropertyAlias.add(list);
//        return alias;
//    }
//
//    private String getAlias(String pathLet, String currentParentTypeName, int level)
//    {
//        List<List<String>> res = parentPropertyAlias.stream().filter(l -> l.get(0).equals(currentParentTypeName) && l.get(1).equals(pathLet) && Integer.parseInt(l.get(3)) == level).collect(Collectors.toList());
//        List<String> list = res.get(0);
//        return  list.get(2);
//    }
//
//    private boolean AliasIsExistent(String pathLet, String currentParentTypeName, int level)
//    {
//        boolean existent = parentPropertyAlias.stream().anyMatch(l -> l.get(0).equals(currentParentTypeName) && l.get(1).equals(pathLet) && Integer.parseInt(l.get(3)) == level);
//        return existent;
//    }
//
//    public void include(String propertyName, String alias, String... parentAlias)
//    {
//        if(parentAlias.length == 0)
//        {
//            //" LEFT JOIN root.propertyName alias"
//            String partQ = space + leftJoinFetch + space +  root + period + propertyName + space + alias;
//            data.put(alias, new AbstractMap.SimpleEntry<>(partQ, root));
//            return;
//        }
//
//    }
//
////    @Override
////    public void load(String entityName, String propertyName, boolean... loadAsAGraph )
////    {
////
////    }
//
//    public String buildHQL()
//    {
//        double workSpaceID = getKey();
//        List<String> rootClausesKeys = data.keySet().stream().filter(key -> data.get(key).getValue().equals(root)).collect(Collectors.toList());
//        //hql += buildHQL(rootKey, data.get(rootKey))
//        rootClausesKeys.forEach(rootKey -> generateHQL(workSpaceID, rootKey, data.get(rootKey)));
//        String partQ = workSpace.get(workSpaceID);
//        partQ = partQ != null ? partQ : "";
//        String finalHQL = fromClause + space +  partQ + space + criterionClause;
//        return finalHQL;
//    }
//
//    private String buildHQL(String key, AbstractMap.SimpleEntry<String, String> args)
//    {
//        String hql = args.getKey();
//        double workSpaceID = getKey();
//        List<String> kidsKeys = data.keySet().stream().filter(k -> data.get(k).getValue().equals(key)).collect(Collectors.toList());
//        if(kidsKeys.size() != 0)
//        {
//            List<AbstractMap.SimpleEntry<String, String>> kids = new ArrayList<>();
//            kidsKeys.forEach(kidKey -> kids.add(data.get(kidKey)));
//            kidsKeys.forEach(kidKey -> generateHQL(workSpaceID, kidKey, data.get(kidKey)));
//            hql += space + workSpace.get(workSpaceID);
//            return hql;
//        }
//        return hql;
//    }
//
//    private void generateHQL(double workSpaceID, String key, AbstractMap.SimpleEntry<String, String> kid)
//    {
//        if(!workSpace.containsKey(workSpaceID))
//        {
//            workSpace.put(workSpaceID, buildHQL(key, kid));
//            return;
//        }
//        String currentHQL = workSpace.get(workSpaceID);
//        workSpace.remove(workSpaceID);
//        workSpace.put(workSpaceID, currentHQL + space + buildHQL(key, kid));
//    }
//
//
//
//
//}

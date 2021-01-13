package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.CriteriaBuilder;
import com.raphjava.softplanner.components.interfaces.KeyGenerator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.hibernate.Session;
//import org.hibernate.query.Query;

public class TCCriteriaBuilder<TEntity> implements CriteriaBuilder<TEntity>
{
    private final Class<TEntity> entityClass;

    public Class<TEntity> getEntityClass()
    {
        return entityClass;
    }

    private final KeyGenerator keyGenerator;
    private Map<String, String> userReferencedCriteriaData;

//    private final Supplier<Session> sessionSupplier;

    private String fromClause = "" ;

    private String criterionClauseConditions = "";
    private String less_ThanEqual = "<=";
    private String greater_ThanEqual = ">=";
    private String colon = ":";

    public String getCriterionClause()
    {
        return where + space + criterionClauseConditions;
    }

    public String getFromClause()
    {
        return fromClause;
    }

    private String rootAlias = "rootEntity";

    public String getRootAlias()
    {
        return rootAlias;
    }


    private String where = "WHERE";
    private String space = " ";
    private String period = ".";
    private String less_Than = "<";
    private String equal = "=";
    private String greater_Than = ">";
//    private Query<TEntity> query = null;
    private String like = "LIKE";
    private String percentSign = "%";
    private String is_Null = "is null";
    private String and = "and";
    private String _or = "or";

    private Map<String, Object> queryParameters;

    public Map<String, Object> getQueryParameters()
    {
        return queryParameters;
    }

    public TCCriteriaBuilder(/*Supplier<Session> session, */Class<TEntity> entityClass, String rootEntityAlias, KeyGenerator k)
    {
        keyGenerator = k;
        rootAlias = rootEntityAlias;
        this.entityClass = entityClass;
//        this.sessionSupplier = session;
        userReferencedCriteriaData = new HashMap<String, String>();
        fromClause = "FROM" + space + entityClass.getSimpleName() + space + rootAlias;
        queryParameters = new HashMap<String, Object>();

    }

    public TCCriteriaBuilder(Class<TEntity> entityClass, /*Supplier<Session> session, */String fromClause, String root, KeyGenerator k)
    {
        keyGenerator = k;
        this.entityClass = entityClass;
//        this.sessionSupplier = session;
        this.fromClause = fromClause;
        this.rootAlias = root;
        queryParameters = new HashMap<String, Object>();


    }

    private int getKey()
    {
        return (int) keyGenerator.getKey();
    }


    @Override
    public <T> void lessThan(String propertyName, T value, String... criterionName)
    {
        String parameterAlias = saveParameterThenGetParameterAlias(value);
        String partQ = rootAlias + period + propertyName + space + less_Than + space + colon + parameterAlias;
        //String hql = fromClause + where + space + partQ;
        updateCriterion(partQ, criterionName);
       /* if(criterionName == null) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);*/
    }

    private void updateCriterion(String partQ, String...criterionName)
    {
        if(criterionName == null || criterionName.length < 1) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);
    }

    private <T> String saveParameterThenGetParameterAlias(T value)
    {
        String queryParameterReference = "parameter_";
        queryParameterReference += String.valueOf(getKey());
        queryParameters.put(queryParameterReference, value);
        return queryParameterReference;
    }

    @Override
    public <T> void lessThanEqual(String propertyName, T value, String... criterionName)
    {
        String parameterAlias = saveParameterThenGetParameterAlias(value);
        String partQ = rootAlias + period + propertyName + space + less_ThanEqual + space + colon + parameterAlias;
        //String hql = fromClause + where + space + partQ;
        updateCriterion(partQ, criterionName);
        /*if(criterionName == null) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);*/
    }

    @Override
    public <T> void equalTo(String propertyName, T value, String... criterionName)
    {
        String parameterAlias = saveParameterThenGetParameterAlias(value);
        String partQ = rootAlias + period + propertyName + space + equal + space + colon + parameterAlias;
        //String hql = fromClause + where + space + partQ;
        updateCriterion(partQ, criterionName);
        /*if(criterionName == null) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);*/
    }

    @Override
    public <T> void greaterThan(String propertyName, T value, String... criterionName)
    {
        String parameterAlias = saveParameterThenGetParameterAlias(value);
        String partQ = rootAlias + period + propertyName + space + greater_Than + space + colon + parameterAlias;
        //String hql = fromClause + where + space + partQ;
        updateCriterion(partQ, criterionName);
        /*if(criterionName == null) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);*/
    }

    @Override
    public <T> void greaterThanEqual(String propertyName, T value, String... criterionName)
    {
        String parameterAlias = saveParameterThenGetParameterAlias(value);
        String partQ = rootAlias + period + propertyName + space + greater_ThanEqual + space + colon + parameterAlias;
        //String hql = fromClause + where + space + partQ;
        updateCriterion(partQ, criterionName);
        /*if(criterionName == null) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);*/

    }

    @Override
    public void contains(String propertyName, String value, boolean caseSensitive, String... criterionName)
    {
        if(caseSensitive) value = value.toLowerCase();
        String partQ = rootAlias + period + propertyName + space + like + space + percentSign + value + percentSign;
        //String hql = fromClause + where + space + partQ;
        updateCriterion(partQ, criterionName);
        /*if(criterionName == null) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);*/
    }

    @Override
    public void startsWith(String propertyName, String value, boolean caseSensitive, String... criterionName)
    {
        if(caseSensitive) value = value.toLowerCase();
        String partQ = rootAlias + period + propertyName + space + like + space +  value + percentSign;
        //String hql = fromClause + where + space + partQ;
        updateCriterion(partQ, criterionName);
        /*if(criterionName == null) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);*/

    }

    @Override
    public void endsWith(String propertyName, String value, boolean caseSensitive, String... criterionName)
    {
        if(caseSensitive) value = value.toLowerCase();
        String partQ = rootAlias + period + propertyName + space + like + space + percentSign + value;
        //String hql = fromClause + where + space + partQ;
        updateCriterion(partQ, criterionName);
        /*if(criterionName == null) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);*/
    }

    @Override
    public <T> void isNull(String propertyName, String... criterionName)
    {
        String partQ = rootAlias + period + propertyName + space + is_Null;
        //String hql = fromClause + where + space + partQ;
        updateCriterion(partQ, criterionName);
        /*if(criterionName == null) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);*/
    }

    @Override
    public <T> void isNotNull(String propertyName, String... criterionName)
    {
        String partQ = rootAlias + period + propertyName + space + is_Null;
        //String hql = fromClause + where + space + partQ;
        updateCriterion(partQ, criterionName);
        /*if(criterionName == null) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);*/

    }

    @Override
    public void or(String criterionName1, String criterionName2, String... criterionName)
    {
        String partQ = userReferencedCriteriaData.get(criterionName1) + space + _or + space + userReferencedCriteriaData.get(criterionName2);
        //String hql = fromClause + space + partQ;
        updateCriterion(partQ, criterionName);
        /*if(criterionName == null) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);*/

    }

    @Override
    public void and(String criterionName1, String criterionName2, String... criterionName)
    {
        String partQ = userReferencedCriteriaData.get(criterionName1) + space + and + space + userReferencedCriteriaData.get(criterionName2);
        //String hql = fromClause + space + partQ;
        updateCriterion(partQ, criterionName);
        /*if(criterionName == null || criterionName.length < 1) criterionClauseConditions = partQ;
        if(criterionName.length != 0) userReferencedCriteriaData.put(criterionName[0], partQ);*/
    }

    public List<TEntity> Build()
    {
//        Query<TEntity> q = sessionSupplier.get().createQuery(fromClause + space + criterionClauseConditions, entityClass);
//        return q.getResultList();
        return null;
    }

    public String buildHQL()
    {
        return fromClause + space + where + space + criterionClauseConditions;
    }
}

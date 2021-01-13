package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.Factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TCFactory<Product> implements Factory<Product>
{
    private String[] parameterTypeNames;
    protected Class[] constructorParameters;
    protected Map<Integer, Object> components;

    private Object[] sortedArgs;

    private String fullyQualifiedName;

    boolean constructorHasParameters = false;

    public TCFactory(String fullyQualifiedName, String[] fullyQualifiedParameterTypeNames, Map<Integer, Object> components)
    {
        parameterTypeNames = fullyQualifiedParameterTypeNames;
        constructorHasParameters = true;
        this.fullyQualifiedName = fullyQualifiedName;
        this.components = components;

        //loadParameterClassTypes();
    }

    private void loadParameterClassTypes()
    {
        List<Class> types = new ArrayList<Class>();
        List<Class> parameterTypeStrings = new ArrayList<Class>();

        for(String typeString : parameterTypeNames)
        {
            try
            {
                types.add(Class.forName(typeString));
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }

        }
        constructorParameters = types.toArray(new Class[]{});

        List<Integer> keys = components.keySet().stream().sorted().collect(Collectors.toList());
        List list = new ArrayList();
        keys.forEach(key -> list.add(components.get(key)));
        sortedArgs = list.toArray();
    }

    public TCFactory(String fullyQualifiedName)
    {
        this.fullyQualifiedName = fullyQualifiedName;
    }




    @Override
    public Product createProduct()
    {
        try
        {


            Class<Product> productClass = (Class<Product>) Class.forName(fullyQualifiedName);
            if(constructorHasParameters)
            {
                if(constructorParameters == null) loadParameterClassTypes();
                //Class[] parameterTypes = Arrays.stream(sortedArgs).map(arg -> arg.getClass()).collect(Collectors.toList()).toArray(new Class[]{});

                Constructor<?> constructor = productClass.getConstructor(constructorParameters);
                Product product = (Product) constructor.newInstance(sortedArgs);
                return product;
            }
            Constructor<?> constructor = productClass.getConstructor();
            Product product = (Product) constructor.newInstance();
            return product;

        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e)
        {
            e.printStackTrace();
        }
        return  null;
    }

    @Override
    public Product createProduct(Map<Integer, Object> otherConstructorArgs)
    {
        List<Integer> indices = new ArrayList<Integer>();
        indices.addAll(components.keySet());
        indices.addAll(otherConstructorArgs.keySet());
        List<Integer> sortedIndices = indices.stream().sorted().collect(Collectors.toList());
        List sortedArguments = new ArrayList();
        sortedIndices.forEach(index -> sortedArguments.add(components.containsKey(index) ? components.get(index) : otherConstructorArgs.get(index)));
        try
        {
            Class<Product> productClass = (Class<Product>) Class.forName(fullyQualifiedName);
            List<Class> pTypes = new ArrayList<Class>();
            sortedArguments.forEach(a -> pTypes.add(a.getClass()));
            Constructor<?> constructor = productClass.getConstructor(pTypes.toArray(new Class[]{ }));
            Product product = (Product) constructor.newInstance(sortedArgs);
            return product;
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchMethodException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        catch (InstantiationException e)
        {
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deleteProduct(Product product)
    {
        System.out.println("You're yet to implement this. At the time I figured if it's not in the Spring IOC, all you need to do is just dereference it everywhere you used it. GC will then do the rest.");
    }
}

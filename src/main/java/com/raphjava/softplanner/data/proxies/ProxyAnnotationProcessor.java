package com.raphjava.softplanner.data.proxies;

import com.raphjava.softplanner.components.SystemExit;
import com.raphjava.softplanner.components.UserDirectoryResolver;
import com.raphjava.softplanner.data.proxies.annotations.Proxied;
import com.sun.xml.internal.txw2.IllegalAnnotationException;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class ProxyAnnotationProcessor
{
    private Collection<ProxyMetaData> proxyEntities = new HashSet<>();

    private String entitiesModelPackage;

    private ProxyAnnotationProcessor(Builder builder)
    {
        entitiesModelPackage = builder.entitiesModelPackage;
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }


    public Collection<ProxyMetaData> getProxyEntities()
    {
        return proxyEntities;
    }

    public void processAnnotations()
    {
        Collection<Class<?>> proxiedClasses = getProxiedClasses();
        createMetaData(proxiedClasses);
    }

    private void createMetaData(Collection<Class<?>> proxiedClasses)
    {
        for (Class clazz : proxiedClasses) createClassMetaData(clazz);
    }

    private void createClassMetaData(Class proxiedEntity)
    {
        ProxyMetaData metaData = new ProxyMetaData();
        metaData.entityClass = proxiedEntity;
        loadProxiedProperties(proxiedEntity, metaData);
        proxyEntities.add(metaData);
    }

    private void loadProxiedProperties(Class proxiedEntity, ProxyMetaData metaData)
    {
        Collection<Method> proxiedMethods = new net.raphjava.raphtility.collectionmanipulation.ArrayList<>(Arrays.asList(proxiedEntity.getMethods()))
                .where(m -> m.isAnnotationPresent(Proxied.class)).list();



        for (Method method : proxiedMethods)
        {
            if(method.getReturnType() == Void.TYPE)
            {
                throw new IllegalAnnotationException(String.format("Annotation \"@%s\" cannot be associated with a method with a void " +
                        "return type. Check method \"%s\" in class \"%s\"", Proxied.class.getSimpleName(), method.getName(), proxiedEntity.getSimpleName()));
            }

            metaData.getProxiedProperties().add(method.getName());
        }
    }

    private Collection<Class<?>> getProxiedClasses()
    {
        //reflect to find proxied classes.
        return new net.raphjava.raphtility.collectionmanipulation.ArrayList<>(getClasses()).where(c -> c
                .isAnnotationPresent(Proxied.class)).list();
    }

    private static UserDirectoryResolver userDirResolver = new UserDirectoryResolver();


    private List<Class<?>> getClasses()
    {
        String path = entitiesModelPackage.replace(".", userDirResolver.getCurrentOSFileSeparator());
        List<Class<?>> classes = new ArrayList<>();
        String[] classPathEntries = System.getProperty("java.class.path").split(System.getProperty("path.separator"));
        String name;
        for (String classpathEntry : classPathEntries)
        {
            if (classpathEntry.endsWith(".jar"))
            {
                File jar = new File(classpathEntry);
                try
                {
                    JarInputStream is = new JarInputStream(new FileInputStream(jar));
                    JarEntry entry;
                    while ((entry = is.getNextJarEntry()) != null)
                    {
                        name = entry.getName();
                        if (name.endsWith(".class"))
                        {
                            if (name.contains(path) && name.endsWith(".class"))
                            {
                                String classPath = name.substring(0, entry.getName().length() - 6);
                                classPath = classPath.replaceAll("[\\|/]", ".");
                                classes.add(Class.forName(classPath));
                            }
                        }
                    }
                }
                catch (Exception ex)
                {
                    // Silence is gold
                }
            }
            else
            {
                try
                {
                    File base = new File(classpathEntry + File.separatorChar + path);
                    for (File file : Objects.requireNonNull(base.listFiles()))
                    {
                        name = file.getName();
                        if (name.endsWith(".class"))
                        {
                            name = name.substring(0, name.length() - 6);
                            classes.add(Class.forName(entitiesModelPackage + "." + name));
                        }
                    }
                }
                catch (Exception ex)
                {
                    // Silence is gold
                }
            }
        }

        return classes;
    }


    public class ProxyMetaData
    {
        /**
         * Returns a string representation of the object. In general, the
         * {@code toString} method returns a string that
         * "textually represents" this object. The result should
         * be a concise but informative representation that is easy for a
         * person to read.
         * It is recommended that all subclasses override this method.
         * <p>
         * The {@code toString} method for class {@code Object}
         * returns a string consisting of the name of the class of which the
         * object is an instance, the at-sign character `{@code @}', and
         * the unsigned hexadecimal representation of the hash code of the
         * object. In other words, this method returns a string equal to the
         * value of:
         * <blockquote>
         * <pre>
         * getClass().getName() + '@' + Integer.toHexString(hashCode())
         * </pre></blockquote>
         *
         * @return a string representation of the object.
         */
        @Override
        public String toString()
        {
            return String.format("Entity \"%s\" should have a data access proxy. Proxied properties: %s"
                    , entityClass == null ? "" : entityClass.getSimpleName()
                    , proxiedProperties.isEmpty() ? "None." : new net.raphjava.raphtility.collectionmanipulation
                            .ArrayList<>(proxiedProperties).selectToObject(new StringBuilder(), (sb, str) -> sb
                            .append(String.format("[%s]. ", str))).toString());

        }

        private Class entityClass;

        private Collection<String> proxiedProperties = new HashSet<>();

        public Class getEntityClass()
        {
            return entityClass;
        }

        public Collection<String> getProxiedProperties()
        {
            return proxiedProperties;
        }
    }

    public static final class Builder
    {
        private String entitiesModelPackage;

        private Builder()
        {
        }


        public ProxyAnnotationProcessor build()
        {
            return new ProxyAnnotationProcessor(this);
        }

        public Builder entitiesModelPackage(String entitiesModelPackage)
        {
            this.entitiesModelPackage = entitiesModelPackage;
            return this;
        }
    }
}

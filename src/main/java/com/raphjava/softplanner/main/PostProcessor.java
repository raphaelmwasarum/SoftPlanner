package com.raphjava.softplanner.main;

import com.raphjava.softplanner.components.AbFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class PostProcessor implements BeanPostProcessor
{
    Logger logger = Logger.getLogger(getClass().getSimpleName());
    private String loggerName = "[" + getClass().getSimpleName() + "]";

    private ApplicationContext context;

    @Autowired
    public void setContext(ApplicationContext context)
    {
        this.context = context;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
    {
        logger.debug(loggerName + " - " + bean.getClass().getSimpleName() + " instance is about to be created. Name in spring configuration: " + beanName);
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        logger.debug(loggerName + " - " + bean.getClass().getSimpleName() + " has been created successfully. Name in spring configuration: " + beanName);
        if(bean instanceof AbFactoryBean)
        {
            Class<?> claz = ((AbFactoryBean) bean).getObjectType();
            if(((AbFactoryBean) bean).isEarlyRiser())
            {
                Object b = context.getBean(claz);
                logger.debug("Early riser " + b + " successfully created.");
            }

        }

        return bean;
    }
}
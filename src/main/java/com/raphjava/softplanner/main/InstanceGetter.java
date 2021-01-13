package com.raphjava.softplanner.main;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.raphjava.softplanner.annotations.Scope.Singleton;


@Lazy
@Scope(Singleton)
@Component
public class InstanceGetter implements Function<String, Object>
{

    public void setIocAccessor(Function<String, Object> iocAccessor)
    {
        this.iocAccessor = iocAccessor;
    }

    private Function<String, Object> iocAccessor;

    /**
     * Applies this function to the given argument.
     *
     * @param s the function argument
     * @return the function result
     */
    @Override
    public Object apply(String s)
    {
        return iocAccessor.apply(s);
    }
}

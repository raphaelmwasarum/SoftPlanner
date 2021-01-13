package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.interfaces.KeyGenerator;
import net.raphjava.raphtility.interfaceImplementations.ForgetfullKeyGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.raphjava.softplanner.annotations.Scope.Singleton;

@Lazy
@Service
@Scope(Singleton)
public class TCKeyGenerator extends ForgetfullKeyGenerator implements KeyGenerator
{


    public TCKeyGenerator(/*double seed, List<Double> numbersToSkip*/) throws Exception
    {
        super(1, new ArrayList<>());
    }



}

//TODO Continue from here. usedKeys singleton not getting injected properlly.
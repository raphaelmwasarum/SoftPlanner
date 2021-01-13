package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import net.raphjava.raphtility.asynchrony.Task;
//import net.raphjava.raphtility.collectionmanipulation.Seq;
//import net.raphjava.raphtility.collectionmanipulation.interfaces.Itemizer;
//import net.raphjava.raphtility.utils.ReflectionHelper;
//
//import java.util.function.Consumer;
//
//public class Raphtilities
//{
//
//    private final ReflectionHelper reflectionHelper;
//
//    private final Itemizer sequenceManipulator;
//
//    public Itemizer getItemizer()
//    {
//        return sequenceManipulator;
//    }
//
//    public Seq getSeq()
//    {
//        return seq;
//    }
//
//    private final Seq seq;
//
////    public ReflectionHelper getReflectionHelper()
////    {
////        return reflectionHelper;
////    }
//
//    public Raphtilities(ReflectionHelper r, Seq s, Itemizer sMan)
//    {
//        reflectionHelper = r;
//        seq = s;
//        sequenceManipulator = sMan;
//        //TODO Continue from here. Deprecate seq and start using sequenceManipulator. Better API for manipulating lists.
//    }
//
//
//    public Task startNewTask(Consumer action)
//    {
//        Task task = TCTask.startNew(action);
//        return task;
//    }
//
//}

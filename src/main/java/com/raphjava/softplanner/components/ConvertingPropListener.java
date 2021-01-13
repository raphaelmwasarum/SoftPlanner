package com.raphjava.softplanner.components;//package net.raphjava.studeeconsole.components;
//
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import net.raphjava.studeeconsole.components.interfaces.ValueConverter;
//import net.raphjava.studeeconsole.components.exceptions.ConversionFailureException;
//import net.raphjava.studeeconsole.components.interfaces.Prop;
//import net.raphjava.studeeconsole.interfaces.Log;
//import net.raphjava.studeeconsole.viewModels.TheViewModelBase;
//
//public class ConvertingPropListener<Source, Target> implements ChangeListener<Source>
//{
//    private final TCWeakReference<TheViewModelBase> viewModel;
//    private final Prop<Target> property;
//    private final ValueConverter<Source, Target> converter;
//    private final Log logger;
//
//    public ConvertingPropListener(TCWeakReference<TheViewModelBase> vm, Log logger, Prop<Target> vmProperty, ValueConverter<Source, Target> converter)
//    {
//        viewModel = vm;
//        this.logger = logger;
//        property = vmProperty;
//        this.converter = converter;
//    }
//
//    @Override
//    public void changed(ObservableValue<? extends Source> observable, Source oldValue, Source newValue)
//    {
//        if(oldValue != newValue)
//        {
//            try
//            {
//                Target data = convert(newValue, newValue, property.get());
//                property.set(data);
//            }
//            catch (ConversionFailureException e)
//            {
//                if(!viewModel.isAlive()) return;
//                logger.debug(viewModel.getReferent().getFormattedLoggerName() + " - Silent Conversion error in the property system. Property: " + property.getPropertyName() + "\nDetails:\n" + e.getMessage());
//                property.set(null);
//            }
//            viewModel.getReferent().handlePropertyChanges(property.getPropertyName());
//        }
//    }
//
//    private Target convert(Source newValue, Source data1, Target data2) throws ConversionFailureException
//    {
//        return converter.convert(newValue).orElseThrow(() -> new ConversionFailureException("Error in conversion. Converter: " + converter.getClass().getSimpleName() + ". Data 1:" + data1 + ". Data 2: " + data2));
//    }
//}

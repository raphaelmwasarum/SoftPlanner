package com.raphjava.softplanner.components.interfaces;//package net.raphjava.studeeconsole.components.interfaces;
//
///**
// * An object that notifies objects(Notifiables) interested of its changes every time they happen.
// */
//public interface NotifyingValue<T>
//{
//
//    void set(T value);
//
//    T get();
//
//    /** Adds a notifiable that will be informed every time this value changes. Please note that this value
//     * will maintain a strong reference of the notifiable so to avoid memory leaks, use the removeNotifiable method.
//     * @param notifiable The new notifiable.
//     */
//    void addNotifiable(Notifiable<T> notifiable);
//
//
//    /** Adds a notifiable that will be informed every time this value changes, but through weak referencing so that the
//     * notifiable is eligible for garbage collection if it is referenced by this notifying value only.
//     * @param notifiable The new notifiable.
//     */
//    void addWeakNotifiable(Notifiable<T> notifiable);
//
//    void removeNotifiable(Notifiable<T> notifiable);
//
//    void removeNotifiables();
//
//}

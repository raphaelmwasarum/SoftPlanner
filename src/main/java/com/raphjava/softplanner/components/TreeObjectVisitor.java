package com.raphjava.softplanner.components;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

public class TreeObjectVisitor<T, R extends Collection<T>>
{
    private T root;

    private Consumer<T> itemAction;

    private boolean stopVisitation;

    public void setStopVisitation(boolean stopVisitation)
    {
        this.stopVisitation = stopVisitation;
    }

    private Function<T, R> childrenGetter;

    private TreeObjectVisitor(Builder<T, R> builder)
    {
        root = builder.root;
        itemAction = builder.itemAction;
        childrenGetter = builder.childrenGetter;
    }

    public void visit()
    {
        visitItem(root);
    }

    private void visitItem(T treeItem)
    {
        if (stopVisitation) return;
        itemAction.accept(treeItem);
        if (stopVisitation) return;
        childrenGetter.apply(treeItem).forEach(this::visitItem);

    }


    public static <T, R extends Collection<T>> Builder<T, R> newBuilder()
    {
        return new Builder<T, R>();
    }


    public static final class Builder<T, R extends Collection<T>>
    {
        private T root;
        private Consumer<T> itemAction;
        private Function<T, R> childrenGetter;

        private Builder()
        {
        }

        public Builder<T,R> root(T root)
        {
            this.root = root;
            return this;
        }

        public Builder<T,R> itemAction(Consumer<T> itemAction)
        {
            this.itemAction = itemAction;
            return this;
        }

        public Builder<T,R> childrenGetter(Function<T, R> childrenGetter)
        {
            this.childrenGetter = childrenGetter;
            return this;
        }

        public TreeObjectVisitor<T, R> build()
        {
            return new TreeObjectVisitor<T, R>(this);
        }
    }
}
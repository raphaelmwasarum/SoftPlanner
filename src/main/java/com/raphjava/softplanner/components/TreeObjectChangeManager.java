package com.raphjava.softplanner.components;

import com.raphjava.softplanner.components.binding.Binder;
import net.raphjava.raphtility.collectionmanipulation.interfaces.NotifyingCollection;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Manages the mutation of a tree object and provides api for acting on changes in the tree object.
 *
 * @param <T> Node in the tree object.
 * @param <R> NotifyingCollection of Nodes in this tree object.
 */
public class TreeObjectChangeManager<T, R extends NotifyingCollection<T>>
{
    private T root;

    private Function<T, NotifyingCollection<T>> childrenGetter;


    private Binder binder;

    private TreeObjectChangeManager(Builder<T, R> builder)
    {
        root = builder.root;
        childrenGetter = builder.childrenGetter;
        binder = builder.binder;
    }

    private void initialize()
    {
        bindNode(root);
    }

    private void bindNode(T node)
    {
        NotifyingCollection<T> c = childrenGetter.apply(node);
        binder.bind(c).onChange(ch -> handleChildrenChanges(node, ch));
        c.forEach(this::bindNode);
    }

    public static <T, R extends NotifyingCollection<T>> Builder<T, R> newBuilder()
    {
        return new Builder<>();
    }


    public void bind(NotifyingCollection<T> source)
    {
//        binder.bind(source).onChange(c -> handleChildrenChanges(root, c));
        NotifyingCollection<T> firstLevelChildren = childrenGetter.apply(root);
        firstLevelChildren.addAll(source);
        binder.bind(source, firstLevelChildren);


    }

    /*private void handleFirstChildrenChanges(NotifyingCollection.CollectionNotifiable.Change<T> c)
    {
        switch (c.getChangeType())
        {
            case Addition:
                addFirstLevelChild(c.getItem());
                callback(root, c, onAddition);
                break;
            case Clearance:
                clear(root);
                callback(root, c, onClearance);
                break;
            case Removal:
                removeNode(c.getItem());
                callback(root, c, onRemoval);
                break;
        }

    }*/

//    private void clear(T parent)
//    {
//        childrenGetter.apply(parent).clear();
//    }

    @SuppressWarnings("unchecked")
    private void removeNode(T item)
    {
        final TreeObjectVisitor<T, R>[] visitor = new TreeObjectVisitor[1];
        visitor[0] = TreeObjectVisitor.<T, R>newBuilder()
                .root(root)
                .itemAction(t ->
                {
                    LambdaSettable<T> child = new LambdaSettable<T>(null);
                    if (isParentOf(t, item, child))
                    {
                        childrenGetter.apply(t).remove(child.getItem());
                        visitor[0].setStopVisitation(true);
                    }

                }).build();
        visitor[0].visit();

    }


    private boolean isParentOf(T parent, T item, LambdaSettable<T> child)
    {
        T childR = childrenGetter.apply(parent).firstOrDefault(it -> Objects.equals(it, item));
        if (childR != null)
        {
            child.setItem(childR);
        }
        return child.getItem() != null;
    }

   /* private void addFirstLevelChild(T firstLevelChild)
    {
        childrenGetter.apply(root).add(firstLevelChild);
        NotifyingCollection<T> grandChildren = childrenGetter.apply(firstLevelChild);
        binder.bind(grandChildren).onChange(change -> handleChildrenChanges(firstLevelChild, change));
        grandChildren.forEach(gc -> add(firstLevelChild, gc));

    }*/


  /*  private void add(T parent, T child)
    {
        childrenGetter.apply(parent).add(child);
        NotifyingCollection<T> grandChildren = this.childrenGetter.apply(child);
        binder.bind(grandChildren).onChange(c -> handleChildrenChanges(child, c));
        if (grandChildren.isEmpty()) return;
        grandChildren.forEach(c -> add(child, c));
    }
*/

    private BiConsumer<T, T> onAddition;

    public TreeObjectChangeManager<T, R> onAddition(BiConsumer<T, T> onAddition)
    {
        this.onAddition = onAddition;
        return this;
    }

    private BiConsumer<T, T> onRemoval;

    public TreeObjectChangeManager<T, R> onRemoval(BiConsumer<T, T> onRemoval)
    {
        this.onRemoval = onRemoval;
        return this;
    }

    private Consumer<T> onClearance;

    public TreeObjectChangeManager<T, R> onClearance(Consumer<T> onClearance)
    {
        this.onClearance = onClearance;
        return this;
    }

    private BiConsumer<T, NotifyingCollection.CollectionNotifiable.Change<T>> onChange;


    public TreeObjectChangeManager<T, R> onChange(BiConsumer<T, NotifyingCollection.CollectionNotifiable.Change<T>> onChange)
    {
        this.onChange = onChange;
        return this;
    }


    private void handleChildrenChanges(T parent, NotifyingCollection.CollectionNotifiable.Change<T> change)
    {
        switch (change.getChangeType())
        {
            case Addition:
                bindNode(change.getItem());
                callback(parent, change, onAddition);
                break;
            case Clearance:
                callback(parent, change, onClearance);
                break;
            case Removal:
                callback(parent, change, onRemoval);
                break;
        }

    }


    private void callback(T parent, NotifyingCollection.CollectionNotifiable.Change<T> change, Consumer<T> cb)
    {
        if(cb != null) cb.accept(parent);
        if(onChange != null) onChange.accept(parent, change);

    }

    private void callback(T parent, NotifyingCollection.CollectionNotifiable.Change<T> change, BiConsumer<T, T> cb)
    {
        if (cb != null) cb.accept(parent, change.getItem());
        if(onChange != null) onChange.accept(parent, change);

    }

    public static final class Builder<T, R extends NotifyingCollection<T>>
    {
        private T root;
        private Function<T, NotifyingCollection<T>> childrenGetter;

        private Binder binder;

        private Builder()
        {
        }

        public Builder<T, R> root(T root)
        {
            this.root = root;
            return this;
        }

        public Builder<T, R> childrenGetter(Function<T, NotifyingCollection<T>> childrenGetter)
        {
            this.childrenGetter = childrenGetter;
            return this;
        }

        public Builder<T, R> binder(Binder binder)
        {
            this.binder = binder;
            return this;
        }
        public TreeObjectChangeManager<T, R> build()
        {
            TreeObjectChangeManager<T, R> cm = new TreeObjectChangeManager<T, R>(this);
            cm.initialize();
            return cm;
        }

    }
}

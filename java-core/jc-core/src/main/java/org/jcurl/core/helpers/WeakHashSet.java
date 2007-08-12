/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.core.helpers;

import java.lang.ref.WeakReference;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.WeakHashMap;

/**
 * Implements a {@link HashSet} where the objects given are stored in
 * {@link WeakReference}s.
 * <p>
 * Uses the {@link WeakHashMap} class as a backing store to implement a set of
 * objects that are stored as weak references. All information concerning using
 * keys in the WeakHashMap class pertain to this class and it is reccomended
 * that the user of this class review that material before using the class.
 * </p>
 * <p>
 * Because this set contains only weak references, it is not serializable. If
 * one tried to serialize a weak reference, the results would be highly
 * unpredictable as the object could likely vanish from memory before the proces
 * was even completed. Users of this class must use transient when the
 * containing class uses this set.
 * </p>
 * <p>
 * Because of the semantics of the weak references, the value null is not
 * allowed in this set.
 * </p>
 * <p>
 * This collection is not identity based but equality based. This can cause some
 * confusion as you cannot put in two objects whose <tt>equals()</tt> methods
 * return true. It also means that an object being held is not necessarily the
 * same one that the user is holding. For example, you could have a String with
 * the value 'fred' at memory location X and ther could be another String with
 * the value 'fred' at memory location Y. The first instance is in the set but
 * the second isn't.
 * </p>
 * 
 * @param <E>
 * 
 * @see java.util.WeakHashMap
 * @see java.lang.ref.WeakReference
 */
public class WeakHashSet<E> extends AbstractSet<E> {

    /** Dummy value used as a value object. */
    private static final Object DUMMY = "DUMMY";

    /** Holds the backing store. */
    private final WeakHashMap<E, Object> backingStore;

    /**
     * Constructs a new empty WeakHashSet with default values passed the the
     * backing store.
     * 
     * @see java.util.WeakHashMap#WeakHashMap()
     */
    public WeakHashSet() {
        backingStore = new WeakHashMap<E, Object>();
    }

    /**
     * Constructs a new WeakHashSet with default values passed the the backing
     * store and fills it with the given collection. Note that duplicates in the
     * collection will merely be overwritten
     * 
     * @param c
     *            collection to use
     * @see WeakHashMap#WeakHashMap(java.util.Map)
     */
    public WeakHashSet(final Collection<? extends E> c) {
        backingStore = new WeakHashMap<E, Object>(Math.max(
                (int) (c.size() / .75f) + 1, 16));
        addAll(c);
    }

    /**
     * Constructs a new WeakHashSet with the values given passed the the backing
     * store.
     * 
     * @param initialCapacity
     *            start size
     * @see java.util.WeakHashMap#WeakHashMap(int)
     */
    public WeakHashSet(final int initialCapacity) {
        backingStore = new WeakHashMap<E, Object>(initialCapacity);
    }

    /**
     * Constructs a new WeakHashSet with the values given passed the the backing
     * store.
     * 
     * @param initialCapacity
     *            start size
     * @param loadFactor
     *            load
     * @see java.util.WeakHashMap#WeakHashMap(int, float)
     */
    public WeakHashSet(final int initialCapacity, final float loadFactor) {
        backingStore = new WeakHashMap<E, Object>(initialCapacity, loadFactor);
    }

    /**
     * @param o
     *            object to add
     * @return <code>true</code>
     * @throws NullPointerException
     *             If the user tries to add null to the set.
     */
    @Override
    public boolean add(final E o) {
        if (o == null)
            throw new NullPointerException();
        return backingStore.put(o, DUMMY) == null;
    }

    @Override
    public boolean addAll(final Collection<? extends E> c) {
        boolean changed = false;
        for (final E element : c)
            changed = changed | backingStore.put(element, DUMMY) != DUMMY;
        return changed;
    }

    @Override
    public void clear() {
        backingStore.clear();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public boolean contains(final Object o) {
        return backingStore.containsKey(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean containsAll(final Collection c) {
        return backingStore.keySet().containsAll(c);
    }

    @Override
    public boolean equals(final Object o) {
        return backingStore.equals(o);
    }

    /**
     * Returns the hash code value for this set.
     * <p>
     * Gives back the hashCode for the backing store key set. The user should be
     * aware, however, that this hash code can change without user intervention
     * as the objects in the collection can easily be collected microseconds
     * after completetion of the method. It is not reccomended that the user
     * rely on this hash code for consistency
     * </p>
     * 
     * @return The hashcode for this object.
     */
    @Override
    public int hashCode() {
        return backingStore.keySet().hashCode();
    }

    @Override
    public boolean isEmpty() {
        return backingStore.keySet().isEmpty();
    }

    /**
     * Returns an iterator over the elements contained in this collection.
     * <p>
     * Note that this iterator is extremely volatile because the user may
     * iterate over an element in the set and find seconds later that it has
     * been removed. This is because of the semantics of {@link WeakReference}s
     * which act like a second thread is silently modifying the collection. For
     * this reason, it is advisable that if the user wants to do something with
     * the set that they maintain a strong reference to the object and not rely
     * on it being in the collection for them.
     * </p>
     * <p>
     * This iterator is fail fast and {@link WeakReference} transparent. By this
     * we mean that the iterator simply ignores objects pending in the reference
     * queue for cleanup.
     * </p>
     * 
     * @return The iterator.
     */
    @Override
    public Iterator<E> iterator() {
        return backingStore.keySet().iterator();
    }

    @Override
    public boolean remove(final Object o) {
        return backingStore.keySet().remove(o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean removeAll(final Collection c) {
        return backingStore.keySet().removeAll(c);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean retainAll(final Collection c) {
        return backingStore.keySet().retainAll(c);
    }

    @Override
    public int size() {
        return backingStore.keySet().size();
    }

    @Override
    public Object[] toArray() {
        return backingStore.keySet().toArray();
    }

    @Override
    public <T> T[] toArray(final T[] a) {
        return backingStore.keySet().toArray(a);
    }

    @Override
    public String toString() {
        return backingStore.keySet().toString();
    }
}
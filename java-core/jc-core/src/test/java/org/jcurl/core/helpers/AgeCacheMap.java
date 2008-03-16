/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Thread-less age based cache.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 * @param <K>
 * @param <V>
 */
public class AgeCacheMap<K, V> implements Map<K, V> {
    protected static class AgeExpirer<K, V> {
        private final long dt;

        public AgeExpirer(final long dt) {
            this.dt = dt;
        }

        public boolean hasExpired(final AgeItem<K> value, final long time) {
            return time - value.time >= dt;
        }
    }

    static class AgeItem<K> {
        public final long time;
        private final WeakReference<K> wkey;

        public AgeItem(final K key, final long time) {
            this.time = time;
            this.wkey = new WeakReference<K>(key);
        }

        public K getKey() {
            return wkey == null ? null : wkey.get();
        }
    }

    private final List<AgeItem<K>> ageList;
    private final Comparator<AgeItem<K>> descComp;
    private final AgeExpirer<K, V> exp;
    private final Map<K, V> store;

    public AgeCacheMap(final long dt) {
        this(new WeakHashMap<K, V>(), dt);
    }

    public AgeCacheMap(final Map<K, V> store, final long dt) {
        this(store, dt, System.currentTimeMillis());
    }

    AgeCacheMap(final Map<K, V> store, final long dt, final long time) {
        this.store = store;
        this.descComp = new Comparator<AgeItem<K>>() {
            public int compare(final AgeItem<K> o1, final AgeItem<K> o2) {
                if (o1.time > o2.time)
                    return 1;
                if (o1.time < o2.time)
                    return -1;
                return 0;
            }
        };
        this.exp = new AgeExpirer<K, V>(dt);
        this.ageList = new ArrayList<AgeItem<K>>();
        for (final Entry<K, V> elem : store.entrySet())
            ageList.add(new AgeItem<K>(elem.getKey(), time));
        Collections.sort(ageList, descComp);
    }

    public void clear() {
        store.clear();
        ageList.clear();
    }

    Comparator<AgeItem<K>> comparator() {
        return descComp;
    }

    public boolean containsKey(final Object key) {
        doExpire(System.currentTimeMillis());
        return store.containsKey(key);
    }

    public boolean containsValue(final Object value) {
        doExpire(System.currentTimeMillis());
        return store.containsValue(value);
    }

    int doExpire(final long time) {
        // when to clean out zombies?
        int i = 0;
        for (final AgeItem<K> elem : ageList) {
            if (elem.getKey() == null) {
                ageList.remove(i);
                continue;
            }
            if (!exp.hasExpired(elem, time))
                break;
            store.remove(elem.getKey());
            i++;
        }
        return i;
    }

    public Set<Entry<K, V>> entrySet() {
        doExpire(System.currentTimeMillis());
        return store.entrySet();
    }

    @Override
    public boolean equals(final Object o) {
        doExpire(System.currentTimeMillis());
        return store.equals(o);
    }

    public V get(final Object key) {
        return get(key, System.currentTimeMillis());
    }

    V get(final Object key, final long time) {
        doExpire(time);
        return store.get(key);
    }

    @Override
    public int hashCode() {
        doExpire(System.currentTimeMillis());
        return store.hashCode();
    }

    public boolean isEmpty() {
        doExpire(System.currentTimeMillis());
        return store.isEmpty();
    }

    public Set<K> keySet() {
        doExpire(System.currentTimeMillis());
        return store.keySet();
    }

    public V put(final K key, final V value) {
        return put(key, value, System.currentTimeMillis());
    }

    V put(final K key, final V value, final long time) {
        doExpire(time);
        final V ret = store.put(key, value);
        // TODO if ret != null remove from list.
        ageList.add(new AgeItem<K>(key, time));
        Collections.sort(ageList, descComp);
        return ret;
    }

    public void putAll(final Map<? extends K, ? extends V> t) {
        final long time = System.currentTimeMillis();
        doExpire(time);
        store.putAll(t);
        for (final Entry<? extends K, ? extends V> elem : t.entrySet())
            ageList.add(new AgeItem<K>(elem.getKey(), time));
        Collections.sort(ageList, descComp);
    }

    public V remove(final Object key) {
        doExpire(System.currentTimeMillis());
        return store.remove(key);
    }

    public int size() {
        return size(System.currentTimeMillis());
    }

    int size(final long time) {
        doExpire(time);
        return store.size();
    }

    public Collection<V> values() {
        doExpire(System.currentTimeMillis());
        return store.values();
    }
}

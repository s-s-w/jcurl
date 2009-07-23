/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
package org.jcurl.mr.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Type safe list for {@link java.awt.Point}s.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PointList.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class PointList implements List {
	public static PointList getLine(final PointList c) {
		final PointList ret = new PointList(new ArrayList(2));
		ret.add(c.dat.get(0));
		ret.add(c.dat.get(c.dat.size() - 1));
		return ret;
	}

	private final List dat;

	public PointList() {
		this(new LinkedList());
	}

	public PointList(final List dat) {
		this.dat = dat;
	}

	public void add(final int index, final Object element) {
		dat.add(index, element);
	}

	/**
	 * Add point reference to the curve.
	 * 
	 * @param o
	 *            the Point to add
	 * @return <code>true</code>
	 */
	public boolean add(final Object o) {
		return dat.add(o);
	}

	public boolean addAll(final Collection c) {
		return dat.addAll(c);
	}

	public boolean addAll(final int index, final Collection c) {
		return dat.addAll(index, c);
	}

	public void clear() {
		dat.clear();
	}

	public boolean contains(final Object o) {
		return dat.contains(o);
	}

	public boolean containsAll(final Collection c) {
		return dat.containsAll(c);
	}

	@Override
	public boolean equals(final Object obj) {
		return dat.equals(obj);
	}

	public Object get(final int index) {
		return dat.get(index);
	}

	@Override
	public int hashCode() {
		return dat.hashCode();
	}

	public int indexOf(final Object o) {
		return dat.indexOf(o);
	}

	public boolean isEmpty() {
		return dat.isEmpty();
	}

	public Iterator iterator() {
		return dat.iterator();
	}

	public int lastIndexOf(final Object o) {
		return dat.lastIndexOf(o);
	}

	public ListIterator listIterator() {
		return dat.listIterator();
	}

	public ListIterator listIterator(final int index) {
		return dat.listIterator(index);
	}

	public Iterator points() {
		return dat.iterator();
	}

	public Object remove(final int index) {
		return dat.remove(index);
	}

	public boolean remove(final Object o) {
		return dat.remove(o);
	}

	public boolean removeAll(final Collection c) {
		return dat.removeAll(c);
	}

	public boolean retainAll(final Collection c) {
		return dat.retainAll(c);
	}

	public Object set(final int index, final Object element) {
		return dat.set(index, element);
	}

	public int size() {
		return dat.size();
	}

	public List subList(final int fromIndex, final int toIndex) {
		return dat.subList(fromIndex, toIndex);
	}

	public Object[] toArray() {
		return dat.toArray();
	}

	public Object[] toArray(final Object[] a) {
		return dat.toArray(a);
	}

	@Override
	public String toString() {
		return dat.toString();
	}
}
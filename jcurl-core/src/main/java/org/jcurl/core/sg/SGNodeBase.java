/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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
package org.jcurl.core.sg;

import java.awt.geom.AffineTransform;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public abstract class SGNodeBase implements SGNode {
    private final List<SGNode> children = new ArrayList<SGNode>();

    private WeakReference<SGNode> parent = null;

    protected WeakReference<SGNode> root = null;

    private AffineTransform trafo;

    public void add(final int index, final SGNode element) {
        element.setParent(this);
        children.add(index, element);
    }

    public boolean add(final SGNode o) {
        o.setParent(this);
        return children.add(o);
    }

    public boolean addAll(final Collection<? extends SGNode> c) {
        for (final SGNode element : c)
            element.setParent(this);
        return children.addAll(c);
    }

    public boolean addAll(final int index, final Collection<? extends SGNode> c) {
        for (final SGNode element : c)
            element.setParent(this);
        return children.addAll(index, c);
    }

    public void clear() {
        for (final SGNode element : this)
            element.setParent(null);
        children.clear();
    }

    public boolean contains(final Object o) {
        return children.contains(o);
    }

    public boolean containsAll(final Collection<?> c) {
        return children.containsAll(c);
    }

    public void fireNodeChange() {
        final SGRoot r = getRoot();
        if (r == null)
            return;
        r.fireNodeChange(this);
    }

    public SGNode get(final int index) {
        return children.get(index);
    }

    public SGNode getParent() {
        return parent == null ? null : parent.get();
    }

    public SGRoot getRoot() {
        if (root == null) {
            // parentship changed:
            SGNode curr = this;
            while (curr.getParent() != null)
                curr = curr.getParent();
            root = new WeakReference<SGNode>(curr);
        }
        return root.get() instanceof SGRoot ? (SGRoot) root.get() : null;
    }

    public AffineTransform getTrafo() {
        return trafo;
    }

    public int indexOf(final Object o) {
        return children.indexOf(o);
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    public Iterator<SGNode> iterator() {
        return children.iterator();
    }

    public int lastIndexOf(final Object o) {
        return children.lastIndexOf(o);
    }

    public ListIterator<SGNode> listIterator() {
        return children.listIterator();
    }

    public ListIterator<SGNode> listIterator(final int index) {
        return children.listIterator(index);
    }

    public SGNode remove(final int index) {
        get(index).setParent(null);
        return children.remove(index);
    }

    public boolean remove(final Object o) {
        ((SGNode) o).setParent(null);
        return children.remove(o);
    }

    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public SGNode set(final int index, final SGNode element) {
        get(index).setParent(null);
        element.setParent(this);
        return children.set(index, element);
    }

    public void setParent(final SGNode parent) {
        if(parent == getParent())
            return;
        // reset root in any case
        root = null;
        if (parent == null) {
            this.parent = null;
            return;
        }
        this.parent = new WeakReference<SGNode>(parent);
        fireNodeChange();
    }

    public void setTrafo(final AffineTransform trafo) {
        if(trafo == getTrafo())
            return;
        this.trafo = trafo;
        fireNodeChange();
    }

    public int size() {
        return children.size();
    }

    public List<SGNode> subList(final int fromIndex, final int toIndex) {
        return children.subList(fromIndex, toIndex);
    }

    public Object[] toArray() {
        return children.toArray();
    }

    public <T> T[] toArray(final T[] a) {
        return children.toArray(a);
    }
}

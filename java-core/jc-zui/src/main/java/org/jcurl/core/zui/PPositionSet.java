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
package org.jcurl.core.zui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockSet;

import edu.umd.cs.piccolo.PNode;

/**
 * Piccolo {@link PNode} backed with a {@link PositionSet}.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PPositionSet extends PNode implements PropertyChangeListener {

    public static final Object index16 = "index16";

    private static final long serialVersionUID = 6564103045992326633L;

    static boolean sync(final Rock src, final PNode dst) {
        // TUNE check if it's changed at all?
        dst.setRotation(src.getA());
        dst.setOffset(src.getX(), src.getY());
        dst.invalidatePaint();
        return true;
    }

    private final PositionSet p;

    /**
     * Create a pickable child node for each rock and set it's attributes
     * {@link #index16} and {@link PositionSet#getClass()} - yes the class
     * object is the key.
     * 
     * @param p
     * @param f
     */
    public PPositionSet(final PositionSet p, final PRockFactory f) {
        this.p = p;
        for (int i = 0; i < RockSet.ROCKS_PER_SET; i++) {
            final PNode c = f.newInstance(i);
            c.addAttribute(index16, i);
            c.addAttribute(p.getClass(), p);
            addChild(i, c);
        }
        p.addPropertyChangeListener(this);
        sync(this.p, this);
    }

    public void propertyChange(final PropertyChangeEvent evt) {
        sync(p, this);
        repaint();
    }

    private void sync(final PositionSet src, final PPositionSet dst) {
        // TODO check if it's changed at all!
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            sync(src.getRock(i), dst.getChild(i));
    }
}

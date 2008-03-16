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
package org.jcurl.core.zui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.log.JCLoggerFactory;

import edu.umd.cs.piccolo.PNode;

/**
 * Piccolo {@link PNode} backed with a {@link PositionSet}.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PPositionSet extends PNode implements PropertyChangeListener {

    private static final double eps = 1e-11;
    public static final Object index16 = "index16";
    private static final Log log = JCLoggerFactory
            .getLogger(PPositionSet.class);
    private static final long serialVersionUID = 6564103045992326633L;

    static boolean sync(final Rock src, final PNode dst) {
        // check if it's changed either location or angle:
        if (src.distanceSq(dst.getOffset()) < eps
                && Math.abs(src.getA() - dst.getRotation()) < eps)
            return false;
        dst.setRotation(src.getA());
        dst.setOffset(src.getX(), src.getY());
        dst.invalidatePaint();
        // Why is this necessary?
        dst.repaint();
        return true;
    }

    private final PRockFactory f;
    private PositionSet model = null;

    /**
     * Create a pickable child node for each rock and set it's attributes
     * {@link #index16} and {@link PositionSet#getClass()} - yes the class
     * object is the key.
     * 
     * @param f
     */
    public PPositionSet(final PRockFactory f) {
        this.f = f;
    }

    public PositionSet getModel() {
        return model;
    }

    public void propertyChange(final PropertyChangeEvent evt) {
        log.debug(evt);
        sync(model, this);
    }

    public void setModel(final PositionSet positionSet) {
        if (model != null)
            positionSet.removePropertyChangeListener(this);
        model = positionSet;
        setVisible(model != null);
        if (model != null) {
            removeAllChildren();
            for (int i = 0; i < RockSet.ROCKS_PER_SET; i++) {
                final PNode c = f.newInstance(i);
                c.addAttribute(index16, i);
                c.addAttribute(positionSet.getClass(), positionSet);
                addChild(i, c);
            }
            sync(model, this);
            positionSet.addPropertyChangeListener(this);
        }
    }

    private void sync(final PositionSet src, final PPositionSet dst) {
        if (src == null)
            return;
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            sync(src.getRock(i), dst.getChild(i));
    }
}

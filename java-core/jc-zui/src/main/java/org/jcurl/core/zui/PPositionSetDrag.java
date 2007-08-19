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

import java.awt.event.InputEvent;
import java.awt.geom.Point2D;

import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;

/**
 * Add drag-support to a {@link PPositionSet}. Requires the attributes
 * {@link PPositionSet#index16} and {@link PPositionSet#getClass()} to be set
 * correctly.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PPositionSetDrag extends PBasicInputEventHandler {

    public PPositionSetDrag() {
        setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK));
    }

    @Override
    public void mouseDragged(final PInputEvent event) {
        final PNode node = event.getPickedNode();
        final int i16 = ((Integer) node.getAttribute(PPositionSet.index16))
                .intValue();
        final PositionSet pp = (PositionSet) node
                .getAttribute(PositionSet.class);
        final Rock r = pp.getRock(i16);
        final Point2D p = node.getParent().globalToLocal(event.getPosition());
        // FIXME Add overlap/collission detection!
        event.setHandled(true);
        // any move at all?
        if (p.distanceSq(r) < 1e-11)
            return;
        r.setLocation(p);
        PPositionSet.sync(r, node);
    }

    @Override
    public void mouseReleased(final PInputEvent event) {
        ((PositionSet) event.getPickedNode().getAttribute(PositionSet.class))
                .notifyChange();
    }
}
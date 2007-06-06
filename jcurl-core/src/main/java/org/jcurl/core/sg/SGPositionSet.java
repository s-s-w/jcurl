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

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.Rock;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.base.Zoomer;
import org.jcurl.core.swing.RockPainter;
import org.jcurl.core.swing.WCComponent;

public class SGPositionSet extends SGNodeBase implements PropertyChangeListener {

    private final PositionSet data;

    private final RockPainter p;

    public SGPositionSet(final PositionSet data, final RockPainter p) {
        this.data = data;
        this.p = p;
        this.data.addPropertyChangeListener(this);
    }

    public double distance(final Point2D p) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void propertyChange(final PropertyChangeEvent evt) {
        fireNodeChange();
    }

    public void render(final Graphics2D g) {
        for (int i = RockSet.ROCKS_PER_SET - 1; i >= 0; i--)
            if ((RockSet.ALL_MASK >> i & 1) == 1) {
                final Rock rock = data.getRock(i);
                final AffineTransform t = g.getTransform();
                try {
                    if (false) {
                        g.translate(Zoomer.SCALE * rock.getX(), Zoomer.SCALE
                                * rock.getY());
                        g.rotate(Math.PI + rock.getA());
                    } else {
                        g.transform(WCComponent.preScale);
                        g.transform(rock.getTrafo());
                        g.transform(WCComponent.postScale);
                    }
                    // make the right-handed coordinate system left handed again
                    // (for un-flipped text display)
                    g.scale(-1, 1);
                    p.paintRockRC(g, i % 2 == 0, i / 2);
                } finally {
                    g.setTransform(t);
                }
            }
    }
}

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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import org.jcurl.core.model.FixpointZoomer;

/**
 * Visual and behavioral container for scene graphs.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class SGComponent extends Component {

    private static final Map<Object, Object> hints = new HashMap<Object, Object>();

    private static final long serialVersionUID = -7128617540182305476L;
    static {
        // hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
        // RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        hints.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private SGNode root = null;

    public SGNode getRoot() {
        return root;
    }

    @Override
    public void paint(final Graphics g) {
        final Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(hints);
        super.paint(g2);
        if (getRoot() == null)
            return;
        if (getRoot().getTrafo() == null)
            getRoot().setTrafo(new AffineTransform());
        // TUNE Do less instanciations!
        final Rectangle b = this.getBounds();
        final Rectangle2D r = new Rectangle2D.Float(b.x, b.y, b.width, b.height);
        FixpointZoomer.HOUSE2HACK.computeWctoDcTrafo(b, getRoot().getTrafo());
        getRoot().doPaint(g2, r);
    }

    public void setRoot(final SGNode root) {
        this.root = root;
    }
}

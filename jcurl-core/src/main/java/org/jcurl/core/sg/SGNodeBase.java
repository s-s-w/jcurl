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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

abstract class SGNodeBase implements SGNode {

    private final Collection<SGNodeBase> children = new ArrayList<SGNodeBase>();

    private AffineTransform trafo;

    public Collection<SGNodeBase> children() {
        return children;
    }

    public void doPaint(final Graphics2D g, final Rectangle2D clip) {
        // TUNE could be quicker: save instanciations
        final AffineTransform t = g.getTransform();
        try {
            if (getTrafo() != null)
                g.setTransform(getTrafo());
            if (isDirty())
                render(g, clip);
            for (final SGNode element : children())
                element.doPaint(g, clip);
        } finally {
            g.setTransform(t);
        }
    }

    public AffineTransform getTrafo() {
        return trafo;
    }

    protected boolean isDirty() {
        return true;
    }

    protected abstract void render(Graphics2D g, Rectangle2D clip);

    public void setTrafo(final AffineTransform trafo) {
        this.trafo = trafo;
    }
}

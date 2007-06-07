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
import java.util.List;

import org.jcurl.core.sg.SGRoot.NodeChangeEvent;

/**
 * 2D Scenegraph node.
 * <p>
 * IS a {@link List} rather than just containing one: Keep control to manage
 * parentship for {@link NodeChangeEvent} delivery.
 * </p>
 * <p>
 * Quick hashing is crucible for quick rendering!
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public interface SGNode extends List<SGNode> {

    public abstract double distance(Point2D p);

    public abstract void fireNodeChange();

    public abstract SGNode getParent();

    public abstract SGRoot getRoot();

    public abstract AffineTransform getTrafo();

    /**
     * Non-recursive rendering.
     * 
     * @param g
     */
    public abstract void render(Graphics2D g);

    public abstract void setParent(SGNode parent);

    public abstract void setTrafo(AffineTransform trafo);

}
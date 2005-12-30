/*
 * jcurl curling simulation framework Copyright (C) 2005 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package jcurl.math;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Abstract base for wrapper classes to enabling convenient approximated Java2D
 * drawing of arbitratry curves. Internally uses a
 * {@link java.awt.geom.GeneralPath}to handle some maths.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class CurveShape implements Shape {

    private GeneralPath _gp = null;

    private final CurveBase curve;

    public CurveShape(CurveBase curve) {
        this.curve = curve;
    }

    protected abstract GeneralPath computeGP(final CurveBase c);

    public boolean contains(double x, double y) {
        return getGp().contains(x, y);
    }

    public boolean contains(double x, double y, double w, double h) {
        return getGp().contains(x, y, w, h);
    }

    public boolean contains(Point2D p) {
        return getGp().contains(p);
    }

    public boolean contains(Rectangle2D r) {
        return getGp().contains(r);
    }

    public Rectangle getBounds() {
        return getGp().getBounds();
    }

    public Rectangle2D getBounds2D() {
        return getGp().getBounds2D();
    }

    private GeneralPath getGp() {
        if (_gp == null)
            _gp = computeGP(curve);
        return _gp;
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return getGp().getPathIterator(at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return getGp().getPathIterator(at, flatness);
    }

    public boolean intersects(double x, double y, double w, double h) {
        return getGp().intersects(x, y, w, h);
    }

    public boolean intersects(Rectangle2D r) {
        return getGp().intersects(r);
    }
}
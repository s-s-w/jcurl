/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
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
package jcurl.math;

import java.awt.geom.Point2D;

/**
 * Helper class that brings some (2D-)vector artihmetics.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public final class MathVec {

    public static double abs(final Point2D a) {
        return Math.sqrt(scal(a, a));
    }

    public static Point2D add(final Point2D a, final Point2D b, final Point2D c) {
        final Point2D ret = ensureInstance(a, c);
        ret.setLocation(a.getX() + b.getX(), a.getY() + b.getY());
        return ret;
    }

    /**
     * Ensure c isn't <code>null</code>, if so create a new instance of the
     * type of <code>template</code>.
     * 
     * @param template
     * @param c
     * @return <code>c</code> or
     *         <code>template.getClass().newInstance()</code>
     */
    public static Point2D ensureInstance(final Point2D template, final Point2D c) {
        if (c != null)
            return c;
        try {
            return (Point2D) template.getClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Couldn't create a new instance.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't create a new instance.", e);
        }
    }

    public static double[] mult(final double fact, final double[] a, double[] b) {
        if (b == null)
            b = new double[a.length];
        for (int i = a.length - 1; i >= 0; i--)
            b[i] = a[i] * fact;
        return b;
    }

    public static Point2D mult(final double fact, final Point2D a, Point2D b) {
        b = ensureInstance(a, b);
        b.setLocation(a.getX() * fact, a.getY() * fact);
        return b;
    }

    public static double scal(final Point2D a, final Point2D b) {
        return a.getX() * b.getX() + a.getY() * b.getY();
    }

    public static Point2D sub(final Point2D a, final Point2D b, final Point2D c) {
        final Point2D ret = ensureInstance(a, c);
        ret.setLocation(a.getX() - b.getX(), a.getY() - b.getY());
        return ret;
    }
}
/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005-2006 M. Rohrmoser
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
package org.jcurl.core.curved;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.jcurl.core.base.Rock;
import org.jcurl.core.math.MathException;

/**
 * One curve with many segments.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurvePiecewise extends Object implements CurveRock {

    private final Collection curves = new LinkedList();

    public void add(CurveTransformed c) {
        // schlauer machen und von hinten her abbauen falls nötig?
        curves.add(c);
    }

    public void clear() {
        curves.clear();
    }

    CurveTransformed[] curves() {
        final CurveTransformed[] ret = new CurveTransformed[curves.size()];
        curves.toArray(ret);
        return ret;
    }

    public int dimension() {
        return 3;
    }

    /**
     * Stupid search: always scan from the left.
     * 
     * @param t
     * @return the curve with biggest t0 <= t
     */
    CurveTransformed find(double t) {
        CurveTransformed curr = null;
        CurveTransformed last = null;
        for (Iterator it = iterator(); it.hasNext();) {
            last = curr;
            curr = (CurveTransformed) it.next();
            if (curr.getT0() > t)
                return last;
        }
        return curr;
    }

    Iterator iterator() {
        return curves.iterator();
    }

    public double[] value(double t, double[] ret) throws MathException {
        return value(t, 0, ret);
    }

    public double[] value(double t, int derivative, double[] ret)
            throws MathException {
        throw new UnsupportedOperationException();
    }

    /**
     * @see #find(double)
     * @see CurveTransformed#value(double, int, Rock)
     */
    public Rock value(double t, int derivative, Rock ret) throws MathException {
        final CurveTransformed c = find(t);
        if (c == null)
            return null;
        return c.value(t, derivative, ret);
    }

    public Rock value(double t, Rock ret) throws MathException {
        return value(t, 0, ret);
    }

}

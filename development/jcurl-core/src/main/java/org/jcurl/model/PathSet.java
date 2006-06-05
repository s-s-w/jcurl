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
package org.jcurl.model;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.math.FunctionEvaluationException;
import org.jcurl.core.dto.Rock;

/**
 * Several {@link org.jcurl.model.PathSegment}s with discontinuities.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PathSet implements JCurlCurve {

    private final SortedMap pieces = new TreeMap();

    public void append(double t0, PathSegment curve) {
        if (pieces.size() > 0) {
            final Double last = (Double) pieces.lastKey();
            if (t0 <= last.doubleValue())
                throw new IllegalArgumentException("t0 <= tmax");
        }
        pieces.put(new Double(t0), curve);
    }

    public void clear() {
        pieces.clear();
    }

    public Iterator discontinuities() {
        return pieces.keySet().iterator();
    }

    public PathSegment getCurve(double t0) {
        Map.Entry prev = null;
        for (Iterator it = pieces.entrySet().iterator(); it.hasNext();) {
            final Map.Entry e = (Map.Entry) it.next();
            final Double t = (Double) e.getKey();
            if (t.doubleValue() > t0) {
                if (prev == null)
                    prev = e;
                break;
            }
            prev = e;
        }
        if (prev == null)
            return null;
        return (PathSegment) prev.getValue();
    }

    /**
     * @see PathSegment#value(double, Rock)
     */
    public Rock value(double t, Rock dst) throws FunctionEvaluationException {
        return getCurve(t).value(t, dst);
    }

    /**
     * @see PathSegment#valueRC(double, Rock)
     */
    public Rock valueRC(double t, Rock dst) throws FunctionEvaluationException {
        return getCurve(t).valueRC(t, dst);
    }

    /**
     * @see PathSegment#valueWC(double, Rock)
     */
    public Rock valueWC(double t, Rock dst) throws FunctionEvaluationException {
        return getCurve(t).valueWC(t, dst);
    }
}
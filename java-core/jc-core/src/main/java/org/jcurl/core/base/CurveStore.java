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
package org.jcurl.core.base;

import java.util.Iterator;
import java.util.Map.Entry;

import org.jcurl.core.helpers.IPropertyChangeSupport;
import org.jcurl.math.R1RNFunction;

/**
 * Manage rock trajectory segments for a complete set of rocks over time.
 * <p>
 * Supports smart stop detection.
 * </p>
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CurveStore.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
public interface CurveStore extends
        Iterable<Iterable<Entry<Double, R1RNFunction>>>, IPropertyChangeSupport {

    public abstract void add(final int i16, final double t,
            final R1RNFunction f, final double tstop);

    public abstract R1RNFunction getCurve(final int i16);

    /**
     * Ascending iterator over the curves returning each segment.
     * 
     * @return iterator over the curves returning each segment.
     */
    public abstract Iterator<Iterable<Entry<Double, R1RNFunction>>> iterator();

    public Iterator<Entry<Double, R1RNFunction>> iterator(int i16);

    public abstract void reset(final int i16);

}
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
package org.jcurl.core.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * Store unique tupels of two rocks (indices). Has a time stamp for sorting
 * purpose.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CollissionStore.java 682 2007-08-12 21:25:04Z mrohrmoser $
 */
class CollissionStore {

    public static class Tupel {
        public final byte a;

        public final byte b;

        public double t;

        public Tupel(final double t, int a, int b) {
            if (a == b)
                throw new IllegalArgumentException();
            this.t = t;
            if (a < b) {
                final int tmp = a;
                a = b;
                b = tmp;
            }
            this.a = (byte) (a & 0xFF);
            this.b = (byte) (b & 0xFF);
        }

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Tupel other = (Tupel) obj;
            if (a != other.a)
                return false;
            if (b != other.b)
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            // http://www.angelikalanger.com/Articles/JavaSpektrum/03.HashCode/03.HashCode.html
            // hashcode N = hashcode N-1 * multiplikator + feldwert N
            int hash = 17;
            final int fact = 59;
            // hash *= fact;
            // final long tmp = t == 0.0 ? 0L : java.lang.Double
            // .doubleToLongBits(t);
            // hash += (int) (tmp ^ tmp >>> 32);
            hash *= fact;
            hash += a;
            hash *= fact;
            hash += b;
            return hash;
        }

        @Override
        public String toString() {
            return new StringBuffer().append(a).append("->").append(b).append(
                    " ").append(t).toString();
        }
    }

    public static class TupelComp implements Comparator<Tupel> {

        public int compare(final Tupel arg0, final Tupel arg1) {
            final Tupel a = arg0;
            final Tupel b = arg1;
            if (a.t < b.t || !Double.isNaN(a.t) && Double.isNaN(b.t))
                return -1;
            if (a.t > b.t || Double.isNaN(a.t) && !Double.isNaN(b.t))
                return 1;
            if (a.a < b.a)
                return -1;
            if (a.a > b.a)
                return 1;
            if (a.b < b.b)
                return -1;
            if (a.b > b.b)
                return 1;
            return 0;
        }
    }

    private static final Comparator<Tupel> comp = new TupelComp();

    private static final Log log = JCLoggerFactory
            .getLogger(CollissionStore.class);

    final LinkedList<Tupel> m = new LinkedList<Tupel>();

    /**
     * @param t
     *            may be {@link Double#NaN} (will be sorted last).
     * @param a16
     *            0-15
     * @param b16
     *            0-15
     */
    public void add(final double t, final int a16, final int b16) {
        final Tupel o = new Tupel(t, a16, b16);
        if (log.isDebugEnabled())
            log.debug("collission " + o);
        if (!m.add(o))
            throw new IllegalArgumentException();
        Collections.sort(m, comp);
    }

    public void clear() {
        m.clear();
    }

    public Tupel first() {
        return m.getFirst();
    }

    /**
     * @param t
     *            may be {@link Double#NaN} (will be sorted last).
     * @param a
     *            0-15
     * @param b
     *            0-15
     */
    public void replace(final double t, int a, int b) {
        if (a < b) {
            final int tmp = a;
            a = b;
            b = tmp;
        }
        for (final Object element : m) {
            final Tupel f = (Tupel) element;
            if (f.a == a && f.b == b) {
                f.t = t;
                if (log.isDebugEnabled())
                    log.debug("collission " + f);
                Collections.sort(m, comp);
                return;
            }
        }
        throw new IllegalStateException("Not found.");
    }
}

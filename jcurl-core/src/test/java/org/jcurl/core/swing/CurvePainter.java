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
package org.jcurl.core.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.CurveStore;
import org.jcurl.core.base.Zoomer;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.CurveShape;
import org.jcurl.math.R1RNFunction;

/**
 * Paint a {@link CurveStore}.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurvePainter {
    private static final Log log = JCLoggerFactory
            .getLogger(CurvePainter.class);

    static Writer toString(Writer w, double[] arr) {
        try {
            if (arr == null) {
                w.write("null");
            } else {
                boolean start = true;
                w.write("[");
                for (int i = 0; i < arr.length; i++) {
                    if (!start)
                        w.write(" ");
                    w.write(Double.toString(arr[i]));
                    start = false;
                }
                w.write("]");
            }
            return w;
        } catch (IOException e) {
            throw new IllegalStateException("Couldn't write to writer.", e);
        }
    }

    private final Color dark;

    private final Color light;

    private final double[] sections = { 0, 0, 0, 0, 0, 0, 0 };

    private final Stroke stroke;

    private final double[] t1 = { 0, 0, 0 };

    private final double[] t2 = { 0, 0, 0 };

    private final double[] t3 = { 0, 0, 0 };

    private final double[] t4 = { 0, 0, 0 };

    public CurvePainter(final Color dark, final Color light, final Stroke stroke) {
        this.dark = dark;
        this.light = light;
        this.stroke = stroke;
    }

    /**
     * Paint all curves of the store, delegate to
     * {@link #doPaint(Graphics2D, Iterator, double[], float, double[], double[], double[], double[])}.
     * 
     * @param g2
     *            where to draw
     * @param cs
     *            store
     */
    public void doPaint(final Graphics2D g2,
            final Iterable<Iterable<Entry<Double, R1RNFunction>>> cs) {
        g2.setStroke(stroke);
        int i = 0;
        for (final Iterable<Entry<Double, R1RNFunction>> name : cs) {
            if (log.isDebugEnabled())
                log.debug("i=" + i + " " + (i % 2 == 0 ? "dark" : "light")
                        + " " + (i / 2));
            g2.setPaint(i++ % 2 == 0 ? dark : light);
            doPaint(g2, name.iterator(), sections, Zoomer.SCALE, t1, t2, t3, t4);
        }
    }

    /**
     * Paint the segments of one curve.
     * 
     * @param g2
     *            where to draw
     * @param path
     * @param sections
     * @param zoom
     *            factor - typically {@link Zoomer#SCALE}.
     * @param t1
     *            save instanciations calling
     *            {@link R1RNFunction#at(int, double, double[])}.
     * @param t2
     *            save instanciations calling
     *            {@link R1RNFunction#at(int, double, double[])}.
     * @param t3
     *            save instanciations calling
     *            {@link R1RNFunction#at(int, double, double[])}.
     * @param t4
     *            save instanciations calling
     *            {@link R1RNFunction#at(int, double, double[])}.
     * @see #doPaint(Graphics2D, R1RNFunction, double[], float, double[],
     *      double[], double[], double[])
     */
    public void doPaint(final Graphics2D g2,
            final Iterator<Entry<Double, R1RNFunction>> path,
            final double[] sections, final float zoom, final double[] t1,
            final double[] t2, final double[] t3, final double[] t4) {
        if (!path.hasNext())
            return;
        Entry<Double, R1RNFunction> curr = path.next();
        while (path.hasNext()) {
            final Entry<Double, R1RNFunction> next = path.next();
            doSections(sections, curr.getKey(), next.getKey());
            if (log.isDebugEnabled()) {
                final StringWriter wri = new StringWriter();
                wri.write("t=");
                toString(wri, sections);
                wri.write(" c=" + curr.getValue());
                log.debug(wri.getBuffer());
            }
            doPaint(g2, curr.getValue(), sections, zoom, t1, t2, t3, t4);
            // step:
            curr = next;
        }
    }

    /**
     * Paint one segment of a curve.
     * 
     * @param g2
     * @param curr
     * @param sections
     * @param zoom
     *            factor - typically {@link Zoomer#SCALE}.
     * @param t1
     *            save instanciations calling
     *            {@link R1RNFunction#at(int, double, double[])}.
     * @param t2
     *            save instanciations calling
     *            {@link R1RNFunction#at(int, double, double[])}.
     * @param t3
     *            save instanciations calling
     *            {@link R1RNFunction#at(int, double, double[])}.
     * @param t4
     *            save instanciations calling
     *            {@link R1RNFunction#at(int, double, double[])}.
     */
    public void doPaint(final Graphics2D g2, R1RNFunction curr,
            final double[] sections, final float zoom, final double[] t1,
            final double[] t2, final double[] t3, final double[] t4) {
        if (true)
            g2.draw(CurveShape.approximateLinear(curr, sections, zoom, t1));
        else
            g2.draw(CurveShape.approximateQuadratic(curr, sections, zoom, t1,
                    t2, t3, t4));
    }

    public void doSections(final double[] sections, double min, double max) {
        CurveShape.exponentialSections(min, max, sections);
    }
}
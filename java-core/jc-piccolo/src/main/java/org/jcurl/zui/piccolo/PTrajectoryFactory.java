/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
package org.jcurl.zui.piccolo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.Factory;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.math.CurveShape;
import org.jcurl.math.R1RNFunction;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

/**
 * Create an unpickable {@link PNode} for a combined curve describing the path
 * of one rock.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PTrajectoryFactory.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public abstract class PTrajectoryFactory implements Factory {
	public static class Fancy extends PTrajectoryFactory {
		private static final Paint dark = new Color(255, 153, 153, 150);

		private static final Paint light = new Color(255, 255, 153, 150);

		private static final Stroke stroke = new BasicStroke(
				2 * RockProps.DEFAULT.getRadius(), BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0);

		private static final int zoom = 1;

		private static Writer toString(final Writer w, final double[] arr) {
			try {
				if (arr == null)
					w.write("null");
				else {
					boolean start = true;
					w.write("[");
					for (final double element : arr) {
						if (!start)
							w.write(" ");
						w.write(Double.toString(element));
						start = false;
					}
					w.write("]");
				}
				return w;
			} catch (final IOException e) {
				throw new IllegalStateException("Couldn't write to writer.", e);
			}
		}

		private final double[] sections = { 0, 0, 0, 0, 0, 0, 0 };

		private final double[] t1 = { 0, 0, 0 };

		private final double[] t2 = { 0, 0, 0 };

		private final double[] t3 = { 0, 0, 0 };

		private final double[] t4 = { 0, 0, 0 };

		/**
		 * @param parent
		 * @param curr
		 * @param tmax
		 * @param isDark
		 */
		private boolean addSegment(final PNode parent,
				final Entry<Double, R1RNFunction> curr, final double tmax,
				final boolean isDark) {
			if (curr.getKey() >= tmax)
				return false;
			doSections(sections, curr.getKey(), tmax);
			if (flog.isDebugEnabled()) {
				final StringWriter wri = new StringWriter();
				wri.write("t=");
				toString(wri, sections);
				wri.write(" c=" + curr.getValue());
				flog.debug(wri.getBuffer());
			}
			parent.addChild(createNode(isDark, curr.getValue(), sections));
			return true;
		}

		private PNode createNode(final boolean isDark, final R1RNFunction curr,
				final double[] sections) {
			final PPath c;
			if (true)
				c = new PPath(CurveShape.approximateLinear(curr, sections,
						zoom, t1), stroke);
			else
				c = new PPath(CurveShape.approximateQuadratic(curr, sections,
						zoom, t1, t2, t3, t4), stroke);
			c.setPickable(false);
			c.setPaint(null);
			c.setStrokePaint(isDark ? dark : light);
			return c;
		}

		/**
		 * Split the given interval into sections.
		 * 
		 * @see CurveShape#exponentialSections(double, double, double[])
		 * @param sections
		 * @param min
		 * @param max
		 * @return sections
		 */
		private double[] doSections(final double[] sections, final double min,
				final double max) {
			if (true)
				return CurveShape.aequidistantSections(min, max, sections);
			return CurveShape.exponentialSections(min, max, sections);
		}

		@Override
		public PNode newInstance(final int i8, final boolean isDark,
				final Iterator<Entry<Double, R1RNFunction>> path,
				final double tmax) {
			final PNode r = new PComposite();
			if (!path.hasNext())
				return r;
			Entry<Double, R1RNFunction> curr = path.next();
			while (path.hasNext()) {
				final Entry<Double, R1RNFunction> next = path.next();
				if (!addSegment(r, curr, next.getKey(), isDark))
					break;
				// step:
				curr = next;
			}
			// don't forget the tail (last segment):
			addSegment(r, curr, tmax, isDark);
			r.setChildrenPickable(false);
			r.setPickable(false);
			return r;
		}
	}

	private static final Log flog = JCLoggerFactory
			.getLogger(PTrajectoryFactory.Fancy.class);

	public abstract PNode newInstance(int i8, boolean isDark,
			Iterator<Entry<Double, R1RNFunction>> t, double tmax);

	public PNode newInstance(final int i16,
			final Iterator<Entry<Double, R1RNFunction>> t, final double tmax) {
		final PNode r = newInstance(RockSet.toIdx8(i16), RockSet.isDark(i16), t, tmax);
		r.addAttribute(PRockNode.INDEX16, 16);
		return r;
	}
}

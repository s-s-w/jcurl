/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
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
package org.jcurl.demo.tactics;

import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.Factory;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.IceShapes;
import org.jcurl.math.CurveShape;
import org.jcurl.math.R1RNFunction;

import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGShape;
import com.sun.scenario.scenegraph.SGAbstractShape.Mode;

/**
 * Create an unpickable {@link SGGroup} for a combined curve describing the path
 * of one rock.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PTrajectoryFactory.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public abstract class SGTrajectoryFactory implements Factory {
	public static class Fancy extends SGTrajectoryFactory {

		private static final Paint dark = IceShapes.trace(
				new IceShapes.RockColors().dark, 255);
		private static final Paint light = IceShapes.trace(
				new IceShapes.RockColors().light, 255);
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

		@Override
		protected boolean addSegment(final Entry<Double, R1RNFunction> src,
				final double tmax, final boolean isDark, final SGGroup dst) {
			if (src.getKey() >= tmax)
				return false;
			doSections(sections, src.getKey(), tmax);
			if (log.isDebugEnabled()) {
				final StringWriter wri = new StringWriter();
				wri.write("t=");
				toString(wri, sections);
				wri.write(" c=" + src.getValue());
				log.debug(wri.getBuffer());
			}
			dst.add(createSegment(isDark, src.getValue(), sections));
			return true;
		}

		private SGNode createSegment(final boolean isDark,
				final R1RNFunction curr, final double[] sections) {
			final Shape s;
			if (true)
				s = CurveShape.approximateLinear(curr, sections, zoom, t1);
			else
				s = CurveShape.approximateQuadratic(curr, sections, zoom, t1,
						t2, t3, t4);
			final SGShape c = new SGShape();
			c.setShape(s);
			c.setDrawStroke(stroke);
			c.setDrawPaint(isDark ? dark : light);
			c.setAntialiasingHint(RenderingHints.VALUE_ANTIALIAS_ON);
			c.setMode(Mode.STROKE);
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
	}

	private static final Log log = JCLoggerFactory
			.getLogger(SGTrajectoryFactory.Fancy.class);

	protected abstract boolean addSegment(Entry<Double, R1RNFunction> src,
			double tmax, boolean isDark, SGGroup dst);

	public SGGroup refresh(final Iterator<Entry<Double, R1RNFunction>> path,
			final boolean isDark, final double tmin, final double tmax,
			SGGroup dst) {
		if (dst == null)
			dst = new SGGroup();
		final boolean vis = dst.isVisible();
		try {
			dst.setVisible(false);
			for (int i = dst.getChildren().size() - 1; i >= 0; i--)
				dst.remove(i);

			if (!path.hasNext())
				return dst;
			Entry<Double, R1RNFunction> curr = path.next();
			while (path.hasNext()) {
				final Entry<Double, R1RNFunction> next = path.next();
				if (!addSegment(curr, next.getKey(), isDark, dst))
					break;
				// step:
				curr = next;
			}
			// don't forget the tail (last segment):
			addSegment(curr, tmax, isDark, dst);
			dst.setMouseBlocker(true);
		} finally {
			dst.setVisible(vis);
		}
		return dst;
	}
}

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
package org.jcurl.zui.piccolo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.GenTrajectoryFactory;
import org.jcurl.core.ui.IceShapes;
import org.jcurl.core.ui.JCurlShaper;
import org.jcurl.math.Interpolator;
import org.jcurl.math.Interpolators;
import org.jcurl.math.R1RNFunction;
import org.jcurl.math.Shaper;
import org.jcurl.math.ShaperUtils;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;

/**
 * Create an unpickable {@link PNode} for a combined curve describing the path
 * of one rock.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:PTrajectoryFactory.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public abstract class PTrajectoryFactory extends GenTrajectoryFactory<PNode> {

	/**
	 * For testing reasons only: Show 2 thin lines, one for high-sampled lineTo
	 * and on for low-sampled quadTo/curveTo.
	 */
	public static class Compare extends PTrajectoryFactory {
		private static final Paint bezier = Color.BLACK;
		private static final Interpolator cip = Interpolators
				.getLinearInstance();
		private static final Paint line = Color.GREEN;
		/** very fine line sampling */
		private static final JCurlShaper liner = new JCurlShaper() {
			@Override
			protected Shape doRender(R1RNFunction f, double tmin, double tmax,
					int samples, float zoom, Interpolator ip) {
				return ShaperUtils.approximateLinear(f, tmin, tmax, 1000,
						zoom, cip);
			}
		};
		private static final Stroke lineS = new BasicStroke(0.01F);
		private static final JCurlShaper sh = new JCurlShaper();
		private static final Stroke stroke = new BasicStroke(0.005F);

		@Override
		protected void addSegment(final R1RNFunction src, final double tmin,
				final double tmax, final boolean isDark, final PNode dst) {
			log.debug("  segment");
			Shape s = liner.toShape(src, tmin, tmax);
			if (s != null) {
				final PPath c = new PPath(s, lineS);
				c.setPaint(null);
				c.setStrokePaint(line);
				dst.addChild(c);
			}
			s = sh.toShape(src, tmin, tmax);
			if (s != null) {
				final PPath c = new PPath(s, stroke);
				c.setPaint(null);
				c.setStrokePaint(bezier);
				dst.addChild(c);
			}
		}
	}

	public static class Fancy extends PTrajectoryFactory {

		private static final Paint dark = IceShapes.trace(
				new IceShapes.RockColors().dark, 100);
		private static final Paint light = IceShapes.trace(
				new IceShapes.RockColors().light, 100);
		/** sampling style along the {@link R1RNFunction} */
		private static final Shaper sh = new JCurlShaper();
		private static final Stroke stroke = new BasicStroke(
				2 * RockProps.DEFAULT.getRadius(), BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0);

		@Override
		protected void addSegment(final R1RNFunction src, final double tmin,
				final double tmax, final boolean isDark, final PNode dst) {
			final Shape s = sh.toShape(src, tmin, tmax);
			if (s == null)
				return;
			final PPath c = new PPath(s, stroke);
			c.setPaint(null);
			c.setStrokePaint(isDark ? dark : light);
			dst.addChild(c);
		}
	}

	private static final Log log = JCLoggerFactory
			.getLogger(PTrajectoryFactory.class);

	@Override
	protected PNode post(final PNode parent) {
		parent.setVisible(true);
		return parent;
	}

	@Override
	protected PNode pre(PNode parent) {
		if (parent == null)
			parent = new PNode();
		parent.setVisible(false);
		parent.removeAllChildren();
		return parent;
	}
}

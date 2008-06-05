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
import java.awt.Paint;
import java.awt.Stroke;

import org.jcurl.core.api.RockProps;
import org.jcurl.core.ui.GenTrajectoryFactory;
import org.jcurl.core.ui.IceShapes;
import org.jcurl.math.CurveShape;
import org.jcurl.math.Interpolator;
import org.jcurl.math.Interpolators;
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
public abstract class PTrajectoryFactory extends GenTrajectoryFactory<PNode> {

	public static class Fancy extends PTrajectoryFactory {

		private static final Paint dark = IceShapes.trace(
				new IceShapes.RockColors().dark, 100);
		private static final Interpolator ip = Interpolators
				.getLinearInstance();
		private static final Paint light = IceShapes.trace(
				new IceShapes.RockColors().light, 100);
		private static final int samples = 25;
		private static final Stroke stroke = new BasicStroke(
				2 * RockProps.DEFAULT.getRadius(), BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0);
		private static final float zoom = 1;

		@Override
		protected boolean addSegment(final R1RNFunction src, final double tmin,
				final double tmax, final boolean isDark, final PNode dst) {
			// TODO move to #refresh
			if (tmin >= tmax)
				return false;
			final PPath c;
			c = new PPath(CurveShape.approximateLinear(src, (float) tmin,
					(float) tmax, samples, zoom, ip), stroke);
			c.setPaint(null);
			c.setStrokePaint(isDark ? dark : light);
			dst.addChild(c);
			return true;
		}
	}

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

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
import java.awt.Shape;
import java.awt.Stroke;

import org.jcurl.core.api.RockProps;
import org.jcurl.core.ui.GenTrajectoryFactory;
import org.jcurl.core.ui.IceShapes;
import org.jcurl.core.ui.JCurlShaper;
import org.jcurl.math.R1RNFunction;
import org.jcurl.math.Shaper;

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
			if(s == null)
				return;
			final PPath c = new PPath(s, stroke);
			c.setPaint(null);
			c.setStrokePaint(isDark ? dark : light);
			dst.addChild(c);
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

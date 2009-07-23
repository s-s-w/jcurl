/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.Orientation;
import org.jcurl.core.ui.Zoomer;

abstract class WCComponent extends Component implements WCLayer {

	private static final Log log = JCLoggerFactory.getLogger(WCComponent.class);

	protected int oldHei = -1;

	protected int oldWid = -1;

	protected Orientation orient = Orientation.W;

	protected final AffineTransform wc_mat = new AffineTransform();

	private Zoomer zoom = null;

	/**
	 * Convert display to world-coordinates.
	 * 
	 * @param dc
	 * @param wc
	 * @return world coordinates
	 */
	public Point2D dc2wc(final Point2D dc, final Point2D wc) {
		try {
			return wc_mat.inverseTransform(dc, wc);
		} catch (final NoninvertibleTransformException e) {
			throw new RuntimeException("Why uninvertible?", e);
		}
	}

	public void exportPng(File dst, final String watermark) throws IOException {
		if (!dst.getName().endsWith(".png"))
			dst = new File(dst.getName() + ".png");
		ImageIO.write(renderPng(watermark), "png", dst);
	}

	public void exportPng(final OutputStream dst, final String watermark)
			throws IOException {
		ImageIO.write(renderPng(watermark), "png", dst);
	}

	public Zoomer getZoom() {
		return zoom;
	}

	private BufferedImage renderPng(final String watermark) {
		final BufferedImage img = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2 = (Graphics2D) img.getGraphics();
		{
			final Map<Key, Object> hints = new HashMap<Key, Object>();
			hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
					RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			hints.put(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			hints.put(RenderingHints.KEY_COLOR_RENDERING,
					RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			hints.put(RenderingHints.KEY_DITHERING,
					RenderingHints.VALUE_DITHER_ENABLE);
			hints.put(RenderingHints.KEY_FRACTIONALMETRICS,
					RenderingHints.VALUE_FRACTIONALMETRICS_ON);
			hints.put(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			hints.put(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);
			hints.put(RenderingHints.KEY_STROKE_CONTROL,
					RenderingHints.VALUE_STROKE_NORMALIZE);
			hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.addRenderingHints(hints);
		}
		final Font f0 = g2.getFont();
		paint(g2);
		g2.setTransform(new AffineTransform());
		if (watermark != null) {
			if (log.isDebugEnabled())
				log.debug(f0);
			g2.setFont(f0);
			g2.setColor(new Color(0, 0, 0, 128));
			g2.drawString(watermark, 10, 20);
		}
		g2.dispose();
		return img;
	}

	/**
	 * Either the wc viewport, fixpoint or dc viewport has changed: re-compute
	 * the transformation {@link #wc_mat}.
	 * 
	 * @return was the transformation changed?
	 */
	protected boolean resized() {
		final int w = getWidth();
		final int h = getHeight();
		if (oldWid != w || oldHei != h) {
			getZoom().computeWctoDcTrafo(this.getBounds(), wc_mat);
			oldWid = w;
			oldHei = h;
			return true;
		}
		return false;
	}

	public void setZoom(final Zoomer zoom) {
		this.zoom = zoom;
		this.repaint();
	}

	/**
	 * Convert world- to display coordinates.
	 * 
	 * @param wc
	 * @param dc
	 * @return display coordinates
	 */
	public Point2D wc2dc(final Point2D wc, final Point2D dc) {
		return wc_mat.transform(wc, dc);
	}
}

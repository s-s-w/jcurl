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

package org.jcurl.svg;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.RenderingHints.Key;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.jcurl.core.helpers.XmlSimpleWriter;
import org.jcurl.core.log.JCLoggerFactory;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class Graphics2Svg extends Graphics2DAntiAdapter {

	private static final Log log = JCLoggerFactory
			.getLogger(Graphics2Svg.class);

	public static Graphics2D createGraphics(final int width, final int height,
			final File dst) throws FileNotFoundException {
		try {
			return createGraphics(height, width, new XmlSimpleWriter(
					new FileOutputStream(dst), "utf-8", false));
		} catch (final UnsupportedEncodingException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	public static Graphics2D createGraphics(final int width, final int height,
			final XmlSimpleWriter dst) {
		return new Graphics2Svg(width, height, dst);
	}

	private final Rectangle clip = new Rectangle(0, 0, 1, 1);
	private final XmlSimpleWriter dst;
	private final int nestingLevel;
	private final AffineTransform trafo = new AffineTransform();

	private Graphics2Svg(final Graphics2Svg src) {
		nestingLevel = src.nestingLevel + 1;
		dst = src.dst;
		clip.setBounds(src.clip);
		setTransform(src.trafo);
	}

	private Graphics2Svg(final int width, final int height,
			final XmlSimpleWriter dst) {
		this.dst = dst;
		nestingLevel = 0;
		clip.setBounds(0, 0, width, height);
	}

	@Override
	public void clip(final Shape s) {
		try {
			dst.comment("clip");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.info("implemented");
	}

	@Override
	public void clipRect(final int x, final int y, final int width,
			final int height) {
		try {
			dst.comment("clipRect");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.info("implemented");
		clip.setBounds(x, y, width, height);
	}

	@Override
	public Graphics create() {
		try {
			dst.comment("create");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.warn("Not implemented");
		return new Graphics2Svg(this);
	}

	@Override
	public Graphics create(final int x, final int y, final int width,
			final int height) {
		try {
			dst.comment("create");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.info("implemented");
		final Graphics ret = create();
		ret.clipRect(x, y, width, height);
		return ret;
	}

	@Override
	public void dispose() {
		if (nestingLevel > 0)
			return;
		try {
			dst.endDocument();
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	@Override
	public void draw3DRect(final int x, final int y, final int width,
			final int height, final boolean raised) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawBytes(final byte[] data, final int offset,
			final int length, final int x, final int y) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawChars(final char[] data, final int offset,
			final int length, final int x, final int y) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawPolygon(final Polygon p) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawRect(final int x, final int y, final int width,
			final int height) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void fill3DRect(final int x, final int y, final int width,
			final int height, final boolean raised) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void fillPolygon(final Polygon p) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void fillRect(final int x, final int y, final int width,
			final int height) {
		try {
			dst.comment("fillRect");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.warn("Not implemented");
	}

	@Override
	public Rectangle getClipBounds() {
		try {
			dst.comment("getClipBounds");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.info("implemented");
		return clip;
	}

	@Override
	public Rectangle getClipBounds(final Rectangle r) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Rectangle getClipRect() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public FontMetrics getFontMetrics() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public AffineTransform getTransform() {
		try {
			dst.comment("getTransform");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.info("implemented");
		return trafo;
	}

	@Override
	public void setBackground(final Color color) {
		try {
			dst.comment("setBackground");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.warn("Not implemented");
	}

	@Override
	public void setClip(final Shape clip) {
		try {
			dst.comment("setClip");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.warn("Not implemented");
	}

	@Override
	public void setColor(final Color c) {
		try {
			dst.comment("setColor");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.warn("Not implemented");
	}

	@Override
	public void setFont(final Font font) {
		try {
			dst.comment("setFont");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.warn("Not implemented");
	}

	@Override
	public void setRenderingHint(final Key hintKey, final Object hintValue) {
		try {
			dst.comment("setRenderingHint");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.info("implemented");
	}

	@Override
	public void setTransform(final AffineTransform Tx) {
		try {
			dst.comment("setTransform");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.info("implemented");
		trafo.setTransform(Tx);
	}

	@Override
	public String toString() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void transform(final AffineTransform Tx) {
		try {
			dst.comment("transform");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.info("implemented");
		trafo.preConcatenate(Tx);
	}

	@Override
	public void translate(final int x, final int y) {
		try {
			dst.comment("translate");
		} catch (final SAXException e) {
			throw new RuntimeException("Unhandled", e);
		}
		log.info("implemented");
		trafo.translate(x, y);
	}
}

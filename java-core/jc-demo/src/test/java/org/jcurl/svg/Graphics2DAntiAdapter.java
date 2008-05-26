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
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public abstract class Graphics2DAntiAdapter extends Graphics2D {

	@Override
	public void addRenderingHints(final Map<?, ?> hints) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void clearRect(final int x, final int y, final int width,
			final int height) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void clip(final Shape s) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void clipRect(final int x, final int y, final int width,
			final int height) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void copyArea(final int x, final int y, final int width,
			final int height, final int dx, final int dy) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Graphics create() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void dispose() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void draw(final Shape s) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawArc(final int x, final int y, final int width,
			final int height, final int startAngle, final int arcAngle) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawGlyphVector(final GlyphVector g, final float x,
			final float y) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawImage(final BufferedImage img, final BufferedImageOp op,
			final int x, final int y) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public boolean drawImage(final Image img, final AffineTransform xform,
			final ImageObserver obs) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public boolean drawImage(final Image img, final int x, final int y,
			final Color bgcolor, final ImageObserver observer) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public boolean drawImage(final Image img, final int x, final int y,
			final ImageObserver observer) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public boolean drawImage(final Image img, final int x, final int y,
			final int width, final int height, final Color bgcolor,
			final ImageObserver observer) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public boolean drawImage(final Image img, final int x, final int y,
			final int width, final int height, final ImageObserver observer) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public boolean drawImage(final Image img, final int dx1, final int dy1,
			final int dx2, final int dy2, final int sx1, final int sy1,
			final int sx2, final int sy2, final Color bgcolor,
			final ImageObserver observer) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public boolean drawImage(final Image img, final int dx1, final int dy1,
			final int dx2, final int dy2, final int sx1, final int sy1,
			final int sx2, final int sy2, final ImageObserver observer) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawLine(final int x1, final int y1, final int x2, final int y2) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawOval(final int x, final int y, final int width,
			final int height) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawPolygon(final int[] points, final int[] points2,
			final int points3) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawPolyline(final int[] points, final int[] points2,
			final int points3) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawRenderableImage(final RenderableImage img,
			final AffineTransform xform) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawRenderedImage(final RenderedImage img,
			final AffineTransform xform) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawRoundRect(final int x, final int y, final int width,
			final int height, final int arcWidth, final int arcHeight) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawString(final AttributedCharacterIterator iterator,
			final float x, final float y) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawString(final AttributedCharacterIterator iterator,
			final int x, final int y) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawString(final String s, final float x, final float y) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void drawString(final String str, final int x, final int y) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void fill(final Shape s) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void fillArc(final int x, final int y, final int width,
			final int height, final int startAngle, final int arcAngle) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void fillOval(final int x, final int y, final int width,
			final int height) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void fillPolygon(final int[] points, final int[] points2,
			final int points3) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void fillRect(final int x, final int y, final int width,
			final int height) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void fillRoundRect(final int x, final int y, final int width,
			final int height, final int arcWidth, final int arcHeight) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Color getBackground() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Shape getClip() {
		return new Rectangle2D.Double(0, 0, 1, 1);
	}

	@Override
	public Rectangle getClipBounds() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Color getColor() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Composite getComposite() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Font getFont() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public FontMetrics getFontMetrics(final Font f) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Paint getPaint() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Object getRenderingHint(final Key hintKey) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public RenderingHints getRenderingHints() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public Stroke getStroke() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public AffineTransform getTransform() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public boolean hit(final Rectangle rect, final Shape s,
			final boolean onStroke) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void rotate(final double theta) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void rotate(final double theta, final double x, final double y) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void scale(final double sx, final double sy) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setBackground(final Color color) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setClip(final int x, final int y, final int width,
			final int height) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setClip(final Shape clip) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setColor(final Color c) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setComposite(final Composite comp) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setFont(final Font font) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setPaint(final Paint paint) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setPaintMode() {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setRenderingHint(final Key hintKey, final Object hintValue) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setRenderingHints(final Map<?, ?> hints) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setStroke(final Stroke s) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setTransform(final AffineTransform Tx) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void setXORMode(final Color c1) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void shear(final double shx, final double shy) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void transform(final AffineTransform Tx) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void translate(final double tx, final double ty) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	@Override
	public void translate(final int x, final int y) {
		throw new UnsupportedOperationException("Not implemented yet.");
	}
}
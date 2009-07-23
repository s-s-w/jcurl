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
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class Graphics2DWrap extends Graphics2D {

	private static final Log log = JCLoggerFactory
			.getLogger(Graphics2DWrap.class);

	private final Graphics2D dst;

	public Graphics2DWrap(final Graphics dst) {
		this((Graphics2D) dst);
	}

	public Graphics2DWrap(final Graphics2D dst) {
		this.dst = dst;
	}

	@Override
	public void addRenderingHints(final Map<?, ?> hints) {
		log.info("wrapped");
		dst.addRenderingHints(hints);
	}

	@Override
	public void clearRect(final int x, final int y, final int width,
			final int height) {
		log.info("wrapped");
		dst.clearRect(x, y, width, height);
	}

	@Override
	public void clip(final Shape s) {
		log.info("wrapped");
		dst.clip(s);
	}

	@Override
	public void clipRect(final int x, final int y, final int width,
			final int height) {
		log.info("wrapped");
		dst.clipRect(x, y, width, height);
	}

	@Override
	public void copyArea(final int x, final int y, final int width,
			final int height, final int dx, final int dy) {
		log.info("wrapped");
		dst.copyArea(x, y, width, height, dx, dy);
	}

	@Override
	public Graphics create() {
		log.info("wrapped");
		return new Graphics2DWrap(dst.create());
	}

	@Override
	public Graphics create(final int x, final int y, final int width,
			final int height) {
		log.info("wrapped");
		return new Graphics2DWrap(dst.create(x, y, width, height));
	}

	@Override
	public void dispose() {
		log.info("wrapped");
		dst.dispose();
	}

	@Override
	public void draw(final Shape s) {
		log.info("wrapped");
		dst.draw(s);
	}

	@Override
	public void draw3DRect(final int x, final int y, final int width,
			final int height, final boolean raised) {
		log.info("wrapped");
		dst.draw3DRect(x, y, width, height, raised);
	}

	@Override
	public void drawArc(final int x, final int y, final int width,
			final int height, final int startAngle, final int arcAngle) {
		log.info("wrapped");
		dst.drawArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void drawBytes(final byte[] data, final int offset,
			final int length, final int x, final int y) {
		log.info("wrapped");
		dst.drawBytes(data, offset, length, x, y);
	}

	@Override
	public void drawChars(final char[] data, final int offset,
			final int length, final int x, final int y) {
		log.info("wrapped");
		dst.drawChars(data, offset, length, x, y);
	}

	@Override
	public void drawGlyphVector(final GlyphVector g, final float x,
			final float y) {
		log.info("wrapped");
		dst.drawGlyphVector(g, x, y);
	}

	@Override
	public void drawImage(final BufferedImage img, final BufferedImageOp op,
			final int x, final int y) {
		log.info("wrapped");
		dst.drawImage(img, op, x, y);
	}

	@Override
	public boolean drawImage(final Image img, final AffineTransform xform,
			final ImageObserver obs) {
		log.info("wrapped");
		return dst.drawImage(img, xform, obs);
	}

	@Override
	public boolean drawImage(final Image img, final int x, final int y,
			final Color bgcolor, final ImageObserver observer) {
		log.info("wrapped");
		return dst.drawImage(img, x, y, bgcolor, observer);
	}

	@Override
	public boolean drawImage(final Image img, final int x, final int y,
			final ImageObserver observer) {
		log.info("wrapped");
		return dst.drawImage(img, x, y, observer);
	}

	@Override
	public boolean drawImage(final Image img, final int x, final int y,
			final int width, final int height, final Color bgcolor,
			final ImageObserver observer) {
		log.info("wrapped");
		return dst.drawImage(img, x, y, width, height, bgcolor, observer);
	}

	@Override
	public boolean drawImage(final Image img, final int x, final int y,
			final int width, final int height, final ImageObserver observer) {
		log.info("wrapped");
		return dst.drawImage(img, x, y, width, height, observer);
	}

	@Override
	public boolean drawImage(final Image img, final int dx1, final int dy1,
			final int dx2, final int dy2, final int sx1, final int sy1,
			final int sx2, final int sy2, final Color bgcolor,
			final ImageObserver observer) {
		log.info("wrapped");
		return dst.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2,
				bgcolor, observer);
	}

	@Override
	public boolean drawImage(final Image img, final int dx1, final int dy1,
			final int dx2, final int dy2, final int sx1, final int sy1,
			final int sx2, final int sy2, final ImageObserver observer) {
		log.info("wrapped");
		return dst.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2,
				observer);
	}

	@Override
	public void drawLine(final int x1, final int y1, final int x2, final int y2) {
		log.info("wrapped");
		dst.drawLine(x1, y1, x2, y2);
	}

	@Override
	public void drawOval(final int x, final int y, final int width,
			final int height) {
		log.info("wrapped");
		dst.drawOval(x, y, width, height);
	}

	@Override
	public void drawPolygon(final int[] points, final int[] points2,
			final int points3) {
		log.info("wrapped");
		dst.drawPolygon(points, points2, points3);
	}

	@Override
	public void drawPolygon(final Polygon p) {
		log.info("wrapped");
		dst.drawPolygon(p);
	}

	@Override
	public void drawPolyline(final int[] points, final int[] points2,
			final int points3) {
		log.info("wrapped");
		dst.drawPolyline(points, points2, points3);
	}

	@Override
	public void drawRect(final int x, final int y, final int width,
			final int height) {
		log.info("wrapped");
		dst.drawRect(x, y, width, height);
	}

	@Override
	public void drawRenderableImage(final RenderableImage img,
			final AffineTransform xform) {
		log.info("wrapped");
		dst.drawRenderableImage(img, xform);
	}

	@Override
	public void drawRenderedImage(final RenderedImage img,
			final AffineTransform xform) {
		log.info("wrapped");
		dst.drawRenderedImage(img, xform);
	}

	@Override
	public void drawRoundRect(final int x, final int y, final int width,
			final int height, final int arcWidth, final int arcHeight) {
		log.info("wrapped");
		dst.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public void drawString(final AttributedCharacterIterator iterator,
			final float x, final float y) {
		log.info("wrapped");
		dst.drawString(iterator, x, y);
	}

	@Override
	public void drawString(final AttributedCharacterIterator iterator,
			final int x, final int y) {
		log.info("wrapped");
		dst.drawString(iterator, x, y);
	}

	@Override
	public void drawString(final String s, final float x, final float y) {
		log.info("wrapped");
		dst.drawString(s, x, y);
	}

	@Override
	public void drawString(final String str, final int x, final int y) {
		log.info("wrapped");
		dst.drawString(str, x, y);
	}

	@Override
	public boolean equals(final Object obj) {
		log.info("wrapped");
		return dst.equals(obj);
	}

	@Override
	public void fill(final Shape s) {
		log.info("wrapped");
		dst.fill(s);
	}

	@Override
	public void fill3DRect(final int x, final int y, final int width,
			final int height, final boolean raised) {
		log.info("wrapped");
		dst.fill3DRect(x, y, width, height, raised);
	}

	@Override
	public void fillArc(final int x, final int y, final int width,
			final int height, final int startAngle, final int arcAngle) {
		log.info("wrapped");
		dst.fillArc(x, y, width, height, startAngle, arcAngle);
	}

	@Override
	public void fillOval(final int x, final int y, final int width,
			final int height) {
		log.info("wrapped");
		dst.fillOval(x, y, width, height);
	}

	@Override
	public void fillPolygon(final int[] points, final int[] points2,
			final int points3) {
		log.info("wrapped");
		dst.fillPolygon(points, points2, points3);
	}

	@Override
	public void fillPolygon(final Polygon p) {
		log.info("wrapped");
		dst.fillPolygon(p);
	}

	@Override
	public void fillRect(final int x, final int y, final int width,
			final int height) {
		log.info("wrapped");
		dst.fillRect(x, y, width, height);
	}

	@Override
	public void fillRoundRect(final int x, final int y, final int width,
			final int height, final int arcWidth, final int arcHeight) {
		log.info("wrapped");
		dst.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public Color getBackground() {
		log.info("wrapped");
		return dst.getBackground();
	}

	@Override
	public Shape getClip() {
		log.info("wrapped");
		return dst.getClip();
	}

	@Override
	public Rectangle getClipBounds() {
		log.info("wrapped");
		return dst.getClipBounds();
	}

	@Override
	public Rectangle getClipBounds(final Rectangle r) {
		log.info("wrapped");
		return dst.getClipBounds(r);
	}

	@Override
	public Rectangle getClipRect() {
		log.info("wrapped");
		return dst.getClipRect();
	}

	@Override
	public Color getColor() {
		log.info("wrapped");
		return dst.getColor();
	}

	@Override
	public Composite getComposite() {
		log.info("wrapped");
		return dst.getComposite();
	}

	@Override
	public GraphicsConfiguration getDeviceConfiguration() {
		log.info("wrapped");
		return dst.getDeviceConfiguration();
	}

	@Override
	public Font getFont() {
		log.info("wrapped");
		return dst.getFont();
	}

	@Override
	public FontMetrics getFontMetrics() {
		log.info("wrapped");
		return dst.getFontMetrics();
	}

	@Override
	public FontMetrics getFontMetrics(final Font f) {
		log.info("wrapped");
		return dst.getFontMetrics(f);
	}

	@Override
	public FontRenderContext getFontRenderContext() {
		log.info("wrapped");
		return dst.getFontRenderContext();
	}

	@Override
	public Paint getPaint() {
		log.info("wrapped");
		return dst.getPaint();
	}

	@Override
	public Object getRenderingHint(final Key hintKey) {
		log.info("wrapped");
		return dst.getRenderingHint(hintKey);
	}

	@Override
	public RenderingHints getRenderingHints() {
		log.info("wrapped");
		return dst.getRenderingHints();
	}

	@Override
	public Stroke getStroke() {
		log.info("wrapped");
		return dst.getStroke();
	}

	@Override
	public AffineTransform getTransform() {
		log.info("wrapped");
		return dst.getTransform();
	}

	@Override
	public int hashCode() {
		log.info("wrapped");
		return dst.hashCode();
	}

	@Override
	public boolean hit(final Rectangle rect, final Shape s,
			final boolean onStroke) {
		log.info("wrapped");
		return dst.hit(rect, s, onStroke);
	}

	@Override
	public boolean hitClip(final int x, final int y, final int width,
			final int height) {
		log.info("wrapped");
		return dst.hitClip(x, y, width, height);
	}

	public void rotate(final double theta) {
		log.info("wrapped");
		dst.rotate(theta);
	}

	public void rotate(final double theta, final double x, final double y) {
		log.info("wrapped");
		dst.rotate(theta, x, y);
	}

	public void scale(final double sx, final double sy) {
		log.info("wrapped");
		dst.scale(sx, sy);
	}

	public void setBackground(final Color color) {
		log.info("wrapped");
		dst.setBackground(color);
	}

	public void setClip(final int x, final int y, final int width,
			final int height) {
		log.info("wrapped");
		dst.setClip(x, y, width, height);
	}

	public void setClip(final Shape clip) {
		log.info("wrapped");
		dst.setClip(clip);
	}

	public void setColor(final Color c) {
		log.info("wrapped");
		dst.setColor(c);
	}

	public void setComposite(final Composite comp) {
		log.info("wrapped");
		dst.setComposite(comp);
	}

	public void setFont(final Font font) {
		log.info("wrapped");
		dst.setFont(font);
	}

	public void setPaint(final Paint paint) {
		log.info("wrapped");
		dst.setPaint(paint);
	}

	public void setPaintMode() {
		log.info("wrapped");
		dst.setPaintMode();
	}

	public void setRenderingHint(final Key hintKey, final Object hintValue) {
		log.info("wrapped");
		dst.setRenderingHint(hintKey, hintValue);
	}

	public void setRenderingHints(final Map<?, ?> hints) {
		log.info("wrapped");
		dst.setRenderingHints(hints);
	}

	public void setStroke(final Stroke s) {
		log.info("wrapped");
		dst.setStroke(s);
	}

	public void setTransform(final AffineTransform Tx) {
		log.info("wrapped");
		dst.setTransform(Tx);
	}

	public void setXORMode(final Color c1) {
		log.info("wrapped");
		dst.setXORMode(c1);
	}

	public void shear(final double shx, final double shy) {
		log.info("wrapped");
		dst.shear(shx, shy);
	}

	public void transform(final AffineTransform Tx) {
		log.info("wrapped");
		dst.transform(Tx);
	}

	public void translate(final double tx, final double ty) {
		log.info("wrapped");
		dst.translate(tx, ty);
	}

	public void translate(final int x, final int y) {
		log.info("wrapped");
		dst.translate(x, y);
	}
}

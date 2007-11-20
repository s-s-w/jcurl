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
package org.jcurl.core.svg;

import java.awt.BasicStroke;
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
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

import org.jcurl.core.helpers.NotImplementedYetException;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class MiniSvg extends Graphics2D {

    private static AttributesImpl aa(final AttributesImpl a, final String name,
            final CharSequence value) {
        a.addAttribute(null, name, null, "String", value.toString());
        return a;
    }

    private static AttributesImpl aa(final AttributesImpl a, final String name,
            final double value) {
        a.addAttribute(null, name, null, "String", Double.toString(value));
        return a;
    }

    private final ContentHandler dst;

    private BasicStroke stroke = new BasicStroke(1);

    public MiniSvg(final ContentHandler dst) {
        this.dst = dst;
        try {
            this.dst.startDocument();
        } catch (final SAXException e) {
            throw new RuntimeException("Unhandled", e);
        }
        final AttributesImpl a = new AttributesImpl();
        aa(a, "xmlns", "http://www.w3.org/2000/svg");
        a.addAttribute(null, "version", null, "String", "1.0");
        startElement("svg", a);
        a.clear();
        aa(a, "stroke", "black");
        aa(a, "stroke-width", stroke.getLineWidth());
        startElement("g", a);
    }

    @Override
    public void addRenderingHints(final Map<?, ?> hints) {
        throw new NotImplementedYetException();
    }

    @Override
    public void clearRect(final int x, final int y, final int width,
            final int height) {
        throw new NotImplementedYetException();
    }

    @Override
    public void clip(final Shape s) {
        throw new NotImplementedYetException();
    }

    @Override
    public void clipRect(final int x, final int y, final int width,
            final int height) {
        throw new NotImplementedYetException();
    }

    @Override
    public void copyArea(final int x, final int y, final int width,
            final int height, final int dx, final int dy) {
        throw new NotImplementedYetException();
    }

    @Override
    public Graphics create() {
        throw new NotImplementedYetException();
    }

    @Override
    public void dispose() {
        endElement("g");
        endElement("svg");
        try {
            dst.endDocument();
        } catch (final SAXException e) {
            throw new RuntimeException("Unhandled", e);
        }
    }

    @Override
    public void draw(final Shape s) {
        throw new NotImplementedYetException();
    }

    /**
     * http://www.w3.org/TR/SVG11/paths.html#PathDataEllipticalArcCommands
     */
    @Override
    public void drawArc(final int x, final int y, final int width,
            final int height, final int startAngle, final int arcAngle) {
        // <path d="M300,200 h-150 a150,150 0 1,0 150,-150 z"
        // fill="red" stroke="blue" stroke-width="5" />
        final AttributesImpl a = new AttributesImpl();
        final StringBuffer d = new StringBuffer();
        d.append("M300,200 h-150 a150,150 0 1,0 150,-150 z");
        aa(a, "d", d);
        aa(a, "fill", "none");
        startElement("path", a);
        endElement("path");
    }

    @Override
    public void drawGlyphVector(final GlyphVector g, final float x,
            final float y) {
        throw new NotImplementedYetException();
    }

    @Override
    public void drawImage(final BufferedImage img, final BufferedImageOp op,
            final int x, final int y) {
        throw new NotImplementedYetException();
    }

    @Override
    public boolean drawImage(final Image img, final AffineTransform xform,
            final ImageObserver obs) {
        throw new NotImplementedYetException();
    }

    @Override
    public boolean drawImage(final Image img, final int x, final int y,
            final Color bgcolor, final ImageObserver observer) {
        throw new NotImplementedYetException();
    }

    @Override
    public boolean drawImage(final Image img, final int x, final int y,
            final ImageObserver observer) {
        throw new NotImplementedYetException();
    }

    @Override
    public boolean drawImage(final Image img, final int x, final int y,
            final int width, final int height, final Color bgcolor,
            final ImageObserver observer) {
        throw new NotImplementedYetException();
    }

    @Override
    public boolean drawImage(final Image img, final int x, final int y,
            final int width, final int height, final ImageObserver observer) {
        throw new NotImplementedYetException();
    }

    @Override
    public boolean drawImage(final Image img, final int dx1, final int dy1,
            final int dx2, final int dy2, final int sx1, final int sy1,
            final int sx2, final int sy2, final Color bgcolor,
            final ImageObserver observer) {
        throw new NotImplementedYetException();
    }

    @Override
    public boolean drawImage(final Image img, final int dx1, final int dy1,
            final int dx2, final int dy2, final int sx1, final int sy1,
            final int sx2, final int sy2, final ImageObserver observer) {
        throw new NotImplementedYetException();
    }

    @Override
    public void drawLine(final int x1, final int y1, final int x2, final int y2) {
        final AttributesImpl a = new AttributesImpl();
        aa(a, "x1", Integer.toString(x1));
        aa(a, "y1", Integer.toString(-y1));
        aa(a, "x2", Integer.toString(x2));
        aa(a, "y2", Integer.toString(-y2));
        aa(a, "stroke-width", "1");
        startElement("line", a);
        endElement("line");
    }

    @Override
    public void drawOval(final int x, final int y, final int width,
            final int height) {
        throw new NotImplementedYetException();
    }

    @Override
    public void drawPolygon(final int[] points, final int[] points2,
            final int points3) {
        throw new NotImplementedYetException();
    }

    @Override
    public void drawPolyline(final int[] points, final int[] points2,
            final int points3) {
        throw new NotImplementedYetException();
    }

    @Override
    public void drawRenderableImage(final RenderableImage img,
            final AffineTransform xform) {
        throw new NotImplementedYetException();
    }

    @Override
    public void drawRenderedImage(final RenderedImage img,
            final AffineTransform xform) {
        throw new NotImplementedYetException();
    }

    @Override
    public void drawRoundRect(final int x, final int y, final int width,
            final int height, final int arcWidth, final int arcHeight) {
        throw new NotImplementedYetException();
    }

    @Override
    public void drawString(final AttributedCharacterIterator iterator,
            final float x, final float y) {
        throw new NotImplementedYetException();
    }

    @Override
    public void drawString(final AttributedCharacterIterator iterator,
            final int x, final int y) {
        throw new NotImplementedYetException();
    }

    @Override
    public void drawString(final String s, final float x, final float y) {
        throw new NotImplementedYetException();
    }

    @Override
    public void drawString(final String str, final int x, final int y) {
        throw new NotImplementedYetException();
    }

    private void endElement(final String name) {
        try {
            dst.endElement(null, name, null);
        } catch (final SAXException e) {
            throw new RuntimeException("Unhandled", e);
        }
    }

    @Override
    public void fill(final Shape s) {
        throw new NotImplementedYetException();
    }

    @Override
    public void fillArc(final int x, final int y, final int width,
            final int height, final int startAngle, final int arcAngle) {
        throw new NotImplementedYetException();
    }

    @Override
    public void fillOval(final int x, final int y, final int width,
            final int height) {
        throw new NotImplementedYetException();
    }

    @Override
    public void fillPolygon(final int[] points, final int[] points2,
            final int points3) {
        throw new NotImplementedYetException();
    }

    @Override
    public void fillRect(final int x, final int y, final int width,
            final int height) {
        throw new NotImplementedYetException();
    }

    @Override
    public void fillRoundRect(final int x, final int y, final int width,
            final int height, final int arcWidth, final int arcHeight) {
        throw new NotImplementedYetException();
    }

    @Override
    public Color getBackground() {
        throw new NotImplementedYetException();
    }

    @Override
    public Shape getClip() {
        throw new NotImplementedYetException();
    }

    @Override
    public Rectangle getClipBounds() {
        throw new NotImplementedYetException();
    }

    @Override
    public Color getColor() {
        throw new NotImplementedYetException();
    }

    @Override
    public Composite getComposite() {
        throw new NotImplementedYetException();
    }

    @Override
    public GraphicsConfiguration getDeviceConfiguration() {
        throw new NotImplementedYetException();
    }

    @Override
    public Font getFont() {
        throw new NotImplementedYetException();
    }

    @Override
    public FontMetrics getFontMetrics(final Font f) {
        throw new NotImplementedYetException();
    }

    @Override
    public FontRenderContext getFontRenderContext() {
        throw new NotImplementedYetException();
    }

    @Override
    public Paint getPaint() {
        throw new NotImplementedYetException();
    }

    @Override
    public Object getRenderingHint(final Key hintKey) {
        throw new NotImplementedYetException();
    }

    @Override
    public RenderingHints getRenderingHints() {
        throw new NotImplementedYetException();
    }

    @Override
    public Stroke getStroke() {
        return stroke;
    }

    @Override
    public AffineTransform getTransform() {
        throw new NotImplementedYetException();
    }

    @Override
    public boolean hit(final Rectangle rect, final Shape s,
            final boolean onStroke) {
        throw new NotImplementedYetException();
    }

    @Override
    public void rotate(final double theta) {
        throw new NotImplementedYetException();
    }

    @Override
    public void rotate(final double theta, final double x, final double y) {
        throw new NotImplementedYetException();
    }

    @Override
    public void scale(final double sx, final double sy) {
        throw new NotImplementedYetException();
    }

    @Override
    public void setBackground(final Color color) {
        throw new NotImplementedYetException();
    }

    @Override
    public void setClip(final int x, final int y, final int width,
            final int height) {
        throw new NotImplementedYetException();
    }

    @Override
    public void setClip(final Shape clip) {
        throw new NotImplementedYetException();
    }

    @Override
    public void setColor(final Color c) {
        throw new NotImplementedYetException();
    }

    @Override
    public void setComposite(final Composite comp) {
        throw new NotImplementedYetException();
    }

    @Override
    public void setFont(final Font font) {
        throw new NotImplementedYetException();
    }

    @Override
    public void setPaint(final Paint paint) {
        throw new NotImplementedYetException();
    }

    @Override
    public void setPaintMode() {
        throw new NotImplementedYetException();
    }

    @Override
    public void setRenderingHint(final Key hintKey, final Object hintValue) {
        throw new NotImplementedYetException();
    }

    @Override
    public void setRenderingHints(final Map<?, ?> hints) {
        throw new NotImplementedYetException();
    }

    @Override
    public void setStroke(final Stroke s) {
        stroke = (BasicStroke) s;
        endElement("g");
        AttributesImpl a = new AttributesImpl();
        aa(a, "stroke-width", stroke.getLineWidth());
        startElement("g", a);
    }

    @Override
    public void setTransform(final AffineTransform Tx) {
        throw new NotImplementedYetException();
    }

    @Override
    public void setXORMode(final Color c1) {
        throw new NotImplementedYetException();
    }

    @Override
    public void shear(final double shx, final double shy) {
        throw new NotImplementedYetException();
    }

    private void startElement(final String name, final Attributes a) {
        try {
            dst.startElement(null, name, null, a);
        } catch (final SAXException e) {
            throw new RuntimeException("Unhandled", e);
        }
    }

    @Override
    public void transform(final AffineTransform Tx) {
        throw new NotImplementedYetException();
    }

    @Override
    public void translate(final double tx, final double ty) {
        throw new NotImplementedYetException();
    }

    @Override
    public void translate(final int x, final int y) {
        throw new NotImplementedYetException();
    }
}
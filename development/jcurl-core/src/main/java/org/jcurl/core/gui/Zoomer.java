/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
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
package org.jcurl.core.gui;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


import org.jcurl.core.dto.Ice;
import org.jcurl.core.dto.RockProps;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.swing.JCurlDisplay;

/**
 * Smart handler for creating wc to dc transformations.
 * 
 * @see org.jcurl.core.swing.JCurlDisplay
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:Zoomer.java 330 2006-06-05 14:29:14Z mrohrmoser $
 */
public class Zoomer {

    private static final float _dia = 2 * RockProps.DEFAULT.getRadius();

    public static final Zoomer C12 = new Zoomer("Twelve foot circle",
            -Ice.SIDE_2_CENTER, -Ice.SIDE_2_CENTER, 2 * Ice.SIDE_2_CENTER,
            2 * Ice.SIDE_2_CENTER, 0, 0);

    public static final Zoomer HOG2HACK = new Zoomer("Far hog back line",
            -(Ice.SIDE_2_CENTER + _dia), -(Ice.BACK_2_TEE + Ice.HACK_2_BACK),
            2 * (Ice.SIDE_2_CENTER + _dia), _dia + Ice.FAR_HOG_2_TEE
                    + Ice.BACK_2_TEE + Ice.HACK_2_BACK, 0,
            -(Ice.BACK_2_TEE + Ice.HACK_2_BACK));

    public static final Zoomer HOUSE = new Zoomer("House",
            -(Ice.SIDE_2_CENTER + _dia), -(Ice.BACK_2_TEE + _dia),
            2 * (Ice.SIDE_2_CENTER + _dia), 2 * _dia + Ice.HOG_2_TEE
                    + Ice.BACK_2_TEE, 0, -(_dia + Ice.BACK_2_TEE));

    public static final Zoomer HOUSE2HACK = new Zoomer("House until back line",
            -(Ice.SIDE_2_CENTER + _dia), -(Ice.BACK_2_TEE + Ice.HACK_2_BACK),
            2 * (Ice.SIDE_2_CENTER + _dia), _dia + Ice.HOG_2_TEE
                    + Ice.BACK_2_TEE + Ice.HACK_2_BACK, 0,
            -(Ice.BACK_2_TEE + Ice.HACK_2_BACK));

    private static final boolean uniform = true;

    private static final Rectangle2D create(final Point2D tl, final Point2D br) {
        final double tlx;
        if (tl.getX() < br.getX())
            tlx = tl.getX();
        else
            tlx = br.getX();
        final double tly;
        if (tl.getY() < br.getY())
            tly = tl.getY();
        else
            tly = br.getY();
        final double w = Math.abs(br.getX() - tl.getX());
        final double h = Math.abs(br.getY() - tl.getY());
        return new Rectangle2D.Double(tlx, tly, w, h);
    }

    private final Point2D fixPoint;

    private final Rectangle2D viewport;

    /**
     * @see #Zoomer(String, Rectangle2D, Point2D)
     * @param txt
     * @param x0
     * @param y0
     * @param w
     * @param h
     * @param fixX
     * @param fixY
     */
    public Zoomer(final String txt, double x0, double y0, double w, double h,
            final double fixX, final double fixY) {
        this(txt, new Rectangle2D.Double(x0, y0, w, h), new Point2D.Double(
                fixX, fixY));
    }

    /**
     * @see #Zoomer(String, Rectangle2D, Point2D)
     * @param txt
     * @param x0
     * @param y0
     * @param w
     * @param h
     * @param fixPoint
     */
    public Zoomer(final String txt, double x0, double y0, double w, double h,
            final Point2D fixPoint) {
        this(txt, new Rectangle2D.Double(x0, y0, w, h), fixPoint);
    }

    /**
     * @see #Zoomer(String, Rectangle2D, Point2D)
     * @param txt
     * @param tl
     * @param br
     * @param fixPoint
     */
    public Zoomer(final String txt, final Point2D tl, final Point2D br,
            final Point2D fixPoint) {
        this(txt, create(tl, br), fixPoint);
    }

    /**
     * @see #computeWctoDcTrafo(Rectangle, Orientation, boolean,
     *      AffineTransform)
     * @param txt
     * @param wc
     *            world-coordinate view-port (zoom-area)
     * @param fixPoint
     *            this point's relative position to the wc-viewport is mapped to
     *            the same relative position in the dc-viewport.
     */
    public Zoomer(final String txt, final Rectangle2D wc, final Point2D fixPoint) {
        this.viewport = wc;
        this.fixPoint = fixPoint;
    }

    /**
     * Map the zoomer's wc viewport to the given dc viewport.
     * 
     * @param dc
     * @param orient
     *            direction of the NEGATIVE y-axis. In Other words: where is the
     *            skip looking?
     * @param isLeftHanded
     *            <code>true</code> if the dc coordinate system is left-handed
     * @param mat
     *            Matrix to add the transformation to, usually call yourself a
     *            {@link AffineTransform#setToIdentity()}&nbsp;before.
     * @return the transformation
     */
    public AffineTransform computeWctoDcTrafo(final Rectangle dc,
            final Orientation orient, final boolean isLeftHanded,
            AffineTransform mat) {
        if (mat == null)
            mat = new AffineTransform();
        else
            mat.setToIdentity();
        mat.translate(-dc.getX(), -dc.getY());
        final int SCALE = JCurlDisplay.SCALE;
        double sca_x = dc.getWidth();
        double sca_y = dc.getHeight();
        if (Orientation.N.equals(orient)) {
            sca_x /= viewport.getWidth();
            sca_y /= viewport.getHeight();
            // compute the fixpoint in dc
            final double fpx = dc.getMinX()
                    + (fixPoint.getX() - viewport.getMinX()) * sca_x;
            final double fpy = dc.getMaxY()
                    - (fixPoint.getY() - viewport.getMinY()) * sca_y;
            mat.translate(fpx, fpy);
        } else if (Orientation.W.equals(orient)) {
            sca_x /= viewport.getHeight();
            sca_y /= viewport.getWidth();
            // compute the fixpoint in dc
            final double fpx = dc.getMaxX()
                    - (fixPoint.getY() - viewport.getMinY()) * sca_x;
            final double fpy = dc.getMinY()
                    + (fixPoint.getX() - viewport.getMinX()) * sca_y;
            mat.translate(fpx, fpy);
        } else {
            throw new NotImplementedYetException();
        }

        if (uniform)
            if (sca_x > sca_y)
                sca_x = sca_y;
            else
                sca_y = sca_x;
        mat.scale(sca_x / SCALE, sca_y / SCALE);
        mat.rotate(orient.angle - Math.PI);
        if (isLeftHanded)
            mat.scale(-1, 1);
        mat.translate(-fixPoint.getX() * SCALE, -fixPoint.getY() * SCALE);
        return mat;
    }

    /**
     * Indicator if the wc zoom has changed.
     * 
     * @return the wc viewport (or fixpoint) has changed
     */
    public boolean hasChanged() {
        return false;
    }
}
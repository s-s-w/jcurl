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
package org.jcurl.core.swing;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jcurl.core.base.Ice;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.helpers.NotImplementedYetException;

/**
 * Zoomer with a fixpoint.
 * 
 * @see org.jcurl.core.swing.JCurlDisplay
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:Zoomer.java 330 2006-06-05 14:29:14Z mrohrmoser $
 */
public class ZoomerFixPoint extends ZoomerRect {
    private static final float _dia = 2 * RockProps.DEFAULT.getRadius();

    public static final Zoomer C12 = new ZoomerFixPoint("Twelve foot circle",
            -Ice.SIDE_2_CENTER, -Ice.SIDE_2_CENTER, 2 * Ice.SIDE_2_CENTER,
            2 * Ice.SIDE_2_CENTER, 0, 0);

    public static final Zoomer HOG2HACK = new ZoomerFixPoint(
            "Far hog back line", -(Ice.SIDE_2_CENTER + _dia),
            -(Ice.BACK_2_TEE + Ice.HACK_2_BACK),
            2 * (Ice.SIDE_2_CENTER + _dia), _dia + Ice.FAR_HOG_2_TEE
                    + Ice.BACK_2_TEE + Ice.HACK_2_BACK, 0,
            -(Ice.BACK_2_TEE + Ice.HACK_2_BACK));

    public static final Zoomer HOUSE = new ZoomerFixPoint("House",
            -(Ice.SIDE_2_CENTER + _dia), -(Ice.BACK_2_TEE + _dia),
            2 * (Ice.SIDE_2_CENTER + _dia), 2 * _dia + Ice.HOG_2_TEE
                    + Ice.BACK_2_TEE, 0, -(_dia + Ice.BACK_2_TEE));

    public static final Zoomer HOUSE2HACK = new ZoomerFixPoint(
            "House until back line", -(Ice.SIDE_2_CENTER + _dia),
            -(Ice.BACK_2_TEE + Ice.HACK_2_BACK),
            2 * (Ice.SIDE_2_CENTER + _dia), _dia + Ice.HOG_2_TEE
                    + Ice.BACK_2_TEE + Ice.HACK_2_BACK, 0,
            -(Ice.BACK_2_TEE + Ice.HACK_2_BACK));

    private static final long serialVersionUID = -8692952713937311284L;

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

    private Rectangle2D vpDc = new Rectangle(0, 0, 0, 0);

    private final Rectangle2D vpWc;

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
    public ZoomerFixPoint(final String txt, double x0, double y0, double w,
            double h, final double fixX, final double fixY) {
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
    public ZoomerFixPoint(final String txt, double x0, double y0, double w,
            double h, final Point2D fixPoint) {
        this(txt, new Rectangle2D.Double(x0, y0, w, h), fixPoint);
    }

    /**
     * @see #Zoomer(String, Rectangle2D, Point2D)
     * @param txt
     * @param tl
     * @param br
     * @param fixPoint
     */
    public ZoomerFixPoint(final String txt, final Point2D tl, final Point2D br,
            final Point2D fixPoint) {
        this(txt, create(tl, br), fixPoint);
    }

    /**
     * @see #computeWc2Dc(Rectangle, Orientation, boolean, AffineTransform)
     * @param txt
     * @param wc
     *            world-coordinate view-port (zoom-area)
     * @param fixPoint
     *            this point's relative position to the wc-viewport is mapped to
     *            the same relative position in the dc-viewport.
     */
    public ZoomerFixPoint(final String txt, final Rectangle2D wc,
            final Point2D fixPoint) {
        this.vpWc = wc;
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
    AffineTransform computeWc2Dc(final Rectangle2D dc,
            final Orientation orient, final boolean uniform,
            final boolean isLeftHanded, AffineTransform mat) {
        if (mat == null)
            mat = getWc2Dc();
        mat.setToIdentity();
        mat.translate(-dc.getX(), -dc.getY());
        final int SCALE = JCurlDisplay.SCALE;
        double sca_x = dc.getWidth();
        double sca_y = dc.getHeight();
        if (Orientation.N.equals(orient)) {
            sca_x /= vpWc.getWidth();
            sca_y /= vpWc.getHeight();
            // compute the fixpoint in dc
            final double fpx = dc.getMinX()
                    + (fixPoint.getX() - vpWc.getMinX()) * sca_x;
            final double fpy = dc.getMaxY()
                    - (fixPoint.getY() - vpWc.getMinY()) * sca_y;
            mat.translate(fpx, fpy);
        } else if (Orientation.W.equals(orient)) {
            sca_x /= vpWc.getHeight();
            sca_y /= vpWc.getWidth();
            // compute the fixpoint in dc
            final double fpx = dc.getMaxX()
                    - (fixPoint.getY() - vpWc.getMinY()) * sca_x;
            final double fpy = dc.getMinY()
                    + (fixPoint.getX() - vpWc.getMinX()) * sca_y;
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

    public void setDc(Rectangle2D r) {
        if (vpDc.equals(r))
            return;
        vpDc = r;
        setWc2Dc(computeWc2Dc(r, Orientation.W, true, true, this.getWc2Dc()));
    }
}
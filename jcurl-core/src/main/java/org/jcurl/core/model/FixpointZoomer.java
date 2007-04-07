/*
 * jcurl curling simulation framework http://www.jcurl.org
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
package org.jcurl.core.model;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.Orientation;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.base.Zoomer;
import org.jcurl.core.helpers.NotImplementedYetException;

/**
 * Smart handler for creating wc to dc transformations.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id:CenteredZoomer.java 378 2007-01-24 01:18:35Z mrohrmoser $
 */
public class FixpointZoomer extends Zoomer {

    private static final float _dia = 2 * RockProps.DEFAULT.getRadius();

    public static final Zoomer C12 = new FixpointZoomer("Twelve foot circle",
            -IceSize.SIDE_2_CENTER, -IceSize.SIDE_2_CENTER, 2 * IceSize.SIDE_2_CENTER,
            2 * IceSize.SIDE_2_CENTER, 0, 0);

    public static final Zoomer HOG2HACK = new FixpointZoomer(
            "Far hog back line", -(IceSize.SIDE_2_CENTER + _dia),
            -(IceSize.BACK_2_TEE + IceSize.HACK_2_BACK),
            2 * (IceSize.SIDE_2_CENTER + _dia), _dia + IceSize.FAR_HOG_2_TEE
                    + IceSize.BACK_2_TEE + IceSize.HACK_2_BACK, 0,
            -(IceSize.BACK_2_TEE + IceSize.HACK_2_BACK));

    public static final Zoomer HOUSE = new FixpointZoomer("House",
            -(IceSize.SIDE_2_CENTER + _dia), -(IceSize.BACK_2_TEE + _dia),
            2 * (IceSize.SIDE_2_CENTER + _dia), 2 * _dia + IceSize.HOG_2_TEE
                    + IceSize.BACK_2_TEE, 0, -(_dia + IceSize.BACK_2_TEE));

    public static final Zoomer HOUSE2HACK = new FixpointZoomer(
            "House until back line", -(IceSize.SIDE_2_CENTER + _dia),
            -(IceSize.BACK_2_TEE + IceSize.HACK_2_BACK),
            2 * (IceSize.SIDE_2_CENTER + _dia), _dia + IceSize.HOG_2_TEE
                    + IceSize.BACK_2_TEE + IceSize.HACK_2_BACK, 0,
            -(IceSize.BACK_2_TEE + IceSize.HACK_2_BACK));

    private static final long serialVersionUID = -3935666633509837731L;

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
     * @see #FixpointZoomer(String, Rectangle2D, Point2D)
     * @param txt
     * @param x0
     * @param y0
     * @param w
     * @param h
     * @param fixX
     * @param fixY
     */
    public FixpointZoomer(final String txt, final double x0, final double y0,
            final double w, final double h, final double fixX, final double fixY) {
        this(txt, new Rectangle2D.Double(x0, y0, w, h), new Point2D.Double(
                fixX, fixY));
    }

    /**
     * @see #FixpointZoomer(String, Rectangle2D, Point2D)
     * @param txt
     * @param x0
     * @param y0
     * @param w
     * @param h
     * @param fixPoint
     */
    public FixpointZoomer(final String txt, final double x0, final double y0,
            final double w, final double h, final Point2D fixPoint) {
        this(txt, new Rectangle2D.Double(x0, y0, w, h), fixPoint);
    }

    /**
     * @see #FixpointZoomer(String, Rectangle2D, Point2D)
     * @param txt
     * @param tl
     * @param br
     * @param fixPoint
     */
    public FixpointZoomer(final String txt, final Point2D tl, final Point2D br,
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
    public FixpointZoomer(final String txt, final Rectangle2D wc,
            final Point2D fixPoint) {
        viewport = (Rectangle2D) wc.clone();
        this.fixPoint = (Point2D) fixPoint.clone();
    }

    @Override
    public AffineTransform computeWctoDcTrafo(final Rectangle dc,
            final AffineTransform mat) {
        return computeWctoDcTrafo(dc, Orientation.W, true, mat);
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
    AffineTransform computeWctoDcTrafo(final Rectangle dc,
            final Orientation orient, final boolean isLeftHanded,
            AffineTransform mat) {
        if (mat == null)
            mat = new AffineTransform();
        else
            mat.setToIdentity();
        mat.translate(-dc.getX(), -dc.getY());
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
        } else
            throw new NotImplementedYetException();

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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final FixpointZoomer other = (FixpointZoomer) obj;
        if (fixPoint == null) {
            if (other.fixPoint != null)
                return false;
        } else if (!fixPoint.equals(other.fixPoint))
            return false;
        if (viewport == null) {
            if (other.viewport != null)
                return false;
        } else if (!viewport.equals(other.viewport))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (fixPoint == null ? 0 : fixPoint.hashCode());
        result = PRIME * result + (viewport == null ? 0 : viewport.hashCode());
        return result;
    }
}
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
package jcurl.core.gui;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import jcurl.core.NotImplementedYetException;
import jcurl.core.dto.Ice;
import jcurl.core.dto.RockProps;

/**
 * @see jcurl.core.gui.ZoomerTest
 * @see jcurl.core.gui.JCurlPanel
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class Zoomer {

    private static final float _dia = 2 * RockProps.DEFAULT.getRadius();

    public static final Zoomer HOUSE = new Zoomer("House",
            -(Ice.SIDE_2_CENTER + _dia), -(Ice.BACK_2_TEE + Ice.HACK_2_BACK),
            2 * (Ice.SIDE_2_CENTER + _dia), Ice.HOG_2_TEE + _dia
                    + Ice.BACK_2_TEE + Ice.HACK_2_BACK, 0,
            -(Ice.BACK_2_TEE + Ice.HACK_2_BACK));

    public static final Zoomer C12 = new Zoomer("Twelve foot circle",
            -Ice.SIDE_2_CENTER, -Ice.SIDE_2_CENTER, 2 * Ice.SIDE_2_CENTER,
            2 * Ice.SIDE_2_CENTER, 0 * Ice.SIDE_2_CENTER, -0.5
                    * Ice.SIDE_2_CENTER);

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

    public Zoomer(final String txt, double x0, double y0, double w, double h,
            final double fixX, final double fixY) {
        this(txt, new Rectangle2D.Double(x0, y0, w, h), new Point2D.Double(
                fixX, fixY));
    }

    public Zoomer(final String txt, double x0, double y0, double w, double h,
            final Point2D fixPoint) {
        this(txt, new Rectangle2D.Double(x0, y0, w, h), fixPoint);
    }

    public Zoomer(final String txt, final Point2D tl, final Point2D br,
            final Point2D fixPoint) {
        this(txt, create(tl, br), fixPoint);
    }

    public Zoomer(final String txt, final Rectangle2D wc,
            final Point2D fixPoint) {
        this.viewport = wc;
        this.fixPoint = fixPoint;
    }

    public AffineTransform applyTrafo(final Rectangle dc,
            final Orientation orient, final boolean isLeftHanded,
            final AffineTransform mat) {
        final int SCALE = JCurlPanel.SCALE;
        if (Orientation.N.equals(orient)) {
            double sca_x = dc.getWidth() / viewport.getWidth();
            double sca_y = dc.getHeight() / viewport.getHeight();
            // compute the fixpoint in dc
            double fpx = dc.getMinX() + (fixPoint.getX() - viewport.getMinX())
                    * sca_x;
            final double dy = dc.getMinY();
            final double fy = fixPoint.getY();
            final double vy = viewport.getMinY();
            double fpy = dc.getMaxY() - (fixPoint.getY() - viewport.getMinY())
                    * sca_y;
            
            mat.translate(fpx, fpy);
            if (uniform)
                if (sca_x > sca_y)
                    sca_x = sca_y;
                else
                    sca_y = sca_x;
            mat.scale(sca_x / SCALE, sca_y / SCALE);
            mat.rotate(Math.PI);
            if (isLeftHanded)
                mat.scale(-1, 1);
            mat.translate(-fixPoint.getX() * SCALE, -fixPoint.getY() * SCALE);
            return mat;
        }
        if (Orientation.W.equals(orient)) {
            double sca_x = dc.getWidth() / viewport.getHeight();
            double sca_y = dc.getHeight() / viewport.getWidth();
            // compute the fixpoint in dc
            double fpx = dc.getMaxX() - (fixPoint.getY() - viewport.getMinY())
                    * sca_x;
            final double dy = dc.getMinY();
            final double fy = fixPoint.getY();
            final double vy = viewport.getMinY();
            double fpy = dc.getMinY() + (fixPoint.getX() - viewport.getMinX())
                    * sca_y;
            
            mat.translate(fpx, fpy);
            if (uniform)
                if (sca_x > sca_y)
                    sca_x = sca_y;
                else
                    sca_y = sca_x;
            mat.scale(sca_x / SCALE, sca_y / SCALE);
            mat.rotate(Math.PI / 2);
            if (isLeftHanded)
                mat.scale(-1, 1);
            mat.translate(-fixPoint.getX() * SCALE, -fixPoint.getY() * SCALE);
            return mat;
        }
        throw new NotImplementedYetException();
    }

    public boolean hasChanged() {
        return false;
    }
}
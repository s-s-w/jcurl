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

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

import jcurl.core.RockSet;
import jcurl.core.TargetDiscrete;

/**
 * "Stupid" display.
 * 
 * @see jcurl.core.gui.Pt
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class JCurlPanel extends JPanel implements TargetDiscrete {

    /**
     *  
     */
    private static void computeTrafo(final int wid, final int hei,
            final ZoomArea zoom, final boolean uniform,
            final AffineTransform mat) {
        float x_sca = wid / (zoom.tl.y - zoom.br.y);
        float y_sca = hei / (-zoom.tl.x + zoom.br.x);
        if (uniform) {
            if (y_sca > x_sca)
                y_sca = x_sca;
            else
                x_sca = y_sca;
        }
        // then build the transformation wc -> dc matrix
        mat.setToIdentity();
        // add the transformations in reverse order!
        mat.translate(wid / 2, hei / 2);
        mat.scale(x_sca, -y_sca);
        mat.rotate(Math.PI / 2);
        //mat.translate(0, (Pt.nhl.wc.y + Pt.br.wc.y) / 2);
        mat
                .translate((zoom.tl.x + zoom.br.x) / 2,
                        -(zoom.tl.y + zoom.br.y) / 2);
        // finally transform the "default" points
        Pt.transform(mat);
    }

    private final IcePainter iceP = new IcePainter();

    private final AffineTransform mat = new AffineTransform();

    private int oldHei = -1;

    private int oldWid = -1;

    private final RockPainter rockP = new RockPainter();

    private RockSet rocks;

    private ZoomArea zom;

    public JCurlPanel(final RockSet rocks, ZoomArea zoom) {
        this.rocks = rocks == null ? RockSet.allHome() : rocks;
        this.zom = zoom == null ? ZoomArea.HOUSE : zoom;
    }

    public JCurlPanel(ZoomArea zoom) {
        this(RockSet.allHome(), zoom);
    }

    public Point2D dc2wc(final Point2D dc, final Point2D wc) {
        try {
            return mat.inverseTransform(dc, wc);
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException("Why uninvertible?", e);
        }
    }

    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final int w = this.getWidth();
        final int h = this.getHeight();
        if (oldWid != w || oldHei != h) {
            // first get the zoomfactor
            computeTrafo(w, h, zom, true, mat);
            oldWid = w;
            oldHei = h;
        }
        iceP.paintIce(g);
        rockP.paintRocks(g, mat, rocks);
    }

    /**
     * Triggers a repaint.
     * 
     * @param rocks
     */
    public void setPos(final long time, final RockSet rocks) {
        setPos(time, rocks, 0);
    }

    /**
     * Triggers a repaint.
     * 
     * @param rocks
     */
    public void setPos(final long time, final RockSet rocks,
            final int discontinuous) {
        this.rocks = rocks;
        this.repaint();
    }

    public Point2D wc2dc(final Point2D wc, final Point2D dc) {
        return mat.transform(wc, dc);
    }
}
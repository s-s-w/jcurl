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

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import jcurl.core.dto.Ice;
import jcurl.core.io.Dim;

/**
 * Frequently use points around the house.
 * 
 * @see jcurl.core.gui.JCurlPanel
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
class Pt {

    private static final Point2D.Float [] _dcs = new Point2D.Float[20];

    private static int _idx;

    private static final Point2D.Float [] _wcs = new Point2D.Float[_dcs.length];

    public static final Pt bc = new Pt(0, -Ice.BACK_2_TEE);

    public static final Pt bl = new Pt(-Ice.SIDE_2_CENTER, -Ice.BACK_2_TEE);

    public static final Pt br = new Pt(Ice.SIDE_2_CENTER, -Ice.BACK_2_TEE);

    public static final Pt fHackC = new Pt(0, Ice.FAR_HACK_2_TEE);

    public static final Pt fhc = new Pt(0, Ice.FAR_HOG_2_TEE);

    public static final Pt fhl = new Pt(-Ice.SIDE_2_CENTER, Ice.FAR_HOG_2_TEE);

    public static final Pt fhr = new Pt(Ice.SIDE_2_CENTER, Ice.FAR_HOG_2_TEE);

    public static final Pt nHackC = new Pt(0,
            -(Ice.BACK_2_TEE + Ice.HACK_2_BACK));

    public static final Pt nhc = new Pt(0, Ice.HOG_2_TEE);

    public static final Pt nhl = new Pt(-Ice.SIDE_2_CENTER, Ice.HOG_2_TEE);

    public static final Pt nhr = new Pt(Ice.SIDE_2_CENTER, Ice.HOG_2_TEE);

    public static final Pt tl = new Pt(-Ice.SIDE_2_CENTER, 0);

    public static final Pt tr = new Pt(Ice.SIDE_2_CENTER, 0);

    public static final Pt tr1 = new Pt(Dim.f2m(0.5), Dim.f2m(0.5));

    public static final Pt tr12 = new Pt(Dim.f2m(6), Dim.f2m(6));

    public static final Pt tr4 = new Pt(Dim.f2m(2), Dim.f2m(2));

    public static final Pt tr8 = new Pt(Dim.f2m(4), Dim.f2m(4));

    public static final Pt zero = new Pt(0, 0);

    public static void transform(final AffineTransform mat) {
        for (int i = _idx - 1; i >= 0; i--) {
            final Point2D pwc = _wcs[i];
            if (pwc == null)
                continue;
            mat.transform(pwc, _dcs[i]);
        }
    }

    public final Point2D.Float dc;

    public final Point2D.Float wc;

    private Pt(float x, float y) {
        _wcs[_idx] = wc = new Point2D.Float(x, y);
        _dcs[_idx] = dc = new Point2D.Float(x, y);
        _idx++;
    }
}
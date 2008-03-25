/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
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
package org.jcurl.zui.piccolo;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.Unit;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;

/**
 * Simple keyboard zoom to sheet, house and 12-foot.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id:KeyboardZoom.java 795 2008-03-19 13:40:42Z mrohrmoser $
 */
public class KeyboardZoom extends PBasicInputEventHandler {
    private static final int _500 = 500;

    /** All from back to back */
    public static final Rectangle2D CompletePlus;

    /** House area plus 1 rock margin plus "out" rock space. */
    public static final Rectangle2D HousePlus;
    /**
     * Inter-hog area area plus house area plus 1 rock margin plus "out" rock
     * space.
     */
    private static final Rectangle2D SheetPlus;

    /** 12-foot circle plus 1 rock */
    private static final Rectangle2D TwelvePlus;

    static {
        final double r2 = 2 * RockProps.DEFAULT.getRadius();
        final double x = IceSize.SIDE_2_CENTER + r2;
        HousePlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE + r2),
                2 * x, IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
        final double c12 = r2 + Unit.f2m(6.0);
        TwelvePlus = new Rectangle2D.Double(-c12, -c12, 2 * c12, 2 * c12);
        SheetPlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_HOG
                + IceSize.HOG_2_TEE + r2), 2 * x, IceSize.HOG_2_HOG
                + IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
        CompletePlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE
                + IceSize.HOG_2_HOG + IceSize.HACK_2_HOG + r2), 2 * x,
                IceSize.HOG_2_HOG + 2 * IceSize.HACK_2_HOG);
    }
    private final PCamera cam;
    public final Action Zoom12Foot;
    public final Action ZoomComplete;
    public final Action ZoomHouse;
    public final Action ZoomSheet;

    public KeyboardZoom(final PCamera cam) {
        this.cam = cam;

        ZoomComplete = new AbstractAction() {
            private static final long serialVersionUID = -4680813072205075958L;

            public void actionPerformed(final ActionEvent e) {
                zoom(CompletePlus);
            }
        };
        ZoomSheet = new AbstractAction() {
            private static final long serialVersionUID = -4680813072205075958L;

            public void actionPerformed(final ActionEvent e) {
                zoom(SheetPlus);
            }
        };
        ZoomHouse = new AbstractAction() {
            private static final long serialVersionUID = -4680813072205075958L;

            public void actionPerformed(final ActionEvent e) {
                zoom(HousePlus);
            }
        };
        Zoom12Foot = new AbstractAction() {
            private static final long serialVersionUID = -4680813072205075958L;

            public void actionPerformed(final ActionEvent e) {
                zoom(TwelvePlus);
            }
        };
    }

    @Override
    public void keyPressed(final PInputEvent event) {
        switch (event.getKeyCode()) {
        case KeyEvent.VK_HOME:
            event.setHandled(true);
            if (event.isControlDown())
                ZoomComplete.actionPerformed(null);
            else
                ZoomHouse.actionPerformed(null);
            break;
        case KeyEvent.VK_END:
            event.setHandled(true);
            if (event.isControlDown())
                ZoomSheet.actionPerformed(null);
            else
                Zoom12Foot.actionPerformed(null);
            break;
        default:
            ;
        }
    }

    private void zoom(final Rectangle2D a) {
        cam.animateViewToCenterBounds(a, true, 333);
    }
}
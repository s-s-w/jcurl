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
package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import org.jcurl.core.base.ComputedTrajectorySet;
import org.jcurl.core.base.CurveStore;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.helpers.Unit;
import org.jcurl.core.model.CurveManager;

/**
 * Graphical Display for the tactics Planner.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class TacticsPanel extends JPanel {

    public static class Model extends UndoRedoDocumentBase implements ZuiMod {

        private final CurveManager model;

        public Model(final CurveManager cm) {
            model = cm;
        }

        public PositionSet getCurrentPos() {
            return model.getCurrentPos();
        }

        public CurveStore getCurveStore() {
            return model.getCurveStore();
        }

        public PositionSet getInitialPos() {
            return model.getInitialPos();
        }

        public ComputedTrajectorySet getModel() {
            return model;
        }
    }

    /** All from back to back */
    static final Rectangle2D completeP;

    /** House area plus 1 rock margin plus "out" rock space. */
    static final Rectangle2D houseP;
    private static final long serialVersionUID = 2301991451388254183L;

    /**
     * Inter-hog area area plus house area plus 1 rock margin plus "out" rock
     * space.
     */
    static final Rectangle2D sheetP;

    /** 12-foot circle plus 1 rock */
    static final Rectangle2D twelveP;
    static {
        final double r2 = 2 * RockProps.DEFAULT.getRadius();
        final double x = IceSize.SIDE_2_CENTER + r2;
        houseP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE + r2), 2 * x,
                IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
        final double c12 = r2 + Unit.f2m(6.0);
        twelveP = new Rectangle2D.Double(-c12, -c12, 2 * c12, 2 * c12);
        sheetP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_HOG
                + IceSize.HOG_2_TEE + r2), 2 * x, IceSize.HOG_2_HOG
                + IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
        completeP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE
                + IceSize.HOG_2_HOG + IceSize.HACK_2_HOG + r2), 2 * x,
                IceSize.HOG_2_HOG + 2 * IceSize.HACK_2_HOG);
    }
    private final Model model;
    private final ZuiPanel zui;

    public TacticsPanel(final CurveManager m) {
        model = new Model(m);
        setLayout(new BorderLayout());
        this.add(zui = new ZuiPanel(model), BorderLayout.CENTER);
    }

    public Model getModel() {
        return model;
    }

    public void zoom(final Rectangle2D r, final int millis) {
        zui.zoom(r, millis);
    }
}

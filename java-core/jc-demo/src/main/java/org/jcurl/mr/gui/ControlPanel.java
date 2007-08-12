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
package org.jcurl.mr.gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.jcurl.core.helpers.Unit;

/**
 * {@link JSlider} plus {@link JTextField} for Broom direction, Interval Time,
 * Draw-To-Tee Time and -Curl.
 * 
 * @see DimValSliderPanel
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class ControlPanel extends JTabbedPane {

    private static final long serialVersionUID = -7647827327554178710L;

    private final Model model;

    public ControlPanel() {
        this(null);
    }

    public ControlPanel(final Model m) {
        setVisible(false);
        model = m == null ? new Model() : m;
        DimValSliderPanel p = null;
        final JPanel p0 = new JPanel();
        // FIXME leads to Rock-Numbering Shift!!!
        p0.setLayout(new GridLayout(0, 2));
        p0.add(p = new DimValSliderPanel(model, "Broom", "broomX", Unit.FOOT));
        p0.add(p = new DimValSliderPanel(model, "Interval", "interval",
                Unit.SECOND));
        p.setMinimum(1000);
        p.setMaximum(4000);
        this.addTab("Rock", p0);

        final JPanel p1 = new JPanel();
        p1.setLayout(p0.getLayout());
        p1.add(p = new DimValSliderPanel(model, "Curl", "drawCurl", Unit.FOOT));
        p1
                .add(p = new DimValSliderPanel(model, "Time", "drawTime",
                        Unit.SECOND));
        p.setMinimum(15000);
        p.setMaximum(30000);
        this.addTab("Ice", p1);

        setSize(180, 250);
        setMinimumSize(new Dimension(180, 250));
        setVisible(true);
    }
}

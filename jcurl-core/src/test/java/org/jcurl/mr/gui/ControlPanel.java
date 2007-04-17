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

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.jcurl.core.helpers.Dim;

public class ControlPanel extends JTabbedPane {

    private static final long serialVersionUID = -7647827327554178710L;

    private final Model model;

    public ControlPanel() {
        this(null);
    }

    public ControlPanel(final Model m) {
        setVisible(false);
        model = m == null ? new Model() : m;
        final JPanel p00 = new DimValSliderPanel(model, "Broom", "broomX", Dim.FOOT);
        // final JPanel p01 = new SliderPanel(model, "Interval", "interval",
        // Dim.SECOND);
        final JPanel p0 = new JPanel();
        p0.setLayout(new FlowLayout());
        p0.add(p00);
        // p0.add(p01);
        this.addTab("Rock", p0);

        final JPanel p10 = new DimValSliderPanel(model, "Curl", "drawCurl", Dim.FOOT);
        // final JPanel p11 = new SliderPanel(model, "Time", "drawTime",
        // Dim.SECOND);
        final JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        p1.add(p10);
        // p1.add(p11);
        this.addTab("Ice", p1);

        setVisible(true);
    }
}

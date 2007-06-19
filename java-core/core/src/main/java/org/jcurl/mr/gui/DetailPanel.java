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

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.helpers.Dim;
import org.jcurl.core.helpers.DimVal;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.model.CurveManager;
import org.jcurl.core.swing.PositionDisplay;
import org.jcurl.core.swing.SumDisplayBase;
import org.jcurl.core.swing.SumShotDisplay;
import org.jcurl.core.swing.SumWaitDisplay;

/**
 * Aggregate {@link PositionDisplay} and {@link ControlPanel}.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class DetailPanel extends JPanel implements ChangeListener {

    private static final Log log = JCLoggerFactory.getLogger(DetailPanel.class);

    private static final long serialVersionUID = 6957715169049221416L;

    final int Granularity = 1000;

    final PositionDisplay ice = new PositionDisplay();

    private final Model model;

    final JSlider time = new JSlider();

    public DetailPanel() {
        this(null);
    }

    public DetailPanel(final Model m) {
        setVisible(false);
        model = m == null ? new Model() : m;
        if (model.getTrajectory() == null)
            model.setTrajectory(new CurveManager());

        ice.setPos(model.getTrajectory().getCurrentPos());
        final ControlPanel control = new ControlPanel(model);

        time.setMinimum(0);
        time.setMaximum(30000);
        time.setValue(0);
        time.addChangeListener(this);

        final SumDisplayBase wait = new SumWaitDisplay(ice.getPos());
        final SumDisplayBase shot = new SumShotDisplay(ice.getPos());

        setLayout(new BorderLayout());
        final JPanel b0 = new JPanel(new BorderLayout());
        b0.add(wait, BorderLayout.WEST);
        b0.add(ice, BorderLayout.CENTER);
        b0.add(shot, BorderLayout.EAST);
        final Box b = Box.createVerticalBox();
        b.add(time);
        b.add(b0);
        add(b, BorderLayout.CENTER);
        add(control, BorderLayout.EAST);

        setSize(600, 300);
        model.setBroomX(new DimVal(0, Dim.METER));
        model.setInterval(new DimVal(2.5, Dim.SECOND));
        // model.setDrawCurl(new DimVal(3, Dim.FOOT));
        model.setDrawTime(new DimVal(25, Dim.SECOND));
        setVisible(true);
    }

    public void exportPng(final File dst, final String watermark)
            throws IOException {
        ice.exportPng(dst, watermark);
    }

    public void exportPng(final OutputStream dst, final String watermark)
            throws IOException {
        ice.exportPng(dst, watermark);
    }

    public void stateChanged(final ChangeEvent arg0) {
        if (arg0.getSource() == time) {
            model.getTrajectory().setCurrentTime(
                    1.0 * time.getValue() / Granularity);
            log.debug(model.getTrajectory().getCurrentTime());
            return;
        }
        log.debug(arg0.getSource());
    }
}

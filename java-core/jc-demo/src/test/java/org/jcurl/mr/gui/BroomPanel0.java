/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.Measure;
import org.jcurl.core.api.Unit;
import org.jcurl.core.log.JCLoggerFactory;

public class BroomPanel0 extends JPanel implements ChangeListener,
		ActionListener, PropertyChangeListener {

	private static final Log log = JCLoggerFactory.getLogger(BroomPanel0.class);

	private static final long serialVersionUID = 9008976409239381440L;

	private final Unit dim = Unit.FOOT;

	private final int Granularity = 1000;

	private final Model model;

	private final JSlider slider;

	private final JTextField text;

	public BroomPanel0() {
		this(null);
	}

	public BroomPanel0(final Model m) {
		model = m == null ? new Model() : m;
		model.addPropertyChangeListener(this);
		setVisible(false);
		setLayout(new BorderLayout());

		final JLabel label;
		this.add(label = new JLabel(), "North");
		label.setText("Broom");
		label.setHorizontalAlignment(SwingConstants.CENTER);

		this.add(slider = new JSlider(), "Center");
		slider.setOrientation(SwingConstants.VERTICAL);

		final int max = (int) (new Measure(IceSize.SIDE_2_CENTER, Unit.METER)
				.to(dim).value * Granularity);
		slider.setMaximum((int) (Granularity * Math.ceil((double) max
				/ Granularity)));
		// slider.setMaximum(2500);
		slider.setMinimum(-slider.getMaximum());
		slider.setMajorTickSpacing(Granularity);
		slider.setMinorTickSpacing(Granularity / 10);
		slider.setAlignmentX(10);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setPaintTrack(true);
		slider.addChangeListener(this);

		this.add(text = new JTextField(), "South");
		text.setHorizontalAlignment(SwingConstants.CENTER);
		text.setEditable(true);
		text.addActionListener(this);
		text.selectAll();

		this.setSize(50, 100);
		model.setBroomX(new Measure(0, dim));
		setVisible(true);
	}

	public void actionPerformed(final ActionEvent arg0) {
		if (arg0.getSource() == text)
			try {
				model.setBroomX(Measure.parse(text.getText()));
			} catch (final NumberFormatException e) {
				;// model. setBroomX(getBroomX());
			}
	}

	public void propertyChange(final PropertyChangeEvent arg0) {
		log.debug(arg0);
		if (model == arg0.getSource())
			if ("broomX".equals(arg0.getPropertyName())) {
				final Measure raw = (Measure) arg0.getNewValue();
				final Measure val;
				if (raw.unit == Unit.NONE)
					val = new Measure(raw.value, dim);
				else
					val = raw.to(dim);
				slider.setValue((int) (val.value * Granularity));
				text.setText(val.toString());
			}
	}

	public void stateChanged(final ChangeEvent arg0) {
		if (arg0.getSource() == slider)
			model.setBroomX(new Measure((double) slider.getValue()
					/ Granularity, dim));
	}
}

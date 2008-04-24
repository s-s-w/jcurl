/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
 * 2005-2008 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.jcurl.demo.tactics;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;

import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.ui.BroomPromptModel;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class TrajectorySwing extends JComponent implements TrajectoryDisplay {
	private static final long serialVersionUID = -3512129363499720146L;

	public TrajectorySwing() {
		setLayout(new BorderLayout());

		final Box b = Box.createVerticalBox();
		{
			final JPanel tb = new JPanel();
			tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
			tb.setBorder(BorderFactory.createTitledBorder("Rock"));
			tb.add(new JComboBox(new Object[] { "1", "2", "3", "4", "5", "6",
					"7", "8" }));
			final JRadioButton dark = new JRadioButton("dark");
			final JRadioButton light = new JRadioButton("light");
			final ButtonGroup bg = new ButtonGroup();
			bg.add(dark);
			bg.add(light);
			tb.add(dark);
			tb.add(light);
			b.add(tb);
		}
		{
			final JPanel tb = new JPanel();
			tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
			tb.setBorder(BorderFactory.createTitledBorder("Handle"));
			final JRadioButton dark = new JRadioButton("in");
			final JRadioButton light = new JRadioButton("out");
			final ButtonGroup bg = new ButtonGroup();
			bg.add(dark);
			bg.add(light);
			tb.add(dark);
			tb.add(light);
			tb.add(Box.createHorizontalGlue());
			b.add(tb);
		}
		{
			final JPanel tb = new JPanel();
			tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
			tb.setBorder(BorderFactory.createTitledBorder("Split Time"));
			tb.add(new JSpinner());
			tb.add(new JComboBox(new Object[] { "sec", "1/10 sec", "1/100 sec",
					"1/1000 sec" }));
			tb.add(Box.createHorizontalGlue());
			b.add(tb);
		}
		{
			final JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
			p.setBorder(BorderFactory.createTitledBorder("Broom Position"));
			{
				final Box tb = Box.createHorizontalBox();
				tb.add(new JLabel("x: "));
				tb.add(new JSpinner());
				tb.add(new JComboBox(
						new Object[] { "m", "cm", "mm", "ft", "in" }));
				p.add(tb);
			}
			{
				final Box tb = Box.createHorizontalBox();
				tb.add(new JLabel("y: "));
				tb.add(new JSpinner());
				tb.add(new JComboBox(
						new Object[] { "m", "cm", "mm", "ft", "in" }));
				p.add(tb);
			}
			b.add(p);
		}
		this.add(b);
	}

	public BroomPromptModel getBroom() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	public CurveManager getCurves() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	public void setBroom(final BroomPromptModel b) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet.");
	}

	public void setCurves(final CurveManager model) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet.");
	}
}

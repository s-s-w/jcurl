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

	private final JComponent rock, dark, light, in, out, split, dt, x, y, dx,
			dy;

	public TrajectorySwing() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		final Box b = Box.createVerticalBox();
		{
			final JPanel tb = new JPanel();
			tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
			tb.setBorder(BorderFactory.createTitledBorder("Rock"));
			tb.add(rock = new JComboBox(new Object[] { "1", "2", "3", "4", "5",
					"6", "7", "8" }));
			final JRadioButton dark = new JRadioButton("dark");
			final JRadioButton light = new JRadioButton("light");
			final ButtonGroup bg = new ButtonGroup();
			bg.add(dark);
			bg.add(light);
			tb.add(dark);
			tb.add(light);
			b.add(tb);
			this.dark = dark;
			this.light = light;
			this.rock.setEnabled(false);
			this.dark.setEnabled(false);
			this.light.setEnabled(false);
		}
		{
			final JPanel tb = new JPanel();
			tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
			tb.setBorder(BorderFactory.createTitledBorder("Handle"));
			final JRadioButton in = new JRadioButton("in");
			final JRadioButton out = new JRadioButton("out");
			final ButtonGroup bg = new ButtonGroup();
			bg.add(in);
			bg.add(out);
			tb.add(in);
			tb.add(out);
			tb.add(Box.createHorizontalGlue());
			b.add(tb);
			this.in = in;
			this.out = out;
			this.in.setEnabled(false);
			this.out.setEnabled(false);
		}
		{
			final JPanel tb = new JPanel();
			tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
			tb.setBorder(BorderFactory.createTitledBorder("Split Time"));
			tb.add(split = new JSpinner());
			tb.add(dt = new JComboBox(new Object[] { "sec", "1/10 sec",
					"1/100 sec", "1/1000 sec" }));
			tb.add(Box.createHorizontalGlue());
			b.add(tb);
			this.split.setEnabled(false);
			this.dt.setEnabled(false);
		}
		{
			final JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
			p.setBorder(BorderFactory.createTitledBorder("Broom Position"));
			{
				final Box tb = Box.createHorizontalBox();
				tb.add(new JLabel("x: "));
				tb.add(x = new JSpinner());
				tb.add(dx = new JComboBox(new Object[] { "ft", "in", "m", "cm",
						"mm" }));
				p.add(tb);
			}
			{
				final Box tb = Box.createHorizontalBox();
				tb.add(new JLabel("y: "));
				tb.add(y = new JSpinner());
				tb.add(dy = new JComboBox(new Object[] { "ft", "in", "m", "cm",
						"mm" }));
				p.add(tb);
			}
			b.add(p);
			this.x.setEnabled(false);
			this.y.setEnabled(false);
			this.dx.setEnabled(false);
			this.dy.setEnabled(false);
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
		// throw new UnsupportedOperationException("Not implemented yet.");
	}

	public void setCurves(final CurveManager b) {
		// TODO Auto-generated method stub
//		throw new UnsupportedOperationException("Not implemented yet.");

		this.rock.setEnabled(b != null);
		this.dark.setEnabled(b != null);
		this.light.setEnabled(b != null);
		this.in.setEnabled(b != null);
		this.out.setEnabled(b != null);
		this.split.setEnabled(b != null);
		this.dt.setEnabled(b != null);
		this.x.setEnabled(b != null);
		this.y.setEnabled(b != null);
		this.dx.setEnabled(b != null);
		this.dy.setEnabled(b != null);
	}
}

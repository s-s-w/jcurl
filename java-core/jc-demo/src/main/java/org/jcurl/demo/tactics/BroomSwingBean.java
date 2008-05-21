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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.BroomPromptModel;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class BroomSwingBean extends JComponent implements ItemListener,
		ChangeListener, ActionListener, PropertyChangeListener {
	private static final Log log = JCLoggerFactory
			.getLogger(BroomSwingBean.class);

	private static final long serialVersionUID = -3512129363499720146L;

	private BroomPromptModel broom;
	private JRadioButton dark, light;
	private final JComponent in, out, split, dt, x, y, dx, dy;
	private final JComboBox rock;

	public BroomSwingBean() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		final Box b = Box.createVerticalBox();
		{
			final JPanel tb = new JPanel();
			tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
			tb.setBorder(BorderFactory.createTitledBorder("Active"));
			tb
					.add(rock = new JComboBox(new Object[] { 1, 2, 3, 4, 5, 6,
							7, 8 }));
			rock.addItemListener(this);
			dark = new JRadioButton("dark");
			dark.addActionListener(this);
			light = new JRadioButton("light");
			light.addActionListener(this);
			final ButtonGroup bg = new ButtonGroup();
			bg.add(dark);
			bg.add(light);
			tb.add(dark);
			tb.add(light);
			b.add(tb);
			rock.setEnabled(false);
			dark.setEnabled(false);
			light.setEnabled(false);
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
			split.setEnabled(false);
			dt.setEnabled(false);
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
			x.setEnabled(false);
			y.setEnabled(false);
			dx.setEnabled(false);
			dy.setEnabled(false);
		}
		this.add(b);
	}

	public void actionPerformed(final ActionEvent e) {
		if (e.getSource() == dark || e.getSource() == light)
			updateIndex();
		else
			log.warn("Unprocessed event from " + e.getSource());
	}

	private int findIndex() {
		final int idx8 = rock.getSelectedIndex();
		if (idx8 < 0)
			return -1;
		if (dark.isSelected())
			return RockSet.toIdx16(true, idx8);
		if (light.isSelected())
			return RockSet.toIdx16(false, idx8);
		return -1;
	}

	public BroomPromptModel getBroom() {
		return broom;
	}

	public void itemStateChanged(final ItemEvent e) {
		if (e.getSource() == rock)
			updateIndex();
		else
			log.warn("Unprocessed event from " + e.getSource());
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getSource() == broom) {
			if ("idx16".equals(evt.getPropertyName()))
				syncIdxBpm2This();
		} else
			log.warn("Unprocessed event from " + evt.getSource());
	}

	public void setBroom(final BroomPromptModel broom) {
		log.debug(broom);
		if (this.broom == broom)
			return;
		if (this.broom != null)
			broom.removePropertyChangeListener(this);
		this.broom = broom;
		syncIdxBpm2This();
		if (this.broom != null)
			broom.addPropertyChangeListener(this);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		rock.setEnabled(enabled);
		dark.setEnabled(enabled);
		light.setEnabled(enabled);

		enabled = false;
		in.setEnabled(enabled);
		out.setEnabled(enabled);
		split.setEnabled(enabled);
		dt.setEnabled(enabled);
		x.setEnabled(enabled);
		y.setEnabled(enabled);
		dx.setEnabled(enabled);
		dy.setEnabled(enabled);
	}

	public void stateChanged(final ChangeEvent e) {
		if (e.getSource() == dark || e.getSource() == light)
			updateIndex();
		else
			log.warn("Unprocessed event from " + e.getSource());
	}

	private void syncIdxBpm2This() {
		if (log.isDebugEnabled())
			log.debug("idx16=" + (broom == null ? -1 : broom.getIdx16()));
		if (broom == null || broom.getIdx16() < 0) {
			dark.setSelected(false);
			light.setSelected(false);
			rock.setSelectedIndex(-1);
			dark.setEnabled(false);
			light.setEnabled(false);
			rock.setEnabled(false);
		} else {
			if (RockSet.isDark(broom.getIdx16()))
				dark.setSelected(true);
			else
				light.setSelected(true);
			rock.setSelectedIndex(RockSet.toIdx8(broom.getIdx16()));
			dark.setEnabled(true);
			light.setEnabled(true);
			rock.setEnabled(true);
		}
	}

	private void updateIndex() {
		if (broom != null)
			broom.setIdx16(findIndex());
	}
}

/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
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
import javax.swing.BoundedRangeModel;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
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
	private static class SpinnerModelWrapper extends SpinnerNumberModel {
		/** */
		private static final long serialVersionUID = 2503438304002446476L;
		private final BoundedRangeModel wrap;

		public SpinnerModelWrapper(final BoundedRangeModel wrap) {
			this(wrap, 50);
		}

		public SpinnerModelWrapper(final BoundedRangeModel wrap, final int dt) {
			super(wrap.getValue(), wrap.getMinimum(), wrap.getMaximum(), dt);
			this.wrap = wrap;
		}

		@Override
		public void addChangeListener(final ChangeListener l) {
			wrap.addChangeListener(l);
		}

		@Override
		public Object getValue() {
			return wrap.getValue();
		}

		@Override
		public void removeChangeListener(final ChangeListener l) {
			wrap.removeChangeListener(l);
		}

		@Override
		public void setValue(final Object value) {
			wrap.setValue(((Number) value).intValue());
			super.setValue(wrap.getValue());
		}
	}

	private static final Log log = JCLoggerFactory
			.getLogger(BroomSwingBean.class);
	private static final long serialVersionUID = -3512129363499720146L;
	private BroomPromptModel broom;
	private final JRadioButton dark, light;
	private final JComponent dt, x, y, dx, dy;
	private final JRadioButton in, out;
	private final JComboBox rock;
	private final JSpinner split;

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
		}
		{
			final JPanel tb = new JPanel();
			tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
			tb.setBorder(BorderFactory.createTitledBorder("Handle"));
			in = new JRadioButton("In Turn");
			in.addActionListener(this);
			out = new JRadioButton("Out Turn");
			out.addActionListener(this);
			final ButtonGroup bg = new ButtonGroup();
			bg.add(in);
			bg.add(out);
			tb.add(out);
			tb.add(in);
			tb.add(Box.createHorizontalGlue());
			b.add(tb);
		}
		{
			final JPanel tb = new JPanel();
			tb.setLayout(new BoxLayout(tb, BoxLayout.X_AXIS));
			tb.setBorder(BorderFactory.createTitledBorder("Split Time"));
			tb.add(split = new JSpinner());
			tb.add(dt = new JComboBox(new Object[] { "1/1000 sec", "1/100 sec",
					"1/10 sec", "sec" }));
			tb.add(Box.createHorizontalGlue());
			b.add(tb);
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
		if (log.isDebugEnabled())
			log.debug(e.getSource());
		if (e.getSource() == dark || e.getSource() == light)
			updateIndex();
		if (e.getSource() == in || e.getSource() == out)
			updateHandle();
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
			else if ("outTurn".equals(evt.getPropertyName()))
				syncHandleBpm2This();
			else if ("splitTimeMillis".equals(evt.getPropertyName()))
				syncSplitBpm2This();
		} else
			log.warn("Unprocessed event from " + evt.getSource());
	}

	public void setBroom(final BroomPromptModel broom) {
		log.debug(broom);
		if (this.broom == broom)
			return;
		if (this.broom != null)
			this.broom.removePropertyChangeListener(this);
		this.broom = broom;
		syncIdxBpm2This();
		syncHandleBpm2This();
		syncSplitBpm2This();
		if (this.broom != null)
			this.broom.addPropertyChangeListener(this);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		rock.setEnabled(enabled);
		dark.setEnabled(enabled);
		light.setEnabled(enabled);

		in.setEnabled(enabled);
		out.setEnabled(enabled);

		split.setEnabled(enabled);

		enabled = false;
		dt.setEnabled(enabled);
		x.setEnabled(enabled);
		y.setEnabled(enabled);
		dx.setEnabled(enabled);
		dy.setEnabled(enabled);
	}

	public void stateChanged(final ChangeEvent e) {
		if (log.isDebugEnabled())
			log.debug(e.getSource());
		if (e.getSource() == dark || e.getSource() == light)
			updateIndex();
		else
			log.warn("Unprocessed event from " + e.getSource());
	}

	private void syncHandleBpm2This() {
		if (log.isDebugEnabled())
			log
					.debug("handle="
							+ (broom == null ? "-" : broom.getOutTurn() ? "out"
									: "in"));
		if (broom == null) {
			in.setEnabled(false);
			out.setEnabled(false);
			in.setSelected(false);
			out.setSelected(false);
		} else {
			in.setEnabled(true);
			out.setEnabled(true);
			if (broom.getOutTurn())
				out.setSelected(true);
			else
				in.setSelected(true);
		}
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
			dark.setEnabled(true);
			light.setEnabled(true);
			rock.setEnabled(true);
			if (RockSet.isDark(broom.getIdx16()))
				dark.setSelected(true);
			else
				light.setSelected(true);
			rock.setSelectedIndex(RockSet.toIdx8(broom.getIdx16()));
		}
	}

	private void syncSplitBpm2This() {
		if (broom != null && broom.getSplitTimeMillis() != null) {
			split.setModel(new SpinnerModelWrapper(broom.getSplitTimeMillis()));
			split.setEnabled(true);
		} else {
			//split.setModel(null);
			split.setEnabled(false);
		}
	}

	private void updateHandle() {
		if (broom == null)
			return;
		if (in.isSelected())
			broom.setOutTurn(false);
		if (out.isSelected())
			broom.setOutTurn(true);
	}

	private void updateIndex() {
		if (broom == null)
			return;
		broom.setIdx16(findIndex());
	}
}

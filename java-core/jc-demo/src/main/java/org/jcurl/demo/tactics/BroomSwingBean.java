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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.JSpinnerBoundedRange;
import org.jcurl.core.ui.Memento;
import org.jcurl.core.ui.BroomPromptModel.HandleMemento;
import org.jcurl.core.ui.BroomPromptModel.IndexMemento;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class BroomSwingBean extends JComponent implements ItemListener,
		FocusListener, ChangeListener, ActionListener, PropertyChangeListener {

	private static final boolean brm = true;
	private static final Log log = JCLoggerFactory
			.getLogger(BroomSwingBean.class);
	private static final long serialVersionUID = -3512129363499720146L;
	private BroomPromptModel broom;
	private final JRadioButton dark, light;
	private final JComponent dt, x, y, dx, dy;
	private final JRadioButton in, out;
	private final JComboBox rock;
	private final JSpinner split;
	private final JSpinnerBoundedRange split2;

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

			if (brm) {
				split2 = new JSpinnerBoundedRange();
				split2.addFocusListener(this);
				split = null;
			} else {
				split2 = null;
				split = new JSpinner();
				// log.info(split.getEditor().getClass().getName());
				split.addFocusListener(this);
				final JSpinner.NumberEditor ed = (JSpinner.NumberEditor) split
						.getEditor();
				ed.addFocusListener(this);
				ed.getTextField().addFocusListener(this);
			}

			tb.add(split2);

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

	public void focusGained(final FocusEvent e) {
		log.info(e.getSource());
	}

	public void focusLost(final FocusEvent e) {
		log.info(e.getSource());
	}

	public BroomPromptModel getBroom() {
		return broom;
	}

	public void itemStateChanged(final ItemEvent e) {
		if (log.isDebugEnabled())
			log.debug(e.getSource());
		if (e.getSource() == rock)
			updateIndex();
		else
			log.warn("Unprocessed event from " + e.getSource());
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		if (log.isDebugEnabled())
			log.debug(evt.getSource());
		if (evt.getSource() == broom) {
			if ("idx16".equals(evt.getPropertyName()))
				syncM2V(broom == null ? -1 : broom.getIdx16());
			else if ("outTurn".equals(evt.getPropertyName()))
				syncM2V(broom == null ? null : broom.getOutTurn());
			else if ("splitTimeMillis".equals(evt.getPropertyName()))
				syncM2V(broom == null ? null : broom.getSplitTimeMillis());
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
		syncM2V(this.broom == null ? -1 : this.broom.getIdx16());
		syncM2V(this.broom == null ? null : this.broom.getOutTurn());
		syncM2V(this.broom == null ? null : this.broom.getSplitTimeMillis());
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

		if (brm)
			split2.setEnabled(enabled);
		else
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

	private void syncM2V(final Boolean handle) {
		if (log.isDebugEnabled())
			log.debug("handle="
					+ (handle == null ? "-" : handle.booleanValue() ? "out"
							: "in"));
		if (handle == null) {
			in.setEnabled(false);
			out.setEnabled(false);
			in.setSelected(false);
			out.setSelected(false);
		} else {
			in.setEnabled(true);
			out.setEnabled(true);
			if (handle.booleanValue())
				out.setSelected(true);
			else
				in.setSelected(true);
		}
	}

	private void syncM2V(final BoundedRangeModel s) {
		if (s != null) {
			if (brm) {
				split2.setBRM(s);
				split2.setEnabled(true);
			} else {
				// split.setModel(new
				// JSpinnerBoundedRange.SpinnerModelWrapper(s));
				split.setEnabled(true);
				// add focus event listener
				// http://forum.java.sun.com/thread.jspa?threadID=409748&forumID=57
				final JSpinner.DefaultEditor ed = (JSpinner.DefaultEditor) split
						.getEditor();
				ed.getTextField().addFocusListener(this);
			}
		} else
			// split.setModel(null);
			split.setEnabled(false);
	}

	private void syncM2V(final int idx16) {
		if (log.isDebugEnabled())
			log.debug("idx16=" + idx16);
		if (idx16 < 0) {
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
			if (RockSet.isDark(idx16))
				dark.setSelected(true);
			else
				light.setSelected(true);
			rock.setSelectedIndex(RockSet.toIdx8(idx16));
		}
	}

	private void updateHandle() {
		if (broom == null)
			return;
		if (in.isSelected())
			view2model(new HandleMemento(broom, false));
		if (out.isSelected())
			view2model(new HandleMemento(broom, true));
	}

	private void updateIndex() {
		if (broom == null)
			return;
		view2model(new IndexMemento(broom, findIndex()));
	}

	private void view2model(final Memento<?> m) {
		m.run();
	}
}

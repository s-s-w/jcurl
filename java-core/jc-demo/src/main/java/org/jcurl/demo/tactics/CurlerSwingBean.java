/*
 * jcurl java curling software framework http://www.jcurl.org Copyright (C)
 * 2005-2009 M. Rohrmoser
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.Curler;
import org.jcurl.core.api.Unit;
import org.jcurl.core.helpers.Service;
import org.jcurl.core.impl.CurlerDenny;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.ChangeManager;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurlerSwingBean extends JComponent implements HasChanger,
		PropertyChangeListener, ChangeListener, ItemListener {
	private static class ComboItem {
		final Class<? extends Curler> c;

		public ComboItem(final Class<? extends Curler> c) {
			this.c = c;
		}

		@Override
		public String toString() {
			return c.getSimpleName();
		}
	}

	private static final Log log = JCLoggerFactory
			.getLogger(CurlerSwingBean.class);
	private static final long serialVersionUID = 2303066300268819434L;

	private static ComboItem[] findCurlers() {
		final Collection<ComboItem> tmp = new ArrayList<ComboItem>();
		if (false)
			for (final Class<Curler> elem : Service.providerClasses(
					Curler.class, null))
				tmp.add(new ComboItem(elem));
		else
			tmp.add(new ComboItem(CurlerDenny.class));
		final ComboItem[] dat = new ComboItem[tmp.size()];
		tmp.toArray(dat);
		return dat;
	}

	private ChangeManager changer;
	private final JSpinnerNumberUnit curl;
	private Curler curler;
	private final JComboBox curlers;
	private final JSpinnerNumberUnit time;

	public CurlerSwingBean() {
		setLayout(new BorderLayout());
		final Box b = Box.createVerticalBox();
		{
			final JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
			p.setBorder(BorderFactory.createTitledBorder("Curl Model"));
			p.add(curlers = new JComboBox(findCurlers()));
			b.add(p);
		}
		{
			final JPanel p = new JPanel();
			p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
			p.setBorder(BorderFactory.createTitledBorder("Draw-To-Tee"));
			{
				time = new JSpinnerNumberUnit();
				time.setLabel("time: ");
				time.setBase(Unit.SECOND);
				time.setModel(new SpinnerNumberModel(24.0, 17.0, 28.0, 0.1));
				time.addPropertyChangeListener(this);
				time.addChangeListener(this);
				p.add(time);
			}
			{
				curl = new JSpinnerNumberUnit();
				curl.setLabel("curl: ");
				curl.setBase(Unit.METER);
				curl.setChoose(Unit.FOOT, Unit.INCH, Unit.CENTIMETER,
						Unit.METER);
				curl.setModel(new SpinnerNumberModel(1.0, 0.0, 3.0, 0.1));
				curl.addPropertyChangeListener(this);
				curl.addChangeListener(this);
				p.add(curl);
			}
			b.add(p);
		}
		add(b, BorderLayout.NORTH);
		add(new JLabel("TODO: other settings"), BorderLayout.CENTER);
		setEnabled(false);
	}

	public ChangeManager getChanger() {
		return ChangeManager.getTrivial(changer);
	}

	public Curler getCurler() {
		return curler;
	}

	public void itemStateChanged(final ItemEvent e) {
		log.warn("Unconsumed event from " + e.getSource());
	}

	public void propertyChange(final PropertyChangeEvent evt) {
		log.warn("Unconsumed event '" + evt.getPropertyName() + "' from "
				+ evt.getSource().getClass().getName() + ": "
				+ evt.getOldValue() + " -> " + evt.getNewValue());
	}

	public void setChanger(final ChangeManager changer) {
		final ChangeManager old = this.changer;
		if (old == changer)
			return;
		firePropertyChange("changer", old, this.changer = changer);
	}

	public void setCurler(final Curler curler) {
		final Curler old = this.curler;
		if (old == curler)
			return;
		firePropertyChange("curler", old, this.curler = curler);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		curlers.setEnabled(enabled);
		time.setEnabled(enabled);
		curl.setEnabled(enabled);
		super.setEnabled(enabled);
	}

	public void stateChanged(final ChangeEvent e) {
		log.warn("Unconsumed event from " + e.getSource());
	}
}

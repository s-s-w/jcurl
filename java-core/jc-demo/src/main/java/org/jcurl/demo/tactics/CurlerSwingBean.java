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

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.Curler;
import org.jcurl.core.helpers.Service;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.ChangeManager;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class CurlerSwingBean extends JComponent implements HasChanger {

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

	private static ComboItem[] findCurlers(final Class<Curler> src) {
		final Collection<ComboItem> tmp = new ArrayList<ComboItem>();
		for (final Class<Curler> elem : Service.providerClasses(src, null))
			tmp.add(new ComboItem(elem));
		final ComboItem[] dat = new ComboItem[tmp.size()];
		tmp.toArray(dat);
		return dat;
	}

	private ChangeManager changer;
	private Curler curler;

	public CurlerSwingBean() {
		setLayout(new BorderLayout());
		if (true)
			add(new JLabel("TODO Ice properties"), BorderLayout.NORTH);
		else
			add(new JComboBox(findCurlers(Curler.class)), BorderLayout.NORTH);
	}

	public ChangeManager getChanger() {
		return ChangeManager.getTrivial(changer);
	}

	public Curler getCurler() {
		return curler;
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
}

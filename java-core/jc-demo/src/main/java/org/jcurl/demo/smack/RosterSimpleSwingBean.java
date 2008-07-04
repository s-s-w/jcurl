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

package org.jcurl.demo.smack;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JList;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.packet.Presence;

/**
 * A simple {@link JList}-based roster list.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class RosterSimpleSwingBean extends JComponent implements RosterListener {

	private static final Log log = JCLoggerFactory
			.getLogger(RosterSimpleSwingBean.class);
	private static final long serialVersionUID = 8787616993298867816L;
	private final Vector<String> data = new Vector<String>();
	private final JList l;
	private Roster roster;
	private final Map<String, XmppAddress> s2a = new WeakHashMap<String, XmppAddress>();
	private Pattern star;

	public RosterSimpleSwingBean() {
		setLayout(new BorderLayout());
		l = new JList(data);
		add(l, BorderLayout.CENTER);
	}

	public void entriesAdded(final Collection<String> arg0) {
		log.warn("ignored");
	}

	public void entriesDeleted(final Collection<String> arg0) {
		log.warn("ignored");
	}

	public void entriesUpdated(final Collection<String> arg0) {
		log.warn("ignored");
	}

	public Roster getRoster() {
		return roster;
	}

	public Pattern getStar() {
		return star;
	}

	public void presenceChanged(final Presence presence) {
		// strip the resource
		final XmppAddress a0 = XmppAddress.parse(presence.getFrom());
		if (a0 == null)
			return;
		String a = a0.toString(null);

		// add a mark if the star pattern matches:
		if (star != null && star.matcher(presence.getFrom()).matches())
			a = "* " + a;

		// update data
		if (presence.isAvailable() && !presence.isAway()) {
			data.add(a);
			s2a.put(a, a0);
		} else {
			data.remove(a);
			s2a.remove(a);
		}
		// update visible list
		l.setListData(data);
	}

	public void setRoster(final Roster roster) {
		if (this.roster != null)
			this.roster.removeRosterListener(this);
		this.roster = roster;
		if (this.roster != null)
			this.roster.addRosterListener(this);
		updateList();
	}

	public void setStar(final Pattern star) {
		this.star = star;
		updateList();
	}

	private void updateList() {
		data.clear();
		if (roster != null)
			for (final RosterEntry elem : roster.getEntries())
				presenceChanged(roster.getPresence(elem.getUser()));
	}
}

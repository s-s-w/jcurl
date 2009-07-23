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

package org.jcurl.demo.smack;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JList;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

/**
 * A simple {@link JList}-based roster list. New {@link Chat}s are created on
 * double-clicking the target address.
 * <p>
 * Register a {@link ChatManagerListener} with
 * {@link XMPPConnection#getChatManager()} to get notice of them.
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class RosterSimpleSwingBean extends JComponent implements RosterListener {

	private static final Log log = JCLoggerFactory
			.getLogger(RosterSimpleSwingBean.class);
	private static final long serialVersionUID = 8787616993298867816L;
	private ChatManager chatManager;
	private final Vector<String> data = new Vector<String>();
	private final JList l;
	private Roster roster;
	private final Map<String, XmppAddress> s2a = new WeakHashMap<String, XmppAddress>();
	private Pattern star;

	public RosterSimpleSwingBean() {
		setLayout(new BorderLayout());
		l = new JList(data);
		// see
		// http://java.sun.com/products/jfc/tsc/tech_topics/jlist_1/jlist.html:
		final MouseListener mouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					final int index = l.locationToIndex(e.getPoint());
					final Object o = l.getModel().getElementAt(index);
					if (o != null && getChatManager() != null)
						getChatManager()
								.createChat(s2a.get(o).toString(), null);
				}
			}
		};
		l.addMouseListener(mouseListener);
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

	public ChatManager getChatManager() {
		return chatManager;
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
		Collections.sort(data);
		l.setListData(data);
	}

	public void setChatManager(final ChatManager chatManager) {
		this.chatManager = chatManager;
	}

	public void setConn(final XMPPConnection conn) {
		setChatManager(conn.getChatManager());
		setRoster(conn.getRoster());
	}

	@Override
	public void setEnabled(final boolean enabled) {
		l.setEnabled(enabled);
		super.setEnabled(enabled);
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

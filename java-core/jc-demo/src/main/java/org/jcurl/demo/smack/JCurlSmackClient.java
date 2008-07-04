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
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jdesktop.application.SingleFrameApplication;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

/**
 * A very simple Jabber/Xmpp client.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class JCurlSmackClient extends SingleFrameApplication {

	static class RosterChanges implements RosterListener {
		private static final Log lo = JCLoggerFactory
				.getLogger(RosterChanges.class);

		public void entriesAdded(final Collection<String> arg0) {
			lo.info(toString(arg0, ", "));
		}

		public void entriesDeleted(final Collection<String> arg0) {
			lo.info(toString(arg0, ", "));
		}

		public void entriesUpdated(final Collection<String> arg0) {
			lo.info(toString(arg0, ", "));
		}

		public void presenceChanged(final Presence p) {
			lo.info(p.getFrom() + " -> " + p.getTo() + ": " + p.isAvailable());
		}

		private CharSequence toString(final Collection<?> c,
				final CharSequence sep) {
			final StringBuilder ret = new StringBuilder();
			boolean start = true;
			for (final Object elem : c) {
				if (!start) {
					ret.append(sep);
					start = false;
				}
				ret.append(elem.toString());
			}
			return ret;
		}
	}

	static interface RosterGui {

	}

	static class XmppAccount {
		private XMPPConnection conn;
		private CharSequence pwd;
		private Roster roster;
		private XmppAddress uid;

		public XMPPConnection getConn() {
			return conn;
		}

		public CharSequence getPwd() {
			return pwd;
		}

		public Roster getRoster() {
			return roster;
		}

		public XmppAddress getUid() {
			return uid;
		}

		public XMPPConnection login(final String resource) throws XMPPException {
			if (conn != null && conn.isConnected())
				conn.disconnect();
			conn = new XMPPConnection(uid.getHost());
			conn.connect();
			conn.login(uid.getAccount(), pwd.toString(), resource);
			roster = conn.getRoster();
			return conn;
		}

		public void setConn(final XMPPConnection conn) {
			this.conn = conn;
		}

		public void setPwd(final CharSequence pwd) {
			this.pwd = pwd;
		}

		public void setUid(final XmppAddress uid) {
			this.uid = uid;
		}
	}

	private static Properties loadClassProps(final Class<?> c, Properties p)
			throws IOException {
		if (p == null)
			p = new Properties();
		final String r = "/" + c.getName().replace('.', '/') + ".properties";
		final InputStream i = c.getResourceAsStream(r);
		if (i == null) {
			System.err.println("Add a file '." + r
					+ "' to the classpath containing the properties:");
			System.err
					.println("\nacc_uid = foo@foo.org # a jabber account name");
			System.err.println("\nacc_pwd = .. # a jabber account password");
		}
		p.load(i);
		return p;
	}

	public static void main(final String[] args) {
		launch(JCurlSmackClient.class, args);
	}

	private final XmppAccount acc = new XmppAccount();
	private final RosterSimpleSwingBean sro = new RosterSimpleSwingBean();

	public JCurlSmackClient() throws IOException, XMPPException,
			InterruptedException {
		// get uid + pwd from a .properties file
		final Properties p = loadClassProps(JCurlSmackClient.class, null);
		acc.setUid(XmppAddress.parse(p.getProperty("acc_uid")));
		acc.setPwd(p.getProperty("acc_pwd"));

		// login and get the present buddies

		final String resource = this.getClass().getSimpleName();
		final XMPPConnection c = acc.login(resource);
		sro.setStar(Pattern.compile("[^/]+/" + resource));
		sro.setRoster(acc.getRoster());
	}

	@Override
	protected void startup() {
		final JComponent pv = new JPanel();
		pv.setLayout(new BorderLayout());
		final Box conversation = Box.createVerticalBox();
		pv.add(new JScrollPane(conversation,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED),
				BorderLayout.CENTER);
		final JTextField text = new JTextField();
		pv.add(new JScrollPane(text,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS),
				BorderLayout.SOUTH);

		final JSplitPane ph = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		ph.add(pv, JSplitPane.LEFT);
		ph.add(new JScrollPane(sro), JSplitPane.RIGHT);

		// final JPanel p = new JPanel();
		// p.setLayout(new BorderLayout());
		// p.add(new JScrollPane(sro), BorderLayout.EAST);

		show(ph);
	}
}

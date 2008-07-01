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

package org.jcurl.smack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PacketExtension;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class SmackTest extends TestCase {

	static class XmppAddress {
		private static final Pattern p = Pattern
				.compile("([^@]+)@([^/]+)(?:/(.*))?");

		public static final XmppAddress parse(final CharSequence str) {
			final Matcher m = p.matcher(str);
			if (!m.matches())
				return null;
			return new XmppAddress(m.group(1), m.group(2), m.group(3));
		}

		public static String toString(final String account, final String host,
				final String resource) {
			if (resource != null && resource.length() > 0)
				return account + "@" + host + "/" + resource;
			else
				return account + "@" + host;
		}

		private final String account;
		private final String host;
		private final String resource;

		public XmppAddress(final String account, final String host) {
			this(account, host, null);
		}

		public XmppAddress(final String account, final String host,
				final String resource) {
			this.account = account;
			this.host = host;
			this.resource = resource == null || resource.length() <= 0 ? null
					: resource;
		}

		public String getAccount() {
			return account;
		}

		public String getHost() {
			return host;
		}

		public String getResource() {
			return resource;
		}

		@Override
		public String toString() {
			return toString(account, host, resource);
		}

		public String toString(final String resource) {
			return toString(account, host, resource);
		}
	}

	static XMPPConnection login(final XmppAddress uid, final String pwd,
			final String resource) throws XMPPException {
		final XMPPConnection conn = new XMPPConnection(uid.getHost());
		conn.connect();
		conn.login(uid.getAccount(), pwd, resource);
		return conn;
	}

	private final String dst_pwd = null;
	private final XmppAddress dst_uid = XmppAddress
			.parse("mrohrmoser@jabber.org/A");

	private final String src_pwd = null;
	private final XmppAddress src_uid = XmppAddress
			.parse("mrohrmoser@gmx.de/B");

	public void testConnection() throws XMPPException, InterruptedException {
		final String resource = "JCurl-Tactics";
		final String namespace = "http://www.jcurl.org/xmpp/xep/tactics/2008";
		final String subnode = "payload";

		if (dst_pwd == null || src_pwd == null)
			return;

		final XMPPConnection dst = login(dst_uid, dst_pwd, resource);
		dst.getChatManager().addChatListener(new ChatManagerListener() {
			public void chatCreated(final Chat arg0, final boolean arg1) {
				arg0.addMessageListener(new MessageListener() {
					public void processMessage(final Chat chat,
							final Message message) {
						System.out.println("Received message: "
								+ message.toXML());
						final PacketExtension pe = message
								.getExtension(namespace);
						if (pe == null)
							return;
						System.out.println(pe.getElementName() + " "
								+ pe.toXML() + " " + pe.toString());
						final Pattern p = Pattern.compile(".+<" + subnode
								+ ">(.*)</" + subnode + ">.+");
						final Matcher m = p.matcher(pe.toXML());
						if (!m.matches()) {
							System.out.println("no match");
							return;
						}
						System.out.println("dt = "
								+ (System.currentTimeMillis() - Long
										.parseLong(m.group(1))) + " millis");
					}
				});
			}
		});

		final XMPPConnection src = login(src_uid, src_pwd, resource);
		try {
			final Chat chat = src.getChatManager().createChat(
					dst_uid.toString(resource), new MessageListener() {

						public void processMessage(Chat chat, Message message) {
							System.out.println("Received message: "
									+ message.toXML());
							message.getExtension(namespace);
						}
					});
			chat.sendMessage("Howdy!");

			final Message jcm = new Message();
			jcm.addExtension(new PacketExtension() {
				public String getElementName() {
					return "jcurl";
				}

				public String getNamespace() {
					return namespace;
				}

				public String toXML() {
					return "<" + getElementName() + " xmlns='" + getNamespace()
							+ "'><" + subnode + ">"
							+ System.currentTimeMillis() + "</" + subnode
							+ "></" + getElementName() + ">";
				}
			});
			jcm.setLanguage("en");
			jcm.setSubject("JCurl data update (subject)");
			chat.sendMessage(jcm);

			Thread.sleep(1000);
		} finally {
			dst.disconnect();
			src.disconnect();
		}
	}

	public void testXmppAddressParse() {
		XmppAddress a = XmppAddress.parse("mrohrmoser@gmx.de/JCurl");
		assertEquals("mrohrmoser", a.getAccount());
		assertEquals("gmx.de", a.getHost());
		assertEquals("JCurl", a.getResource());
		assertEquals("mrohrmoser@gmx.de/JCurl", a.toString());

		a = XmppAddress.parse("mrohrmoser@gmx.de/");
		assertEquals("mrohrmoser", a.getAccount());
		assertEquals("gmx.de", a.getHost());
		assertEquals(null, a.getResource());
		assertEquals("mrohrmoser@gmx.de", a.toString());

		a = XmppAddress.parse("mrohrmoser@gmx.de");
		assertEquals("mrohrmoser", a.getAccount());
		assertEquals("gmx.de", a.getHost());
		assertEquals(null, a.getResource());
		assertEquals("mrohrmoser@gmx.de", a.toString());
	}
}

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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.jcurl.core.ui.ChangeManager;
import org.jcurl.core.ui.Memento;
import org.jcurl.core.ui.UndoableMemento;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.PacketExtension;

/**
 * Could well be a decorator to add Jabber/XMPP remoting to
 * {@link ChangeManager}s.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class SmackChangeManager extends ChangeManager implements
		ChatManagerListener {

	private class MementoPackageExtension implements PacketExtension {
		private final Memento<?> me;
		private String xml = null;

		public MementoPackageExtension(final Memento<?> me) {
			this.me = me;
		}

		public String getElementName() {
			return temporaryElem;
		}

		public String getNamespace() {
			return namespace;
		}

		public String toXML() {
			if (xml != null)
				return xml;
			return xml = toXml(me);
		}
	}

	private static final String JCURL_GRAPHICS_SYNC = "jcurl graphics sync";
	private static final String namespace = "http://www.jcurl.org/xep/"
			+ SmackChangeManager.class.getName();
	static final Pattern pat = Pattern
			.compile("<([^ >]+)\\s+xmlns='([^']*)'><([^ >]+)\\s+id='([^']*)'>([^<]*)</([^ >]+)></([^ >]+)>");
	private static final String subnode = "base64gz";
	private static final String temporaryElem = "temporary";
	private static final String US_ASCII = "US-ASCII";

	/**
	 * Turn a {@link PacketExtension#toXML()} string into a {@link Memento}.
	 * 
	 * @see #toXml(Memento)
	 */
	static Memento<?> fromXml(final String xml) {
		final Matcher m = pat.matcher(xml);
		if (!m.matches())
			throw new IllegalStateException("xml doesn't match: " + xml);
		if (!m.group(1).equals(m.group(7)) || !m.group(3).equals(m.group(6)))
			throw new IllegalStateException("xml not well formed: " + xml);
		if (!namespace.equals(m.group(2)))
			throw new IllegalStateException("wrong namespace: " + m.group(2));
		if (!temporaryElem.equals(m.group(1)))
			throw new IllegalStateException("wrong elementName: " + m.group(1));
		if (!subnode.equals(m.group(3)))
			throw new IllegalStateException("wrong subnode: " + m.group(3));
		final String id = m.group(4);
		try {
			// deserialize the memento from BASE64:
			final ByteArrayInputStream bin = new ByteArrayInputStream(Base64
					.decodeBase64(m.group(5).getBytes(US_ASCII)));
			final GZIPInputStream gin = new GZIPInputStream(bin);
			final ObjectInputStream oin = new ObjectInputStream(gin);
			try {
				final Memento<?> me = (Memento<?>) oin.readObject();
				// FIXME id -> context.
				me.setContext(null);
				return me;
			} catch (final ClassNotFoundException e) {
				throw new IllegalStateException("Unhandled", e);
			} finally {
				oin.close();
			}
		} catch (final IOException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	/**
	 * Turn a {@link Memento} into a {@link PacketExtension#toXML} string.
	 * 
	 * @see #fromXml(String)
	 */
	static String toXml(final Memento<?> me) {
		final StringBuilder s = new StringBuilder();
		s.append('<').append(temporaryElem).append(" xmlns='")
				.append(namespace).append("'>");
		s.append("<").append(subnode).append(" id='");
		{
			// FIXME find/create the id.
			s.append("TODO id");
		}
		s.append("'>");
		try {
			// serialize the memento to BASE64:
			final ByteArrayOutputStream bout = new ByteArrayOutputStream();
			final GZIPOutputStream gout = new GZIPOutputStream(bout);
			final ObjectOutputStream oout = new ObjectOutputStream(gout);
			oout.writeObject(me);
			oout.close();
			s.append(new String(Base64.encodeBase64(bout.toByteArray(), false),
					US_ASCII));
		} catch (final IOException e) {
			throw new RuntimeException("Unhandled", e);
		}
		s.append("</").append(subnode).append(">");
		s.append("</").append(temporaryElem).append('>');
		return s.toString();
	}

	private Chat chat = null;
	/** push incoming remote changes to the local data model */
	private final MessageListener msg = new MessageListener() {
		/** dispatch remote messages to local methods */
		public void processMessage(final Chat arg0, final Message arg1) {
			final PacketExtension pe = arg1.getExtension(namespace);
			if (pe == null)
				return;
			if (temporaryElem.equals(pe.getElementName()))
				superTemporary(fromXml(pe.toXML()));
			else if (superUndoable(convert_(arg1)))
				;
			else
				throw new IllegalStateException(pe.getElementName());
			return;
		}
	};

	public SmackChangeManager() {
		super();
	}

	public SmackChangeManager(final Executor executor) {
		super(executor);
	}

	public void chatCreated(final Chat chat, final boolean arg1) {
		if (this.chat != null)
			this.chat.removeMessageListener(msg);
		this.chat = chat;
		if (this.chat != null)
			this.chat.addMessageListener(msg);
	}

	private Message convert(final Memento<?> pre, final Memento<?> post) {
		// TODO
		throw new UnsupportedOperationException();
	}

	private UndoableMemento<?> convert_(final Message m) {
		// TODO
		throw new UnsupportedOperationException();
	}

	private boolean superTemporary(final Memento<?> m) {
		if (m == null)
			return false;
		super.temporary(m);
		return true;
	}

	private boolean superUndoable(final UndoableMemento m) {
		if (m == null)
			return false;
		// return super.undoable(m.pre, m.post);
		// TODO
		throw new UnsupportedOperationException();
	}

	/**
	 * Push local gui-generated Mementos to the local data model and publish
	 * them
	 */
	@Override
	public void temporary(final Memento<?> m) {
		super.temporary(m);
		if (chat == null)
			return;
		try {
			final Message r = new Message();
			r.setBody(JCURL_GRAPHICS_SYNC);
			r.addExtension(new MementoPackageExtension(m));
			chat.sendMessage(r);
		} catch (final XMPPException e) {
			throw new RuntimeException("Unhandled", e);
		}
	}

	/**
	 * Push local gui-generated Mementos to the local data model and publish
	 * them
	 */
	@Override
	public <E> boolean undoable(final Memento<E> pre, final Memento<E> post) {
		if (!super.undoable(pre, post))
			return false;
		if (chat != null)
			try {
				chat.sendMessage(convert(pre, post));
			} catch (final XMPPException e) {
				throw new RuntimeException("Unhandled", e);
			}
		return true;
	}
}

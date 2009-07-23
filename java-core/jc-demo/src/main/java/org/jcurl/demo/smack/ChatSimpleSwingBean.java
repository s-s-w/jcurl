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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

/**
 * A simple chat-message sender GUI component. Implements
 * {@link ChatManagerListener} to keep track of the current chat to send to.
 * <p>
 * Locally created {@link Chat}s typically come from a
 * {@link RosterSimpleSwingBean}.
 * </p>
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class ChatSimpleSwingBean extends JComponent implements
		ChatManagerListener {

	private static final Log log = JCLoggerFactory
			.getLogger(ChatSimpleSwingBean.class);

	private static final long serialVersionUID = 3756526957709716506L;
	private Chat chat = null;
	private final JTextField text;

	public ChatSimpleSwingBean() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		text = new JTextField();
		// see
		// http://java.sun.com/products/jfc/tsc/tech_topics/jlist_1/jlist.html:
		final KeyListener keyListener = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent evt) {
				if ('\n' == evt.getKeyChar())
					try {
						send(text.getText());
						text.setText("");
					} catch (XMPPException e) {
						log.error("Sending \"" + text.getText() + "\"", e);
					}
			}
		};
		text.addKeyListener(keyListener);
		add(text);
		setEnabled(false);
	}

	public void chatCreated(final Chat chat2, final boolean flag) {
		if (chat != null
				&& !chat.getParticipant().equals(chat2.getParticipant())) {
			log.error("Ingore the intruder " + chat2.getParticipant());
			return;
		}
		chat = chat2;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setEnabled(true);
			}
		});
	}

	private void send(final CharSequence txt) throws XMPPException {
		if (chat == null || txt == null || txt.length() == 0)
			return;
		final Message msg = new Message(chat.getParticipant(), Type.chat);
		msg.setBody(txt.toString());
		chat.sendMessage(msg);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		if (isEnabled() == enabled)
			return;
		log.info(enabled);
		text.setEnabled(enabled);
		super.setEnabled(enabled);
	}
}

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

import javax.swing.JComponent;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;

/**
 * Log a conversation. Register with {@link XMPPConnection#getChatManager()} to
 * keep track of the current chats.
 * 
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class ChatLogSimpleSwingBean extends JComponent implements
		ChatManagerListener, MessageListener {

	private static final Log log = JCLoggerFactory
			.getLogger(ChatLogSimpleSwingBean.class);
	private static final long serialVersionUID = 7185219077872195562L;

	public ChatLogSimpleSwingBean() {}

	public void chatCreated(final Chat chat, final boolean flag) {
		chat.addMessageListener(this);
	}

	public void processMessage(final Chat chat, final Message message) {
		if (message.getBody() == null)
			log.info(message.getFrom() + " -> " + message.getTo() + ": "
					+ message.toXML());
		else
			log.info(message.getFrom() + " -> " + message.getTo() + ": "
					+ message.getBody());
	}

}

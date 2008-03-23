/*
 * jcurl curling simulation framework http://www.jcurl.org Copyright (C)
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

package org.jcurl.core.ui;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.MessageExecutor;
import org.jcurl.core.ui.MessageExecutor.ExecutorType;
import org.jcurl.core.ui.MessageExecutor.Message;
import org.jcurl.core.ui.MessageExecutor.Parallel;
import org.jcurl.core.ui.MessageExecutor.Single;
import org.jcurl.core.ui.MessageExecutor.SwingEDT;

/**
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class MessageExecutorTest extends TestCase {

	private static class Message1 implements Message<SwingEDT> {
		public void run() {
			log.info("Hello, " + this.getClass().getName());
		}
	}

	private static class Message2 implements Message<Single> {
		public void run() {
			log.info("Hello, " + this.getClass().getName());
		}
	}

	private static class Message3 implements Message<Parallel> {
		public void run() {
			log.info("Hello, " + this.getClass().getName());
		}
	}

	private static final Log log = JCLoggerFactory
			.getLogger(MessageExecutorTest.class);;

	public void testInvoke() {
		final MessageExecutor mb = MessageExecutor.getInstance();
		mb.execute(new Message1());
		mb.execute(new Message2());
		mb.execute(new Message3());
	}

	public void testRTTI() {
		assertEquals(SwingEDT.class, MessageExecutor.findGenericTypeParam(Message1.class,
				ExecutorType.class));
	}
}

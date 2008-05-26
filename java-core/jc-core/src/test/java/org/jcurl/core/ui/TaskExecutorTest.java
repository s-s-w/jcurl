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

package org.jcurl.core.ui;

import java.lang.reflect.ParameterizedType;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.TaskExecutor.ForkableFixed;
import org.jcurl.core.ui.TaskExecutor.Parallel;
import org.jcurl.core.ui.TaskExecutor.SmartQueue;
import org.jcurl.core.ui.TaskExecutor.SwingEDT;
import org.jcurl.core.ui.TaskExecutor.Task;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class TaskExecutorTest extends TestCase {

	private static class Message1 implements Task<SwingEDT> {
		public void run() {
			log.info("Hello, " + this.getClass().getName());
		}
	}

	private static class Message2 implements Task<SmartQueue> {
		public void run() {
			log.info("Hello, " + this.getClass().getName());
		}
	}

	private static class Message3 implements Task<Parallel> {
		public void run() {
			log.info("Hello, " + this.getClass().getName());
		}
	}

	private static class MessageBase1 extends ForkableFixed<SwingEDT> {
		public void run() {
			log.info("Hello, " + this.getClass().getName());
		}
	}

	private static final Log log = JCLoggerFactory
			.getLogger(TaskExecutorTest.class);;

	public void _testInvoke() {
		final TaskExecutor mb = TaskExecutor.getInstance();
		mb.execute(new Message1());
		mb.execute(new Message2());
		mb.execute(new Message3());
	}

	public void testRTTI() {
		assertEquals(0, MessageBase1.class.getGenericInterfaces().length);
		assertEquals(
				"org.jcurl.core.ui.TaskExecutor.org.jcurl.core.ui.TaskExecutor$ForkableFixed<org.jcurl.core.ui.TaskExecutor$SwingEDT>",
				MessageBase1.class.getGenericSuperclass().toString());
		ParameterizedType pt = (ParameterizedType) MessageBase1.class
				.getGenericSuperclass();
		assertEquals(SwingEDT.class, pt.getActualTypeArguments()[0]);

		assertEquals(1, Message1.class.getGenericInterfaces().length);
		assertEquals(Object.class, Message1.class.getGenericSuperclass());
		pt = (ParameterizedType) Message1.class.getGenericInterfaces()[0];
		assertEquals(SwingEDT.class, pt.getActualTypeArguments()[0]);

		assertEquals(SwingEDT.class, TaskExecutor
				.findMessageTypeParam(Message1.class));
		assertEquals(SwingEDT.class, TaskExecutor
				.findMessageTypeParam(MessageBase1.class));
	}
}

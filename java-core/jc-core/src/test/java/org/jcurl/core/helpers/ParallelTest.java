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

package org.jcurl.core.helpers;

import javolution.context.ConcurrentContext;
import junit.framework.TestCase;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class ParallelTest extends TestCase {

	private final Runnable sleep = new Runnable() {
		public void run() {
			try {
				Thread.sleep(1000);
			} catch (final InterruptedException e) {
				return;
			}
		}
	};

	public void testParallel() {
		ConcurrentContext.enter();
		try {
			ConcurrentContext.execute(sleep);
		} finally {
			ConcurrentContext.exit();
		}
	}

}

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

package org.jcurl.demo.tactics;

import java.util.concurrent.Executor;

import org.jcurl.core.ui.Memento;
import org.jcurl.core.ui.TaskExecutor.SwingEDT;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class MementoHandler {
	private final Executor executor;

	public MementoHandler() {
		// this(Executors.newSingleThreadExecutor());
		//this(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
		//		new HFBlockingQueue<Runnable>()));
		this(new SwingEDT());
	}

	public MementoHandler(final Executor executor) {
		this.executor = executor;
	}

	public void add(final Memento<?> m, boolean undoable) {
		executor.execute(m);
	}
}

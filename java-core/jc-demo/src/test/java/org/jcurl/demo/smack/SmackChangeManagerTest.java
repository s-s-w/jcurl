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

import java.awt.geom.Point2D;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.Memento;
import org.jcurl.core.ui.PosMemento;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class SmackChangeManagerTest extends TestCase {
	private static final Log log = JCLoggerFactory
			.getLogger(SmackChangeManagerTest.class);

	private static final String s = "<temporary xmlns=\'http://www.jcurl.org/xep/org.jcurl.demo.smack.SmackChangeManager\'><base64gz id=\'TODO id\'>H4sIAAAAAAAAAFvzloG1uIhBJr8oXS8rubQoRy85vyhVrzRTLyC/2Dc1NzWvJH/vnW9M7KZnRJgYGCqKGGQxlQblJ2dD1TJAACMTA6MnA2tmSoWhWQV2441cMLQwuTAwVgBxJVCLJKYWDPUMFQUOUJ4DBwMMMAIAndlXWtQAAAA=</base64gz></temporary>";

	public void testFromXmlToXmlSymmetry() {
		// warm up once:
		final Memento<?> m = SmackChangeManager.fromXml(s);
		assertNotNull(m);
		assertEquals(s, SmackChangeManager.toXml(m));

		// try a loop
		final int loops = 100;
		final long start = System.currentTimeMillis();
		for (int i = loops - 1; i >= 0; i--)
			SmackChangeManager.toXml(SmackChangeManager.fromXml(s));
		log.info(loops + " loops took " + (System.currentTimeMillis() - start)
				+ " millis");
	}

	public void testPattern() {
		Pattern
				.compile("<([^ >]+)\\s+xmlns='([^']*)'><([^ >]+)\\s+id='([^']*)'>([^<]*)</([^ >]+)></([^ >]+)>");
		assertEquals(
				"<([^ >]+)\\s+xmlns=\'([^\']*)\'><([^ >]+)\\s+id=\'([^\']*)\'>([^<]*)</([^ >]+)></([^ >]+)>",
				SmackChangeManager.pat.pattern());
	}

	public void testToXml() {
		final Memento<?> m = new PosMemento(new RockDouble<Pos>(), 1,
				new Point2D.Double(2, 3));
		assertEquals(s, SmackChangeManager.toXml(m));
	}
}

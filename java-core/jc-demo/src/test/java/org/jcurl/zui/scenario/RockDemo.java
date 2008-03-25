/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2008 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.jcurl.zui.scenario;

import java.awt.geom.AffineTransform;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockType.Pos;

import com.sun.scenario.scenegraph.JSGPanel;
import com.sun.scenario.scenegraph.SGGroup;
import com.sun.scenario.scenegraph.SGNode;
import com.sun.scenario.scenegraph.SGTransform;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
public class RockDemo {
	public static void main(final String[] args) {
		final JFrame f = new JFrame("Demo");
		f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		final JSGPanel panel = new JSGPanel();

		final AffineTransform at = new AffineTransform();
		at.translate(200, 400);
		at.scale(200, 200);

		final SGGroup content = new SGGroup();
		final Rock<Pos> rock = new RockDouble<Pos>(0.1, 0.2, 3);
		content.add(new SGRockFactory.Fancy(200).newInstance(3, true, rock));
		final SGNode ice = new SGIceFactory.Fancy().newInstance(content);
		panel.setScene(SGTransform.createAffine(at, ice));

		// TODO
		rock.setA(0);
		
		f.add(panel);
		f.setSize(400, 800);
		f.setVisible(true);
	}
}

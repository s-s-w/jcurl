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

package org.jcurl.demo.tactics;

import java.awt.geom.Rectangle2D;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.Unit;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.demo.tactics.ActionRegistry.JCAction;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
@ActionRegistry.JCMenu("&View")
public class MenuView {
	/** All from back to back */
	public static final Rectangle2D CompletePlus;
	/** House area plus 1 rock margin plus "out" rock space. */
	public static final Rectangle2D HousePlus;
	private static final Log log = JCLoggerFactory.getLogger(MenuView.class);
	/**
	 * Inter-hog area area plus house area plus 1 rock margin plus "out"
	 * rock space.
	 */
	private static final Rectangle2D SheetPlus;
	/** 12-foot circle plus 1 rock */
	private static final Rectangle2D TwelvePlus;
	static {
		final double r2 = 2 * RockProps.DEFAULT.getRadius();
		final double x = IceSize.SIDE_2_CENTER + r2;
		HousePlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE + r2),
				2 * x, IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
		final double c12 = r2 + Unit.f2m(6.0);
		TwelvePlus = new Rectangle2D.Double(-c12, -c12, 2 * c12, 2 * c12);
		SheetPlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_HOG
				+ IceSize.HOG_2_TEE + r2), 2 * x, IceSize.HOG_2_HOG
				+ IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
		CompletePlus = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE
				+ IceSize.HOG_2_HOG + IceSize.HACK_2_HOG + r2), 2 * x,
				IceSize.HOG_2_HOG + 2 * IceSize.HACK_2_HOG);
	}
	private Zoomable model = null;

	public Zoomable getModel() {
		return model;
	}

	public void setModel(final Zoomable model) {
		this.model = model;
		final boolean a = this.model != null;
		final ActionRegistry ah = ActionRegistry.getInstance();
		ah.findAction(this, "zoomActive").setEnabled(a);
		ah.findAction(this, "zoomHouse").setEnabled(a);
		ah.findAction(this, "zoomRink").setEnabled(a);
		ah.findAction(this, "zoomTwelve").setEnabled(a);
	}

	private void zoom(final Rectangle2D viewport) {
		if (getModel() != null)
			getModel().setZoom(viewport);
	}

	@JCAction(title = "&Active", idx = 40, accelerator = "CTRL-END")
	public void zoomActive() {
		zoom(SheetPlus);
	}

	@JCAction(title = "&House", idx = 10, accelerator = "HOME")
	public void zoomHouse() {
		zoom(HousePlus);
	}

	@JCAction(title = "&Complete", idx = 30, accelerator = "CTRL-HOME")
	public void zoomRink() {
		zoom(CompletePlus);
	}

	@JCAction(title = "1&2-Foot", idx = 20, accelerator = "END")
	public void zoomTwelve() {
		zoom(TwelvePlus);
	}
}

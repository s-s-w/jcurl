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

package org.jcurl.demo.tactics.old;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.Unit;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.demo.tactics.Zoomable;
import org.jcurl.demo.tactics.old.ActionRegistry.JCAction;

/**
 * @author <a href="mailto:m@jcurl.org">M. Rohrmoser </a>
 * @version $Id$
 */
@ActionRegistry.JCMenu("&View")
public class MenuView {
	/** All from back to back */
	public static final Rectangle2D CompletePlus;
	/** */
	private static final int DT = 200;
	/** House area plus 1 rock margin plus "out" rock space. */
	public static final Rectangle2D HousePlus;
	private static final Log log = JCLoggerFactory.getLogger(MenuView.class);
	/**
	 * Inter-hog area area plus house area plus 1 rock margin plus "out" rock
	 * space.
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

	private void pan(final double rx, final double ry, final int dt) {
		if (getModel() == null)
			return;
		final RectangularShape src = getModel().getZoom();
		src.setFrame(src.getX() + src.getWidth() * rx, src.getY()
				+ src.getHeight() * ry, src.getWidth(), src.getHeight());
		zoom(src, dt);
	}

	@JCAction(title = "Pan &East", idx = 90, accelerator = "LEFT")
	public void panEast() {
		pan(-0.2, 0, DT);
	}

	@JCAction(title = "Pan &North", idx = 70, accelerator = "DOWN", separated = true)
	public void panNorth() {
		pan(0, 0.2, DT);
	}

	@JCAction(title = "Pan &South", idx = 80, accelerator = "UP")
	public void panSouth() {
		pan(0, -0.2, DT);
	}

	@JCAction(title = "Pan &West", idx = 100, accelerator = "RIGHT")
	public void panWest() {
		pan(0.2, 0, DT);
	}

	public void setModel(final Zoomable model) {
		this.model = model;
		final boolean a = this.model != null;
		final ActionRegistry ah = ActionRegistry.getInstance();
		ah.findAction(this, "zoomActive").setEnabled(a);
		ah.findAction(this, "zoomHouse").setEnabled(a);
		ah.findAction(this, "zoomRink").setEnabled(a);
		ah.findAction(this, "zoomTwelve").setEnabled(a);
		ah.findAction(this, "zoomIn").setEnabled(a);
		ah.findAction(this, "zoomOut").setEnabled(a);
		ah.findAction(this, "panNorth").setEnabled(a);
		ah.findAction(this, "panSouth").setEnabled(a);
		ah.findAction(this, "panEast").setEnabled(a);
		ah.findAction(this, "panWest").setEnabled(a);
	}

	private void zoom(final Point2D center, final double ratio, final int dt) {
		if (getModel() == null)
			return;
		final RectangularShape src = getModel().getZoom();
		if (log.isDebugEnabled())
			log.debug(src);
		final double w = src.getWidth() * ratio;
		final double h = src.getHeight() * ratio;
		final double cx, cy;
		if (center == null) {
			cx = src.getCenterX();
			cy = src.getCenterY();
		} else {
			cx = center.getX();
			cy = center.getY();
		}
		zoom(new Rectangle2D.Double(cx - w / 2, cy - h / 2, Math.abs(w), Math
				.abs(h)), dt);
	}

	private void zoom(final RectangularShape viewport, final int dt) {
		if (getModel() == null)
			return;
		getModel().setZoom(viewport, dt);
	}

	@JCAction(title = "&Active", idx = 40, accelerator = "CTRL-END")
	public void zoomActive() {
		zoom(SheetPlus, -1);
	}

	@JCAction(title = "&House", idx = 10, accelerator = "HOME")
	public void zoomHouse() {
		zoom(HousePlus, -1);
	}

	@JCAction(title = "Zoom &In", idx = 50, accelerator = "ADD", separated = true)
	public void zoomIn() {
		zoom(null, 0.75, DT);
	}

	@JCAction(title = "Zoom &Out", idx = 60, accelerator = "SUBTRACT")
	public void zoomOut() {
		zoom(null, 1.25, DT);
	}

	@JCAction(title = "&Complete", idx = 30, accelerator = "CTRL-HOME")
	public void zoomRink() {
		zoom(CompletePlus, -1);
	}

	@JCAction(title = "1&2-Foot", idx = 20, accelerator = "END")
	public void zoomTwelve() {
		zoom(TwelvePlus, -1);
	}
}

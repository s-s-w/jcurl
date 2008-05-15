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
package org.jcurl.demo.zui;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.RockProps;
import org.jcurl.core.api.Unit;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.ui.DefaultBroomPromptModel;
import org.jcurl.zui.piccolo.BroomPromptSimple;
import org.jcurl.zui.piccolo.KeyboardZoom;
import org.jcurl.zui.piccolo.PIceFactory;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.activities.PInterpolatingActivity;
import edu.umd.cs.piccolo.util.PPaintContext;

public class BroomPromptDemo {
	/** All from back to back */
	static final Rectangle2D completeP;
	/** House area plus 1 rock margin plus "out" rock space. */
	static final Rectangle2D houseP;
	private static final Log log = JCLoggerFactory
			.getLogger(BroomPromptDemo.class);
	/**
	 * Inter-hog area area plus house area plus 1 rock margin plus "out" rock
	 * space.
	 */
	static final Rectangle2D sheetP;

	/** 12-foot circle plus 1 rock */
	static final Rectangle2D twelveP;

	static {
		final double r2 = 2 * RockProps.DEFAULT.getRadius();
		final double x = IceSize.SIDE_2_CENTER + r2;
		houseP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE + r2), 2 * x,
				IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
		final double c12 = r2 + Unit.f2m(6.0);
		twelveP = new Rectangle2D.Double(-c12, -c12, 2 * c12, 2 * c12);
		sheetP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_HOG
				+ IceSize.HOG_2_TEE + r2), 2 * x, IceSize.HOG_2_HOG
				+ IceSize.HOG_2_TEE + IceSize.BACK_2_TEE + 3 * r2 + 2 * r2);
		completeP = new Rectangle2D.Double(-x, -(IceSize.HOG_2_TEE
				+ IceSize.HOG_2_HOG + IceSize.HACK_2_HOG + r2), 2 * x,
				IceSize.HOG_2_HOG + 2 * IceSize.HACK_2_HOG);
	}

	private static PCamera animateToBounds(final PCamera c,
			final Rectangle2D r, final long duration) {
		final PInterpolatingActivity pi = c.animateViewToCenterBounds(r, true,
				duration);
		return c;
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				final JFrame application = new JFrame();
				application.setTitle("JCurl BroomPrompt");
				application
						.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				final PCanvas pc = new PCanvas();
				pc
						.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
				pc
						.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
				pc.getRoot().getDefaultInputManager().setKeyboardFocus(
						new KeyboardZoom(pc.getCamera()));
				pc.setBackground(new Color(0xE8E8FF));

				final PNode ice = new PIceFactory.Fancy().newInstance();
				pc.getLayer().addChild(ice);
				application.getContentPane().add(pc);
				application.setSize(500, 800);
				application.setVisible(true);
				animateToBounds(pc.getCamera(), twelveP, 500);
				final BroomPromptSimple bp;
				ice.addChild(bp = new BroomPromptSimple());
				final DefaultBroomPromptModel bpm;
				bp.setModel(bpm = new DefaultBroomPromptModel());
				bpm.addPropertyChangeListener(new PropertyChangeListener() {
					public void propertyChange(final PropertyChangeEvent evt) {
						// FIXME why doesn't fire this?
						log.info(evt);
					}
				});
				bpm.getSplitTimeMillis().addChangeListener(
						new ChangeListener() {
							public void stateChanged(final ChangeEvent e) {
								log.info(e);
							}
						});
				bpm.setIdx16(1);
				bpm.setOutTurn(false);
				bp
						.animateToPositionScaleRotation(1, 2, 1,
								-0.1 * Math.PI, 5000);
			}
		});
	}
}
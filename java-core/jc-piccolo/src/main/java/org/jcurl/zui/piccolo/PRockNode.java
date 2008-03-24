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

package org.jcurl.zui.piccolo;

import java.awt.Cursor;
import java.awt.event.InputEvent;
import java.awt.geom.Point2D;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.RockType.Pos;
import org.jcurl.core.ui.MessageExecutor.ForkableFixed;
import org.jcurl.core.ui.MessageExecutor.SwingEDT;

import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.event.PInputEventFilter;

/**
 * A single {@link Rock}s Zui representation base.
 * 
 * Maybe add a fully transparent circle to avoid the {@link PComposite}.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class PRockNode extends PComposite implements ChangeListener {
	/**
	 * Add drag-support to a {@link PRockNode}s. This is kinda controller
	 * view-&gt;model.
	 * 
	 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
	 * @version $Id:PPositionSetDrag.java 795 2008-03-19 13:40:42Z mrohrmoser $
	 */
	public static class DragHandler extends PBasicInputEventHandler {

		private static final Cursor CURSOR = new Cursor(Cursor.HAND_CURSOR);
		private Point2D previous = null;

		public DragHandler() {
			setEventFilter(new PInputEventFilter(InputEvent.BUTTON1_MASK));
		}

		@Override
		public void mouseDragged(final PInputEvent event) {
			pushChangeInternal(false, event);
		}

		@Override
		public void mouseEntered(final PInputEvent arg0) {
			super.mouseEntered(arg0);
			arg0.pushCursor(CURSOR);
		}

		@Override
		public void mouseExited(final PInputEvent arg0) {
			super.mouseExited(arg0);
			arg0.popCursor();
		}

		@Override
		public void mouseReleased(final PInputEvent event) {
			pushChangeInternal(true, event);
			previous = null;
		}

		/**
		 * Typical extension point - by default just update the rock's location.
		 * 
		 * @see Rock#p
		 * @see Point2D#setLocation(Point2D)
		 * @param isDrop
		 * @param node
		 * @param currentPos
		 * @param startPos
		 */
		protected void pushChange(final boolean isDrop, final PRockNode node,
				final Point2D currentPos, final Point2D startPos) {
			node.getRock().p().setLocation(currentPos);
		}

		private void pushChangeInternal(final boolean isDrop,
				final PInputEvent event) {
			final PRockNode node = (PRockNode) event.getPickedNode();
			final Point2D p = event.getPickedNode().getParent().globalToLocal(
					event.getPosition());
			// FIXME Add overlap/collission detection!
			if (previous == null)
				previous = p;
			else
			// any move at all?
			if (p.distanceSq(previous) < 1e-11)
				return;
			pushChange(isDrop, node, p, previous);
			event.setHandled(true);
		}
	}

	static final double EPSILON = 1e-11;
	static final Object INDEX16 = "index16";
	private static final long serialVersionUID = 4713843366445017130L;
	/** This is kinda controller model-&gt;view */
	private final transient ForkableFixed<SwingEDT> model2view = new ForkableFixed<SwingEDT>() {
		public void run() {
			if (getGlobalTranslation().distanceSq(getRock().p()) < EPSILON)
				return;
			// TODO check the turn
			setTransform(getRock().getAffineTransform());
		}
	};

	/**
	 * Init and wire up the model {@link Rock}.
	 * 
	 * @param idx8
	 * @param isDark
	 * @param rock
	 * @see Rock#addChangeListener(ChangeListener)
	 */
	public PRockNode(final int idx8, final boolean isDark, final Rock<Pos> rock) {
		addAttribute(Rock.class.getName(), rock);
		addAttribute(PRockNode.INDEX16, RockSet.toIdx16(isDark, idx8));
		setTransform(rock.getAffineTransform());
		rock.addChangeListener(this);
	}

	public int getIndex16() {
		return (Integer) getAttribute(PRockNode.INDEX16);
	}

	public Rock<Pos> getRock() {
		return (Rock<Pos>) getAttribute(Rock.class.getName());
	}

	public void stateChanged(final ChangeEvent e) {
		if (e.getSource() != getRock())
			return;
		model2view.fork();
	}
}

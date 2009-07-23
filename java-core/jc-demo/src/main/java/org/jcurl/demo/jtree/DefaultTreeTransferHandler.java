/*
 * jcurl java curling software framework http://www.jcurl.org
 * Copyright (C) 2005-2009 M. Rohrmoser
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
package org.jcurl.demo.jtree;

import java.awt.Point;
import java.awt.dnd.DnDConstants;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * http://forum.java.sun.com/thread.jspa?threadID=296255&start=0
 * 
 * @author <a href="http://forum.java.sun.com/profile.jspa?userID=82795">Deudeu</a>
 * @version $Id: DefaultTreeTransferHandler.java 776 2008-03-16 10:17:28Z
 *          mrohrmoser $
 */
public class DefaultTreeTransferHandler extends AbstractTreeTransferHandler {

	public DefaultTreeTransferHandler(final DNDTree tree, final int action) {
		super(tree, action, true);
	}

	@Override
	public boolean canPerformAction(final DNDTree target,
			final DefaultMutableTreeNode draggedNode, final int action,
			final Point location) {
		final TreePath pathTarget = target.getPathForLocation(location.x,
				location.y);
		if (pathTarget == null) {
			target.setSelectionPath(null);
			return false;
		}
		target.setSelectionPath(pathTarget);
		if (action == DnDConstants.ACTION_COPY)
			return true;
		else if (action == DnDConstants.ACTION_MOVE) {
			final DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) pathTarget
					.getLastPathComponent();
			if (draggedNode.isRoot() || parentNode == draggedNode.getParent()
					|| draggedNode.isNodeDescendant(parentNode))
				return false;
			else
				return true;
		} else
			return false;
	}

	@Override
	public boolean executeDrop(final DNDTree target,
			final DefaultMutableTreeNode draggedNode,
			final DefaultMutableTreeNode newParentNode, final int action) {
		if (action == DnDConstants.ACTION_COPY) {
			final DefaultMutableTreeNode newNode = DNDTree
					.makeDeepCopy(draggedNode);
			((DefaultTreeModel) target.getModel()).insertNodeInto(newNode,
					newParentNode, newParentNode.getChildCount());
			final TreePath treePath = new TreePath(newNode.getPath());
			target.scrollPathToVisible(treePath);
			target.setSelectionPath(treePath);
			return true;
		}
		if (action == DnDConstants.ACTION_MOVE) {
			draggedNode.removeFromParent();
			((DefaultTreeModel) target.getModel()).insertNodeInto(draggedNode,
					newParentNode, newParentNode.getChildCount());
			final TreePath treePath = new TreePath(draggedNode.getPath());
			target.scrollPathToVisible(treePath);
			target.setSelectionPath(treePath);
			return true;
		}
		return false;
	}
}
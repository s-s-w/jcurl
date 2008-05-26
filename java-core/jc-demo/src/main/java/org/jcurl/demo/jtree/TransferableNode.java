/*
 * jcurl java curling software framework http://www.jcurl.org
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
package org.jcurl.demo.jtree;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.Arrays;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * http://forum.java.sun.com/thread.jspa?threadID=296255&start=0
 * 
 * @author <a href="http://forum.java.sun.com/profile.jspa?userID=82795">Deudeu</a>
 * @version $Id$
 */
public class TransferableNode implements Transferable {
	public static final DataFlavor NODE_FLAVOR = new DataFlavor(
			DataFlavor.javaJVMLocalObjectMimeType, "Node");
	private final DataFlavor[] flavors = { NODE_FLAVOR };
	private final DefaultMutableTreeNode node;

	public TransferableNode(final DefaultMutableTreeNode nd) {
		node = nd;
	}

	public synchronized Object getTransferData(final DataFlavor flavor)
			throws UnsupportedFlavorException {
		if (flavor == NODE_FLAVOR)
			return node;
		else
			throw new UnsupportedFlavorException(flavor);
	}

	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	public boolean isDataFlavorSupported(final DataFlavor flavor) {
		return Arrays.asList(flavors).contains(flavor);
	}
}
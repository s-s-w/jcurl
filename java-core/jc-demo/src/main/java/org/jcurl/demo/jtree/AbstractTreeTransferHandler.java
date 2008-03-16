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
package org.jcurl.demo.jtree;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * http://forum.java.sun.com/thread.jspa?threadID=296255&start=0
 * 
 * @author <a href="http://forum.java.sun.com/profile.jspa?userID=82795">Deudeu</a>
 * @version $Id$
 */
public abstract class AbstractTreeTransferHandler implements
        DragGestureListener, DragSourceListener, DropTargetListener {

    private static DefaultMutableTreeNode draggedNode;
    private static BufferedImage image = null; // buff image
    private DefaultMutableTreeNode draggedNodeParent;
    private final DragSource dragSource; // dragsource
    private final boolean drawImage;
    private final DropTarget dropTarget; // droptarget
    private final Rectangle rect2D = new Rectangle();
    private final DNDTree tree;

    protected AbstractTreeTransferHandler(final DNDTree tree, final int action,
            final boolean drawIcon) {
        this.tree = tree;
        drawImage = drawIcon;
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(tree, action, this);
        dropTarget = new DropTarget(tree, action, this);
    }

    public abstract boolean canPerformAction(DNDTree target,
            DefaultMutableTreeNode draggedNode, int action, Point location);

    private final void clearImage() {
        tree.paintImmediately(rect2D.getBounds());
    }

    /* Methods for DragSourceListener */
    public void dragDropEnd(final DragSourceDropEvent dsde) {
        if (dsde.getDropSuccess()
                && dsde.getDropAction() == DnDConstants.ACTION_MOVE
                && draggedNodeParent != null)
            ((DefaultTreeModel) tree.getModel())
                    .nodeStructureChanged(draggedNodeParent);
    }

    public final void dragEnter(final DragSourceDragEvent dsde) {
        final int action = dsde.getDropAction();
        if (action == DnDConstants.ACTION_COPY)
            dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
        else if (action == DnDConstants.ACTION_MOVE)
            dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
        else
            dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
    }

    public final void dragEnter(final DropTargetDragEvent dtde) {
        final Point pt = dtde.getLocation();
        final int action = dtde.getDropAction();
        if (drawImage)
            paintImage(pt);
        if (canPerformAction(tree, draggedNode, action, pt))
            dtde.acceptDrag(action);
        else
            dtde.rejectDrag();
    }

    public final void dragExit(final DragSourceEvent dse) {
        dse.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
    }

    /* Methods for DropTargetListener */
    public final void dragExit(final DropTargetEvent dte) {
        if (drawImage)
            clearImage();
    }

    /* Methods for DragGestureListener */
    public final void dragGestureRecognized(final DragGestureEvent dge) {
        final TreePath path = tree.getSelectionPath();
        if (path != null) {
            draggedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            draggedNodeParent = (DefaultMutableTreeNode) draggedNode
                    .getParent();
            if (drawImage) {
                final Rectangle pathBounds = tree.getPathBounds(path); // getpathbounds
                                                                        // of
                                                                        // selectionpath
                final JComponent lbl = (JComponent) tree.getCellRenderer()
                        .getTreeCellRendererComponent(
                                tree,
                                draggedNode,
                                false,
                                tree.isExpanded(path),
                                ((DefaultTreeModel) tree.getModel())
                                        .isLeaf(path.getLastPathComponent()),
                                0, false);// returning the label
                lbl.setBounds(pathBounds);// setting bounds to lbl
                image = new BufferedImage(lbl.getWidth(), lbl.getHeight(),
                        java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE);// buffered
                                                                        // image
                                                                        // reference
                                                                        // passing
                                                                        // the
                                                                        // label's
                                                                        // ht
                                                                        // and
                                                                        // width
                final Graphics2D graphics = image.createGraphics();// creating
                                                                    // the
                                                                    // graphics
                                                                    // for
                                                                    // buffered
                                                                    // image
                graphics.setComposite(AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER, 0.5f)); // Sets the
                                                            // Composite for the
                                                            // Graphics2D
                                                            // context
                lbl.setOpaque(false);
                lbl.paint(graphics); // painting the graphics to label
                graphics.dispose();
            }
            dragSource.startDrag(dge, DragSource.DefaultMoveNoDrop, image,
                    new Point(0, 0), new TransferableNode(draggedNode), this);
        }
    }

    public final void dragOver(final DragSourceDragEvent dsde) {
        final int action = dsde.getDropAction();
        if (action == DnDConstants.ACTION_COPY)
            dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
        else if (action == DnDConstants.ACTION_MOVE)
            dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
        else
            dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
    }

    public final void dragOver(final DropTargetDragEvent dtde) {
        final Point pt = dtde.getLocation();
        final int action = dtde.getDropAction();
        tree.autoscroll(pt);
        if (drawImage)
            paintImage(pt);
        if (canPerformAction(tree, draggedNode, action, pt))
            dtde.acceptDrag(action);
        else
            dtde.rejectDrag();
    }

    public final void drop(final DropTargetDropEvent dtde) {
        try {
            if (drawImage)
                clearImage();
            final int action = dtde.getDropAction();
            final Transferable transferable = dtde.getTransferable();
            final Point pt = dtde.getLocation();
            if (transferable
                    .isDataFlavorSupported(TransferableNode.NODE_FLAVOR)
                    && canPerformAction(tree, draggedNode, action, pt)) {
                final TreePath pathTarget = tree.getPathForLocation(pt.x, pt.y);
                final DefaultMutableTreeNode node = (DefaultMutableTreeNode) transferable
                        .getTransferData(TransferableNode.NODE_FLAVOR);
                final DefaultMutableTreeNode newParentNode = (DefaultMutableTreeNode) pathTarget
                        .getLastPathComponent();
                if (executeDrop(tree, node, newParentNode, action)) {
                    dtde.acceptDrop(action);
                    dtde.dropComplete(true);
                    return;
                }
            }
            dtde.rejectDrop();
            dtde.dropComplete(false);
        } catch (final Exception e) {
            System.out.println(e);
            dtde.rejectDrop();
            dtde.dropComplete(false);
        }
    }

    public final void dropActionChanged(final DragSourceDragEvent dsde) {
        final int action = dsde.getDropAction();
        if (action == DnDConstants.ACTION_COPY)
            dsde.getDragSourceContext().setCursor(DragSource.DefaultCopyDrop);
        else if (action == DnDConstants.ACTION_MOVE)
            dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveDrop);
        else
            dsde.getDragSourceContext().setCursor(DragSource.DefaultMoveNoDrop);
    }

    public final void dropActionChanged(final DropTargetDragEvent dtde) {
        final Point pt = dtde.getLocation();
        final int action = dtde.getDropAction();
        if (drawImage)
            paintImage(pt);
        if (canPerformAction(tree, draggedNode, action, pt))
            dtde.acceptDrag(action);
        else
            dtde.rejectDrag();
    }

    public abstract boolean executeDrop(DNDTree tree,
            DefaultMutableTreeNode draggedNode,
            DefaultMutableTreeNode newParentNode, int action);

    private final void paintImage(final Point pt) {
        tree.paintImmediately(rect2D.getBounds());
        rect2D.setRect((int) pt.getX(), (int) pt.getY(), image.getWidth(),
                image.getHeight());
        tree.getGraphics().drawImage(image, (int) pt.getX(), (int) pt.getY(),
                tree);
    }
}
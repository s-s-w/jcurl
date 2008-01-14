/*
 * jcurl curling simulation framework http://www.jcurl.org
 * Copyright (C) 2005-2007 M. Rohrmoser
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

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DnDConstants;
import java.util.Enumeration;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/**
 * http://forum.java.sun.com/thread.jspa?threadID=296255&start=0
 * 
 * @author <a href="http://forum.java.sun.com/profile.jspa?userID=82795">Deudeu</a>
 * @version $Id$
 */
public class DNDTree extends JTree {

    private static final long serialVersionUID = 4923607215510457402L;

    public static DefaultMutableTreeNode createTree() {
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        final DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("node1");
        final DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("node2");
        root.add(node1);
        root.add(node2);
        node1.add(new DefaultMutableTreeNode("sub1_1"));
        node1.add(new DefaultMutableTreeNode("sub1_2"));
        node1.add(new DefaultMutableTreeNode("sub1_3"));
        node2.add(new DefaultMutableTreeNode("sub2_1"));
        node2.add(new DefaultMutableTreeNode("sub2_2"));
        node2.add(new DefaultMutableTreeNode("sub2_3"));
        return root;
    }

    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            final JFrame frame = new JFrame();
            final Container contentPane = frame.getContentPane();
            contentPane.setLayout(new GridLayout(1, 2));
            final DefaultMutableTreeNode root1 = DNDTree.createTree();
            final DNDTree tree1 = new DNDTree(root1);
            final DefaultMutableTreeNode root2 = DNDTree.createTree();
            final DNDTree tree2 = new DNDTree(root2);
            contentPane.add(new JScrollPane(tree1));
            contentPane.add(new JScrollPane(tree2));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setVisible(true);
        } catch (final Exception e) {
            System.out.println(e);
        }
    }

    public static DefaultMutableTreeNode makeDeepCopy(
            final DefaultMutableTreeNode node) {
        final DefaultMutableTreeNode copy = new DefaultMutableTreeNode(node
                .getUserObject());
        for (final Enumeration e = node.children(); e.hasMoreElements();)
            copy.add(makeDeepCopy((DefaultMutableTreeNode) e.nextElement()));
        return copy;
    }

    Insets autoscrollInsets = new Insets(20, 20, 20, 20); // insets

    public DNDTree(final DefaultMutableTreeNode root) {
        setAutoscrolls(true);
        final DefaultTreeModel treemodel = new DefaultTreeModel(root);
        setModel(treemodel);
        setRootVisible(true);
        setShowsRootHandles(false);// to show the root icon
        getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION); // set
                                                                                        // single
                                                                                        // selection
                                                                                        // for
                                                                                        // the
                                                                                        // Tree
        setEditable(false);
        new DefaultTreeTransferHandler(this, DnDConstants.ACTION_COPY_OR_MOVE);
    }

    public void autoscroll(final Point cursorLocation) {
        final Insets insets = getAutoscrollInsets();
        final Rectangle outer = getVisibleRect();
        final Rectangle inner = new Rectangle(outer.x + insets.left, outer.y
                + insets.top, outer.width - (insets.left + insets.right),
                outer.height - (insets.top + insets.bottom));
        if (!inner.contains(cursorLocation)) {
            final Rectangle scrollRect = new Rectangle(cursorLocation.x
                    - insets.left, cursorLocation.y - insets.top, insets.left
                    + insets.right, insets.top + insets.bottom);
            scrollRectToVisible(scrollRect);
        }
    }

    public Insets getAutoscrollInsets() {
        return autoscrollInsets;
    }
}
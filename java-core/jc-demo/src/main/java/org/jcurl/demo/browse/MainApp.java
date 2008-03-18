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
package org.jcurl.demo.browse;

import javax.swing.JFrame;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.PositionSet;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.SpeedSet;
import org.jcurl.core.api.Unit;
import org.jcurl.core.helpers.AnnoHelper;
import org.jcurl.core.impl.CollissionSpin;
import org.jcurl.core.impl.CurlerDenny;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.impl.NewtonCollissionDetector;
import org.jcurl.core.io.IODocument;
import org.jcurl.core.io.IOTrajectories;

public class MainApp extends JFrame {

    static class JCTree extends JTree {
        private static final long serialVersionUID = -5677436927105230582L;

        public JCTree() {
            this(new DefaultMutableTreeNode("Parent"));
        }

        private JCTree(final DefaultMutableTreeNode root) {
            super(new DefaultTreeModel(root));
            setRootVisible(true);
            final DefaultMutableTreeNode n = new DefaultMutableTreeNode(
                    "file://", true);
            root.add(n);
            n.add(new DefaultMutableTreeNode("http://"));
            root.add(new DefaultMutableTreeNode("jdbc://"));
            setRootVisible(false);
            makeVisible(new TreePath(new Object[] { root, n }));
            setEditable(true);
        }
    }

    static class JCTreeModel extends DefaultTreeModel {
        private static final long serialVersionUID = 7383682967060505192L;

        public JCTreeModel() {
            this(new DefaultMutableTreeNode("Parent"));
        }

        private JCTreeModel(final DefaultMutableTreeNode root) {
            super(root);
        }

    }

    private static final long serialVersionUID = 1295540052650355258L;

    static CurveManager initHammy(CurveManager te) {
        if (te == null)
            te = new CurveManager();
        te.setCollider(new CollissionSpin(0.5, 0.0));
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setCurler(new CurlerDenny(24, 1));
        te.setInitialPos(PositionSet.allOut());
        te.setInitialSpeed(new SpeedSet(new RockDouble()));
        te.getAnnotations().put(AnnoHelper.HammerK, AnnoHelper.HammerVDark);
        te.getAnnotations().put(AnnoHelper.DarkTeamK, "Scotland");
        te.getAnnotations().put(AnnoHelper.LightTeamK, "Canada");
        te.getAnnotations().put(AnnoHelper.GameK, "Semifinal");
        te.getAnnotations().put(AnnoHelper.EventK,
                "World Curling Championships");
        te.getAnnotations().put(AnnoHelper.DateK, "1992");
        te.getAnnotations().put(AnnoHelper.LocationK, "Garmisch");
        initHammy(te.getInitialPos(), te.getInitialSpeed());
        return te;
    }

    static void initHammy(final PositionSet p, final SpeedSet s) {
        PositionSet.allOut(p);
        // te.getInitialPos().getLight(1-1).setLocation(
        p.getLight(2 - 1).setLocation(Unit.f2m(-1.170732), Unit.f2m(15.365854),
                0);
        p.getLight(3 - 1)
                .setLocation(Unit.f2m(0.292683), Unit.f2m(8.780488), 0);
        p.getLight(4 - 1).setLocation(Unit.f2m(2.195122), Unit.f2m(12), 0);
        p.getLight(5 - 1)
                .setLocation(Unit.f2m(1.463415), Unit.f2m(5.707317), 0);
        p.getLight(6 - 1).setLocation(Unit.f2m(1.463415), Unit.f2m(-2.780488),
                0);
        p.getLight(7 - 1).setLocation(Unit.f2m(-0.439024), Unit.f2m(-5.560976),
                0);
        p.getLight(8 - 1).setLocation(Unit.f2m(-1.756098), Unit.f2m(-1.609756),
                0);
        // p.getDark(1-1).setLocation(
        // p.getDark(2-1).setLocation(
        p.getDark(3 - 1)
                .setLocation(Unit.f2m(0.878049), Unit.f2m(14.341463), 0);
        p.getDark(4 - 1).setLocation(Unit.f2m(-2.634146), Unit.f2m(13.170732),
                0);
        p.getDark(5 - 1)
                .setLocation(Unit.f2m(4.536585), Unit.f2m(-0.439024), 0);
        p.getDark(6 - 1).setLocation(Unit.f2m(0.731707), Unit.f2m(-3.95122), 0);
        p.getDark(7 - 1).setLocation(Unit.f2m(-2.780488), Unit.f2m(-4.390244),
                0);
        p.getDark(8 - 1).setLocation(Unit.f2m(3.89991), IceSize.HOG_2_TEE, 0);
        RockSet.allZero(s);
        s.getDark(8 - 1).setLocation(0, -3, 100 * Math.PI / 180);

        p.getDark(8 - 1).setLocation(0, IceSize.FAR_HACK_2_TEE, 0);
        s.getDark(8 - 1).setLocation(0.188, -3, -100 * Math.PI / 180);

        p.getDark(8 - 1).setLocation(0, IceSize.FAR_HACK_2_TEE, 0);
        s.getDark(8 - 1).setLocation(0.1785, -4, -100 * Math.PI / 180);
        p.fireStateChanged();
        s.fireStateChanged();
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainApp().setVisible(true);
            }
        });
    }

    static IODocument newHammy() {
        final IODocument doc = new IODocument();
        doc.annotations().put("t", "document");
        final IOTrajectories root = new IOTrajectories();
        doc.setRoot(root);
        root.annotations().put("t", "World Curling Championships");

        final IOTrajectories c1 = new IOTrajectories();
        c1.annotations().put("t", "Men");
        root.children().add(c1);

        final IOTrajectories c2 = new IOTrajectories();
        c2.annotations().put("t", "Garmisch 1992");
        c1.children().add(c2);

        final IOTrajectories c3 = new IOTrajectories();
        c3.annotations().put("t", "Semi Final SCO - CAN");
        c2.children().add(c3);

        c3.trajectories().add(initHammy(null));
        return doc;
    }

    public MainApp() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(new JCTree(), "West");
        pack();
        setSize(600, 800);
        // setVisible(true);
        setVisible(false);
    }
}

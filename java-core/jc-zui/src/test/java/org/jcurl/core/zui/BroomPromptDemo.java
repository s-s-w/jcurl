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
package org.jcurl.core.zui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.helpers.MutableObject;
import org.jcurl.core.helpers.Unit;
import org.jcurl.core.log.JCLoggerFactory;

import edu.umd.cs.piccolo.PCamera;
import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.activities.PInterpolatingActivity;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolo.util.PPickPath;

public class BroomPromptDemo {
    /** Piccolo View + Controller for {@link BroomPromptModel}s. */
    public static class BroomPrompt03 extends PNode implements
            PropertyChangeListener {
        private static final Color cold = Color.BLUE;
        private static final Color dark = Color.RED;
        private static final Color hot = Color.RED;
        private static final Color light = Color.YELLOW;
        private static final Log lo = JCLoggerFactory
                .getLogger(BroomPrompt03.class);
        private static final long serialVersionUID = 3115716478135484000L;
        private final PNode handle;
        private BroomPromptModel model;
        private final PNode pie;
        private final float slideMax;
        private final PPath slider;

        public BroomPrompt03() {
            this(null);
        }

        public BroomPrompt03(final BroomPromptModel model) {
            final int pieAngle = 180;
            final Stroke st = new BasicStroke(0.01f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER);
            final Color sp = Color.BLACK;
            final Color bgc = new Color(1, 1, 1, 0.65f);
            // final Font fo = new Font("SansSerif", Font.BOLD, 1);
            final float outer = RockProps.DEFAULT.getRadius();
            slideMax = 5 * outer;
            final float inner = 0.5F * outer;
            setPickable(false);
            final BroomPrompt03 self = this;
            handle = new PNode() {
                private static final long serialVersionUID = -7641452321842902940L;

                /**
                 * Return true if this node or any pickable descendends are
                 * picked. If a pick occurs the pickPath is modified so that
                 * this node is always returned as the picked node, event if it
                 * was a decendent node that initialy reported the pick.
                 * 
                 * @see PComposite
                 */
                @Override
                public boolean fullPick(final PPickPath pickPath) {
                    if (super.fullPick(pickPath)) {
                        PNode picked = pickPath.getPickedNode();
                        // this code won't work with internal cameras, because
                        // it doesn't pop
                        // the cameras view transform.
                        while (picked != self) {
                            pickPath.popTransform(picked
                                    .getTransformReference(false));
                            pickPath.popNode(picked);
                            picked = pickPath.getPickedNode();
                        }
                        return true;
                    }
                    return false;
                }
            };
            { // opaque Background
                float f = 1.2f;
                final PNode bg = node(new Arc2D.Float(-f * outer, -f * outer, 2
                        * f * outer, 2 * f * outer, 0, 360, Arc2D.OPEN), null,
                        null);
                bg.setPaint(bgc);
                bg.setPickable(true);
                handle.addChild(bg);
            }
            { // Cross-hair circles and pie
                final int off = 90;
                final int pieOff = 180;
                // (1/4+angle) pie:
                pie = node(new Arc2D.Float(-outer, -outer, 2 * outer,
                        2 * outer, off - pieOff, pieAngle, Arc2D.PIE), null,
                        null);
                handle.addChild(pie);
                // (3/4) inner circle:
                handle
                        .addChild(node(new Arc2D.Float(-inner, -inner,
                                2 * inner, 2 * inner, off, pieOff + pieAngle,
                                Arc2D.OPEN), st, sp));
                // (3/4+angle) outer circle:
                handle.addChild(node(new Arc2D.Float(-outer, -outer, 2 * outer,
                        2 * outer, off, pieOff + pieAngle - 12, Arc2D.OPEN),
                        st, sp));
                // arrow:
                final float f = outer / 10;
                final PPath s = node(createArrowHead(f, 3 * f, 0.5f * f), null,
                        null);
                s.setPaint(sp);
                final double ar = Math.PI * (off + pieAngle) / 180.0;
                s.translate(-outer * Math.cos(ar), outer * Math.sin(ar));
                s.rotate(Math.PI * (90 - off - pieAngle + 8) / 180.0);
                handle.addChild(s);
                this.addChild(handle);
            }
            { // y-axis:
                handle.addChild(node(new Line2D.Float(0, 1.2f * outer, 0, -5
                        * outer), st, sp));
                // x-axis:
                handle.addChild(node(new Line2D.Float(-1.2f * outer, 0,
                        1.2f * outer, 0), st, sp));
            }
            { // slider
                slider = new PPath(createSlider(0.4f * outer), st);
                slider.setStrokePaint(sp);
                slider.setPickable(true);
                this.addChild(slider);
            }
            // Set up Event handling
            addInputEventListener(new PDragEventHandler() {
                /** double-click: flip handle */
                @Override
                public void mouseClicked(final PInputEvent arg0) {
                    super.mouseClicked(arg0);
                    if (arg0.getClickCount() > 1) {
                        arg0.setHandled(true);
                        getModel().setOutTurn(!getModel().getOutTurn());
                    }
                }

                /** drag/move */
                @Override
                public void mouseDragged(final PInputEvent arg0) {
                    arg0.setHandled(true);
                    getModel().setBroom(
                            arg0.getPositionRelativeTo(self.getParent()));
                }
            });
            slider.addInputEventListener(new PDragEventHandler() {
                /** adjust the slider */
                @Override
                public void mouseDragged(final PInputEvent arg0) {
                    arg0.setHandled(true);
                    final Point2D p = arg0.getPositionRelativeTo(self);
                    self.getModel().setSlider(-(float) p.getY() / slideMax);
                }
            });
            setModel(model);
        }

        public BroomPromptModel getModel() {
            return model;
        }

        public void propertyChange(final PropertyChangeEvent evt) {
            lo.debug(evt.getPropertyName());
            if ("broom".equals(evt.getPropertyName()))
                setBroom((Point2D) evt.getNewValue());
            else if ("idx16".equals(evt.getPropertyName()))
                setIdx16((Integer) evt.getNewValue());
            else if ("outTurn".equals(evt.getPropertyName()))
                setOutTurn((Boolean) evt.getNewValue());
            else if ("slider".equals(evt.getPropertyName()))
                setSlider((Double) evt.getNewValue());
        }

        /** adjust position + rotation */
        private void setBroom(final Point2D b) {
            final AffineTransform t = getTransformReference(true);
            t.setToIdentity();
            t.translate(b.getX(), b.getY());
            t.rotate(Math.atan2(b.getX(), IceSize.HACK_2_HOG
                    + IceSize.FAR_HOG_2_TEE - b.getY()));
            invalidateFullBounds();
            invalidatePaint();
        }

        /** adjust Color */
        private void setIdx16(final int i) {
            if (i < 0 || i >= RockSet.ROCKS_PER_SET)
                pie.setPaint(null);
            else if (i % 2 == 0)
                pie.setPaint(dark);
            else
                pie.setPaint(light);
            pie.invalidatePaint();
        }

        public void setModel(final BroomPromptModel model) {
            if (this.model != null)
                this.model.removePropertyChangeListener(this);
            this.model = model == null ? new BroomPromptModel() : model;
            setBroom(this.model.getBroom());
            setIdx16(this.model.getIdx16());
            setOutTurn(this.model.getOutTurn());
            setSlider(this.model.getSlider());
            this.model.addPropertyChangeListener(this);
        }

        private void setOutTurn(final boolean ot) {
            handle.getTransformReference(true).setToScale(ot ? -1 : 1, 1);
            handle.invalidatePaint();
        }

        private void setSlider(final double s) {
            slider.setPaint(interpolateRGB(cold, hot, s));
            slider.getTransformReference(true).setToTranslation(0,
                    -slideMax * s);
            slider.invalidateFullBounds();
            slider.invalidatePaint();
        }
    }

    public static class BroomPromptModel extends MutableObject {
        private static final long serialVersionUID = 4808528753885429987L;
        private Point2D broom = new Point2D.Float(0, 0);
        private int idx16 = -1;
        private boolean outTurn = true;
        private float slider = 0.5f;

        @Override
        public boolean equals(final Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final BroomPromptModel other = (BroomPromptModel) obj;
            if (broom == null) {
                if (other.broom != null)
                    return false;
            } else if (!broom.equals(other.broom))
                return false;
            if (idx16 != other.idx16)
                return false;
            if (outTurn != other.outTurn)
                return false;
            if (Float.floatToIntBits(slider) != Float
                    .floatToIntBits(other.slider))
                return false;
            return true;
        }

        public Point2D getBroom() {
            return broom;
        }

        public int getIdx16() {
            return idx16;
        }

        public boolean getOutTurn() {
            return outTurn;
        }

        public float getSlider() {
            return slider;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + (broom == null ? 0 : broom.hashCode());
            result = prime * result + idx16;
            result = prime * result + (outTurn ? 1231 : 1237);
            result = prime * result + Float.floatToIntBits(slider);
            return result;
        }

        public void setBroom(final Point2D broom) {
            firePropertyChange("broom", this.broom, broom);
            this.broom = broom;
        }

        public void setIdx16(int idx16) {
            if (idx16 < -1)
                idx16 = -1;
            if (idx16 >= RockSet.ROCKS_PER_SET)
                idx16 = RockSet.ROCKS_PER_SET - 1;
            firePropertyChange("idx16", this.idx16, idx16);
            this.idx16 = idx16;
        }

        public void setOutTurn(final boolean outTurn) {
            firePropertyChange("outTurn", this.outTurn, outTurn);
            this.outTurn = outTurn;
        }

        public void setSlider(float slider) {
            if (slider < 0)
                slider = 0;
            if (slider > 1)
                slider = 1;
            firePropertyChange("slider", this.slider, slider);
            this.slider = slider;
        }
    }

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

    /**
     * Pointing along the positive X-axis, ending at 0,0
     * 
     * @param wingWidth
     * @param length
     * @param tail
     */
    static Shape createArrowHead(final float wingWidth,
            final float length, final float tail) {
        final GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        gp.lineTo(-length, wingWidth);
        gp.lineTo(-length + tail, 0);
        gp.lineTo(-length, -wingWidth);
        gp.closePath();
        return gp;
    }

    static PNode createBroomPrompt01(final Paint sp) {
        final float ro = RockProps.DEFAULT.getRadius();
        final float ri = 0.5F * ro;
        final Font fo = new Font("SansSerif", Font.BOLD, 1);
        final PNode bp = new PNode(); // PComposite();
        bp.setPickable(true);
        final Stroke st = new BasicStroke(0.01f);

        final PNode handle = new PNode();
        { // opaque Background
            float f = 1.0f;
            final PNode bg = node(new Arc2D.Float(-f * ro, -f * ro, 2 * f * ro,
                    2 * f * ro, 0, 360, Arc2D.OPEN), null, null);
            bg.setPaint(new Color(1, 1, 1, 0.65f));
            handle.addChild(bg);
        }
        {
            final int angle = 75;
            // outturn: handle.setTransform(AffineTransform.getScaleInstance(-1,
            // 1));
            // (partial) inner circle:
            handle.addChild(node(new Arc2D.Float(-ri, -ri, 2 * ri, 2 * ri, 0,
                    270, Arc2D.OPEN), st, sp));
            // (partial) outer circle:
            handle.addChild(node(new Arc2D.Float(-ro, -ro, 2 * ro, 2 * ro, 0,
                    270 + angle, Arc2D.OPEN), st, sp));
            // arrow:
            final float f = ro / 20;
            final PPath s = node(createArrowHead(f, 3 * f, 0.5f * f), st, sp);
            double ar = Math.PI * (angle + 6) / 180;
            s.translate(ro * Math.sin(ar), ro * Math.cos(ar));
            ar = Math.PI * angle / 180;
            s.rotate(-ar);
            handle.addChild(s);
            bp.addChild(handle);
        }
        {
            // y-axis:
            bp
                    .addChild(node(new Line2D.Float(0, 1.2f * ro, 0, -5 * ro),
                            st, sp));
            // x-axis:
            bp.addChild(node(new Line2D.Float(-1.2f * ro, 0, 1.2f * ro, 0), st,
                    sp));
        }
        {
            // slider
            final float f = 3.5f / 5.0f;
            final PPath s = node(createSlider(0.4f * ro), null, null);
            s.setPaint(interpolateRGB(Color.BLUE, Color.RED, f));
            s.translate(0, -5 * f * ro);
            bp.addChild(s);
        }
        return bp;
    }

    private static Shape createSlider(final float f) {
        final GeneralPath gp = new GeneralPath();
        gp.moveTo(0, 0);
        gp.lineTo(f, f);
        gp.lineTo(4 * f, f);
        gp.lineTo(4 * f, -f);
        gp.lineTo(f, -f);
        gp.lineTo(-f, f);
        gp.lineTo(-4 * f, f);
        gp.lineTo(-4 * f, -f);
        gp.lineTo(-f, -f);
        gp.closePath();
        return gp;
    }

    private static float interpolate(final float a, final float b,
            final double ratio) {
        return (float) (a + ratio * (b - a));
    }

    private static int interpolate(final int a, final int b, final double ratio) {
        return (int) (a + ratio * (b - a));
    }

    private static Color interpolateHSB(final Color a, final Color b,
            final double ratio) {
        final float[] _a = new float[3];
        final float[] _b = new float[3];
        Color.RGBtoHSB(a.getRed(), a.getGreen(), a.getBlue(), _a);
        Color.RGBtoHSB(b.getRed(), b.getGreen(), b.getBlue(), _b);

        return new Color(Color.HSBtoRGB(interpolate(_a[0], _b[0], ratio),
                interpolate(_a[1], _b[1], ratio), interpolate(_a[2], _b[2],
                        ratio)));
    }

    private static Color interpolateRGB(final Color a, final Color b,
            final double ratio) {
        return new Color(interpolate(a.getRed(), b.getRed(), ratio),
                interpolate(a.getGreen(), b.getGreen(), ratio), interpolate(a
                        .getBlue(), b.getBlue(), ratio));
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
                final BroomPromptModel bpm;
                final PNode bp;
                ice.addChild(bp = new BroomPrompt03(
                        bpm = new BroomPromptModel()));
                bpm.setIdx16(1);
                bpm.setOutTurn(false);
                bp
                        .animateToPositionScaleRotation(1, 2, 1,
                                -0.1 * Math.PI, 5000);
            }
        });
    }

    private static PPath node(final Shape sh, final Stroke st, final Paint sp) {
        final PPath s = new PPath(sh);
        s.setStroke(st);
        s.setStrokePaint(sp);
        s.setPickable(false);
        return s;
    }
}
package org.jcurl.core.zui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.commons.logging.Log;
import org.jcurl.core.base.IceSize;
import org.jcurl.core.base.RockProps;
import org.jcurl.core.base.RockSet;
import org.jcurl.core.log.JCLoggerFactory;
import org.jcurl.core.model.BroomPromptModel;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PDragEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.util.PPaintContext;
import edu.umd.cs.piccolo.util.PPickPath;

/** Piccolo View + Controller for {@link BroomPromptModel}s. */
public class BroomPromptSimple extends PNode implements PropertyChangeListener {
    private static final Cursor CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private static final Color dark = Color.RED;
    private static final Color fast = Color.RED;
    private static final Color light = Color.YELLOW;
    private static final Log log = JCLoggerFactory
            .getLogger(BroomPromptSimple.class);
    private static final double scale0 = 0;
    private static final long serialVersionUID = 3115716478135484000L;

    private static final Color slow = Color.BLUE;

    /**
     * Pointing along the positive X-axis, ending at 0,0
     * 
     * @param wingWidth
     * @param length
     * @param tail
     */
    static Shape createArrowHead(final float wingWidth, final float length,
            final float tail) {
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
                    2 * f * ro, 0, 360, Arc2D.OPEN), null, null, scale0);
            bg.setPaint(new Color(1, 1, 1, 0.65f));
            handle.addChild(bg);
        }
        {
            final int angle = 75;
            // outturn: handle.setTransform(AffineTransform.getScaleInstance(-1,
            // 1));
            // (partial) inner circle:
            handle.addChild(node(new Arc2D.Float(-ri, -ri, 2 * ri, 2 * ri, 0,
                    270, Arc2D.OPEN), st, sp, scale0));
            // (partial) outer circle:
            handle.addChild(node(new Arc2D.Float(-ro, -ro, 2 * ro, 2 * ro, 0,
                    270 + angle, Arc2D.OPEN), st, sp, scale0));
            // arrow:
            final float f = ro / 20;
            final PPath s = node(createArrowHead(f, 3 * f, 0.5f * f), st, sp,
                    scale0);
            double ar = Math.PI * (angle + 6) / 180;
            s.translate(ro * Math.sin(ar), ro * Math.cos(ar));
            ar = Math.PI * angle / 180;
            s.rotate(-ar);
            handle.addChild(s);
            bp.addChild(handle);
        }
        {
            // y-axis:
            bp.addChild(node(new Line2D.Float(0, 1.2f * ro, 0, -5 * ro), st,
                    sp, scale0));
            // x-axis:
            bp.addChild(node(new Line2D.Float(-1.2f * ro, 0, 1.2f * ro, 0), st,
                    sp, scale0));
        }
        {
            // slider
            final float f = 3.5f / 5.0f;
            final PPath s = node(createSlider(0.4f * ro), null, null, scale0);
            s.setPaint(interpolateRGB(Color.BLUE, Color.RED, f));
            s.translate(0, -5 * f * ro);
            bp.addChild(s);
        }
        return bp;
    }

    static Shape createSlider(final float f) {
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

    static float interpolate(final float a, final float b, final double ratio) {
        return (float) (a + ratio * (b - a));
    }

    static int interpolate(final int a, final int b, final double ratio) {
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

    static Color interpolateRGB(final Color a, final Color b, final double ratio) {
        return new Color(interpolate(a.getRed(), b.getRed(), ratio),
                interpolate(a.getGreen(), b.getGreen(), ratio), interpolate(a
                        .getBlue(), b.getBlue(), ratio));
    }

    /**
     * @param sh
     * @param st
     * @param sp
     * @param minScale
     *            Threshold for <a
     *            href="http://www.cs.umd.edu/hcil/jazz/learn/patterns.shtml#Semantic%20Zooming">semantic
     *            zooming</a>
     */
    static PPath node(final Shape sh, final Stroke st, final Paint sp,
            final double minScale) {
        final PPath s = new PPath(sh) {
            private static final long serialVersionUID = 5255259239579383074L;

            @Override
            public void paint(final PPaintContext aPaintContext) {
                if (aPaintContext.getScale() < minScale)
                    return;
                super.paint(aPaintContext);
            }
        };
        s.setStroke(st);
        s.setStrokePaint(sp);
        s.setPickable(false);
        return s;
    }

    private final PNode handle;

    private BroomPromptModel model;

    private final PNode pie;

    private final float slideMax;

    private final PPath slider;

    public BroomPromptSimple() {
        this(null);
    }

    public BroomPromptSimple(final BroomPromptModel model) {
        final int pieAngle = 150;
        final Stroke st = new BasicStroke(0.01f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER);
        final Color sp = Color.BLACK;
        final Color bgc = new Color(1, 1, 1, 0.65f);
        // final Font fo = new Font("SansSerif", Font.BOLD, 1);
        final float outer = RockProps.DEFAULT.getRadius();
        slideMax = 5 * outer;
        final float inner = 0.5F * outer;
        setPickable(false);
        final BroomPromptSimple self = this;
        handle = new PNode() {
            private static final long serialVersionUID = -7641452321842902940L;

            /**
             * Return true if this node or any pickable descendends are picked.
             * If a pick occurs the pickPath is modified so that this node is
             * always returned as the picked node, event if it was a decendent
             * node that initialy reported the pick.
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
        final PNode bg;
        { // opaque Background
            float f = 1.2f;
            bg = node(new Arc2D.Float(-f * outer, -f * outer, 2 * f * outer, 2
                    * f * outer, 0, 360, Arc2D.OPEN), null, null, scale0);
            bg.setPaint(bgc);
            bg.setPickable(true);
            handle.addChild(bg);
        }
        { // Cross-hair circles and pie
            final int off = 90;
            final int pieOff = 180;
            // (1/4+angle) pie:
            pie = node(new Arc2D.Float(-outer, -outer, 2 * outer, 2 * outer,
                    off - pieOff, pieAngle, Arc2D.PIE), null, null, scale0);
            handle.addChild(pie);
            // (3/4) inner circle:
            handle
                    .addChild(node(new Arc2D.Float(-inner, -inner, 2 * inner,
                            2 * inner, off, pieOff + pieAngle, Arc2D.OPEN), st,
                            sp, 50));
            // (3/4+angle) outer circle:
            handle.addChild(node(new Arc2D.Float(-outer, -outer, 2 * outer,
                    2 * outer, off, pieOff + pieAngle - 12, Arc2D.OPEN), st,
                    sp, scale0));
            final double ar = Math.PI * (off + pieAngle) / 180.0;
            // chord ?
            if (pieAngle % 90 != 0)
                handle.addChild(node(new Line2D.Double(0, 0, -outer
                        * Math.cos(ar), outer * Math.sin(ar)), st, sp, scale0));
            // arrow:
            final float f = outer / 10;
            final PPath s = node(createArrowHead(f, 3 * f, 0.5f * f), null,
                    null, 50);
            s.setPaint(sp);
            s.translate(-outer * Math.cos(ar), outer * Math.sin(ar));
            s.rotate(Math.PI * (90 - off - pieAngle + 8) / 180.0);
            handle.addChild(s);
            this.addChild(handle);
        }
        { // y-axis:
            handle.addChild(node(new Line2D.Float(0, 1.2f * outer, 0, -5
                    * outer), st, sp, scale0));
            // x-axis:
            handle.addChild(node(new Line2D.Float(-1.2f * outer, 0,
                    1.2f * outer, 0), st, sp, scale0));
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
        log.debug(evt.getPropertyName());
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
        slider.setPaint(interpolateRGB(slow, fast, s));
        slider.getTransformReference(true).setToTranslation(0, -slideMax * s);
        slider.invalidateFullBounds();
        slider.invalidatePaint();
    }
}
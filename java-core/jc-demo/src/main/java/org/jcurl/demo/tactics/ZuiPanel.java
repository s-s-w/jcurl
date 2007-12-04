package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.undo.StateEdit;

import org.jcurl.core.base.CurveStore;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.zui.PCurveStore;
import org.jcurl.core.zui.PIceFactory;
import org.jcurl.core.zui.PPositionSet;
import org.jcurl.core.zui.PPositionSetDrag;
import org.jcurl.core.zui.PRockFactory;
import org.jcurl.core.zui.PTrajectoryFactory;

import edu.umd.cs.piccolo.PCanvas;
import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.util.PPaintContext;

class ZuiPanel extends JComponent {
    private static final long serialVersionUID = -4648771240323713217L;

    private final PPositionSet current;
    private final PNode ice;
    private final PPositionSet initial;
    private final int major = 255;
    private final int minor = 64;
    final PCanvas pico;

    private final PCurveStore traj;

    public ZuiPanel(final UndoRedoDocumentBase undo) {
        setLayout(new BorderLayout());
        this.add(pico = new PCanvas(), BorderLayout.CENTER);
        pico.setBackground(new Color(0xE8E8FF));
        pico.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        pico.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        // pico.getRoot().getDefaultInputManager().setKeyboardFocus(
        // new KeyboardZoom(pico.getCamera()));

        pico.getLayer().addChild(ice = new PIceFactory.Fancy().newInstance());

        traj = new PCurveStore(new PTrajectoryFactory.Fancy(), MainApp.tmax);
        initial = new PPositionSet(new PRockFactory.Fancy(minor));
        initial.addInputEventListener(new PPositionSetDrag() {
            private StateEdit edit = null;

            @Override
            public void mouseDragged(final PInputEvent event) {
                if (edit == null)
                    edit = new StateEdit(getInitialPos());
                super.mouseDragged(event);
            }

            @Override
            public void mouseReleased(final PInputEvent event) {
                if (edit == null)
                    return;
                super.mouseReleased(event);
                edit.end();
                undo.addEdit(edit);
                edit = null;
                // FIXME traj.sync(MainApp.tmax);
            }
        });
        current = new PPositionSet(new PRockFactory.Fancy(major));

        ice.addChild(traj);
        ice.addChild(current);
        ice.addChild(initial);
    }

    public PositionSet getCurrentPos() {
        return current.getPositionSet();
    }

    public PositionSet getInitialPos() {
        return initial.getPositionSet();
    }

    public void setCurrentPos(final PositionSet current) {
        this.current.setPositionSet(current);
    }

    public void setCurveStore(final CurveStore cs) {
        traj.setCurveStore(cs);
    }

    public void setInitialPos(final PositionSet initial) {
        this.initial.setPositionSet(initial);
    }
}
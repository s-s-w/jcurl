package org.jcurl.demo.tactics;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;

import org.jcurl.core.zui.KeyboardZoom;
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

/**
 * Graphical display for {@link ZuiMod}.
 */
class ZuiPanel extends JComponent {

    private static final long serialVersionUID = -4648771240323713217L;

    private final PNode ice;

    private final PCanvas pico;

    public ZuiPanel(final ZuiMod model) {
        setLayout(new BorderLayout());
        this.add(pico = new PCanvas(), BorderLayout.CENTER);
        pico.setBackground(new Color(0xE8E8FF));
        pico.setAnimatingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        pico.setInteractingRenderQuality(PPaintContext.HIGH_QUALITY_RENDERING);
        pico.getRoot().getDefaultInputManager().setKeyboardFocus(
                new KeyboardZoom(pico.getCamera()));

        pico.getLayer().addChild(ice = new PIceFactory.Fancy().newInstance());

        final PCurveStore traj = new PCurveStore(model.getCurveStore(),
                new PTrajectoryFactory.Fancy(), MainApp.tmax);
        final PNode initial = new PPositionSet(model.getInitialPos(),
                new PRockFactory.Fancy());
        initial.addInputEventListener(new PPositionSetDrag() {
            @Override
            public void mouseDragged(final PInputEvent event) {
                // TODO add undo/redo!
                super.mouseDragged(event);
            }

            @Override
            public void mouseReleased(final PInputEvent event) {
                super.mouseReleased(event);
                traj.sync(MainApp.tmax);
            }
        });
        final PNode current = new PPositionSet(model.getCurrentPos(),
                new PRockFactory.Simple());

        ice.addChild(traj);
        ice.addChild(current);
        ice.addChild(initial);
    }

    public void center() {
        pico.getCamera()
                .animateViewToCenterBounds(KeyboardZoom.houseP, true, 1);
    }
}
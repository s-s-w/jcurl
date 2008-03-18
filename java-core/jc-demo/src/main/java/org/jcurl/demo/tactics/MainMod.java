package org.jcurl.demo.tactics;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jcurl.core.api.Collider;
import org.jcurl.core.api.CollissionDetector;
import org.jcurl.core.api.Curler;
import org.jcurl.core.api.CurveStore;
import org.jcurl.core.api.IceSize;
import org.jcurl.core.api.PositionSet;
import org.jcurl.core.api.Rock;
import org.jcurl.core.api.RockDouble;
import org.jcurl.core.api.RockSet;
import org.jcurl.core.api.VelocitySet;
import org.jcurl.core.api.Unit;
import org.jcurl.core.helpers.AnnoHelper;
import org.jcurl.core.helpers.NotImplementedYetException;
import org.jcurl.core.impl.CollissionSpin;
import org.jcurl.core.impl.CurlerDenny;
import org.jcurl.core.impl.CurveManager;
import org.jcurl.core.impl.NewtonCollissionDetector;
import org.jcurl.core.io.IODocument;
import org.jcurl.core.io.IOTrajectories;
import org.jcurl.core.io.JCurlSerializer;
import org.jcurl.core.io.JDKSerializer;
import org.jcurl.core.ui.BroomPromptModel;
import org.jcurl.core.ui.UndoRedoDocumentBase;

class MainMod extends BroomPromptModel {

    private static final Log log = LogFactory.getLog(MainMod.class);
    private static final long serialVersionUID = 4796273188307263927L;

    static CurveManager initHammy(CurveManager te) {
        if (te == null)
            te = new CurveManager();
        te.setCollider(new CollissionSpin(0.5, 0.0));
        te.setCollissionDetector(new NewtonCollissionDetector());
        te.setCurler(new CurlerDenny(24, 1));
        te.setInitialPos(PositionSet.allOut());
        te.setInitialSpeed(new VelocitySet(new RockDouble()));
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

    static void initHammy(final PositionSet p, final VelocitySet s) {
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

    private final CurveManager ts = new CurveManager();

    final UndoRedoDocumentBase undo = new UndoRedoDocumentBase();

    public MainMod() {
        initHammy(ts);
    }

    public boolean addEdit(final UndoableEdit anEdit) {
        return undo.addEdit(anEdit);
    }

    public void addUndoableEditListener(final UndoableEditListener l) {
        undo.addUndoableEditListener(l);
    }

    public Map<CharSequence, CharSequence> annotations() {
        return ts.getAnnotations();
    }

    public boolean canRedo() {
        return undo.canRedo();
    }

    public boolean canUndo() {
        return undo.canUndo();
    }

    @Override
    public boolean equals(final Object obj) {
        return ts.equals(obj);
    }

    @Override
    public Rock getBroom() {
        throw new NotImplementedYetException();
    }

    public Collider getCollider() {
        return ts.getCollider();
    }

    public CollissionDetector getCollissionDetector() {
        return ts.getCollissionDetector();
    }

    public Curler getCurler() {
        return ts.getCurler();
    }

    File getCurrentFile() {
        return new File(".");
    }

    public PositionSet getCurrentPos() {
        return ts.getCurrentPos();
    }

    public VelocitySet getCurrentSpeed() {
        return ts.getCurrentSpeed();
    }

    public double getCurrentTime() {
        return ts.getCurrentTime();
    }

    public CurveStore getCurveStore() {
        return ts.getCurveStore();
    }
    public CurveManager getCurveManager() {
        return ts;
    }

    public PositionSet getInitialPos() {
        return ts.getInitialPos();
    }

    public VelocitySet getInitialSpeed() {
        return ts.getInitialSpeed();
    }

    public int getPlayed() {
        throw new NotImplementedYetException();
    }

    public double getSplitTime() {
        throw new NotImplementedYetException();
    }

    public Iterable<UndoableEditListener> getUndoableEditListeners() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int hashCode() {
        return ts.hashCode();
    }

    void open(final File src) {
        if (src.getName().endsWith(".jcz") || src.getName().endsWith(".jcx")) {
            log.debug(src);
            final JCurlSerializer xs = new JDKSerializer();
            try {
                throw new IOException("Not implemented yet"); // xs.read(src.toURL(),
                // xs.wrap(null,
                // ts));
            } catch (final IOException e) {
                log.error("", e);
            }
        } else
            open(new File(src.getAbsoluteFile() + ".jcz"));
    }

    public void redo() {
        undo.redo();
    }

    public void removeUndoableEditListener(final UndoableEditListener l) {
        undo.removeUndoableEditListener(l);
    }

    void save(final File dst) {
        if (dst.getName().endsWith(".jcz") || dst.getName().endsWith(".jcx")) {
            log.debug(dst);
            final JCurlSerializer xs = new JDKSerializer();
            final IODocument src = new IODocument();
            final IOTrajectories l = new IOTrajectories();
            src.setRoot(l);
            l.trajectories().add(ts);
            try {
                xs.write(src, dst);
            } catch (final IOException e) {
                log.error("", e);
            }
        } else
            save(new File(dst.getAbsoluteFile() + ".jcz"));
    }

    public void setBroom(final Rock b) {
        throw new NotImplementedYetException();
    }

    public void setCollider(final Collider collider) {
        ts.setCollider(collider);
    }

    public void setCollissionDetector(
            final CollissionDetector collissionDetector) {
        ts.setCollissionDetector(collissionDetector);
    }

    public void setCurler(final Curler curler) {
        ts.setCurler(curler);
    }

    public void setCurrentTime(final double currentTime) {
        ts.setCurrentTime(currentTime);
    }

    public void setCurveStore(final CurveStore curveStore) {
        propChange.firePropertyChange("curveStore", ts.getCurveStore(),
                curveStore);
        ts.setCurveStore(curveStore);
    }

    public void setInitialPos(final PositionSet initialPos) {
        ts.setInitialPos(initialPos);
    }

    public void setInitialSpeed(final VelocitySet initialSpeed) {
        ts.setInitialSpeed(initialSpeed);
    }

    public void setPlayed(final int p) {
        throw new NotImplementedYetException();
    }

    public void setSplitTime(final double s) {
        throw new NotImplementedYetException();
    }

    public void undo() {
        undo.undo();
    }
}
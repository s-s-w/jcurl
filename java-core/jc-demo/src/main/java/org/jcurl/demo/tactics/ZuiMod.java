package org.jcurl.demo.tactics;

import org.jcurl.core.base.CurveStore;
import org.jcurl.core.base.PositionSet;
import org.jcurl.core.helpers.IPropertyChangeSupport;

/**
 * Data model for {@link ZuiPanel}.
 */
interface ZuiMod extends UndoRedoDocument, IPropertyChangeSupport {
    public abstract PositionSet getCurrentPos();

    public abstract CurveStore getCurveStore();

    public abstract PositionSet getInitialPos();
}
package org.jcurl.demo.tactics;

import org.jcurl.core.base.CurveStore;
import org.jcurl.core.base.PositionSet;

/**
 * Data model for {@link ZuiPanel}.
 */
interface ZuiMod {

    public abstract PositionSet getCurrentPos();

    public abstract CurveStore getCurveStore();

    public abstract PositionSet getInitialPos();
}
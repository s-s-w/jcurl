/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
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
package jcurl.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import jcurl.core.NotImplementedYetException;
import jcurl.core.PositionSet;
import jcurl.core.RockSet;
import jcurl.core.SpeedSet;
import jcurl.core.dto.Ice;
import jcurl.sim.core.CollissionStrategy;
import jcurl.sim.core.SlideStrategy;

import org.apache.ugli.LoggerFactory;
import org.apache.ugli.ULogger;

/**
 * 
 * @see jcurl.core.io.OldConfigReaderTest
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class OldConfigReader {

    private static class RockData {

        public DimVal speed;

        public DimVal spin;

        public DimVal to_x;

        public DimVal to_y;
    }

    private static final ULogger log = LoggerFactory
            .getLogger(OldConfigReader.class);

    public static OldConfigReader load(final File file)
            throws FileNotFoundException, IOException {
        return load(new FileReader(file));
    }

    public static OldConfigReader load(final InputStream in) throws IOException {
        return load(new InputStreamReader(in));
    }

    public static OldConfigReader load(final Reader in) throws IOException {
        final OldConfigReader ret = new OldConfigReader();
        final String tok = readToken(in);
        if (!"curliniV2".equals(tok))
            throw new IllegalStateException("Invalid token: [" + tok + "]");
        parseIce(ret, in);
        while (parseRock(ret, in))
            ;
        return ret;
    }

    private static DimVal parseDim(final String s) {
        final DimVal ret = DimVal.parse(s);
        if (ret.dim == null)
            log.debug("DIMENSION NULL!");
        return ret;
    }

    private static boolean parseIce(final OldConfigReader ret, final Reader in)
            throws IOException {
        String tok = readToken(in);
        if (!"ICE".equals(tok))
            throw new IllegalStateException("Invalid token: [" + tok + "]");
        for (;;) {
            tok = readToken(in);
            if ("comment".equals(tok))
                ret.setIceComment(readToken(in));
            else if ("draw".equals(tok))
                ret.setDraw(readToken(in), readToken(in));
            else if ("raiseloss".equals(tok))
                ret.setLoss(readToken(in));
            else if ("friction".equals(tok))
                ret.setFrictionRockRock(readToken(in), readToken(in));
            else if ("DONE".equals(tok))
                return true;
            else
                throw new IllegalStateException("Invalid token: [" + tok + "]");
        }
    }

    private static boolean parseRock(final OldConfigReader ret, final Reader in)
            throws IOException {
        String tok = readToken(in);
        if (tok == null)
            return false;
        if (!"ROCK".equals(tok))
            throw new IllegalStateException("Invalid token: [" + tok + "]");
        final boolean isDark;
        {
            tok = readToken(in);
            if ("light".equals(tok))
                isDark = false;
            else if ("dark".equals(tok))
                isDark = true;
            else
                throw new IllegalStateException("Invalid token: [" + tok + "]");
        }
        final int no = Byte.parseByte(readToken(in)) - 1;
        for (;;) {
            tok = readToken(in);
            if ("from".equals(tok)) {
                tok = readToken(in);
                if ("OUT".equals(tok))
                    ret.setOut(ret, isDark, no);
                else
                    ret.setFrom(ret, isDark, no, tok, readToken(in));
            } else if ("to".equals(tok)) {
                tok = readToken(in);
                if ("OUT".equals(tok)) {
                    ret.setOut(ret, isDark, no);
                } else
                    ret.setTo(ret, isDark, no, tok, readToken(in));
            } else if ("speed".equals(tok))
                ret.setSpeed(ret, isDark, no, readToken(in));
            else if ("and".equals(tok))
                ;
            else if ("with".equals(tok))
                ;
            else if ("spin".equals(tok))
                ret.setSpin(ret, isDark, no, readToken(in));
            else if ("angle".equals(tok))
                ret.setAngle(isDark, no, readToken(in));
            else if ("DONE".equals(tok))
                return true;
            else
                throw new IllegalStateException("Invalid token: [" + tok + "]");
        }
    }

    /**
     * Read a single token.
     * 
     * @param read
     * @return -
     * @throws IOException
     */
    static String readToken(final Reader read) throws IOException {
        final StringBuffer ret = new StringBuffer();
        final int pre = 1;
        final int content = 2;
        final int comment = 3;
        char sep = '-';
        int state = pre;
        for (;;) {
            int ch = read.read();
            switch (state) {
            case pre:
                if (ch == -1) {
                    log.debug("token=[null]");
                    return null;
                }
                if (Character.isWhitespace((char) ch))
                    continue;
                if ('#' == (char) ch)
                    state = comment;
                else {
                    ret.setLength(0);
                    if ('"' == (char) ch)
                        sep = '"';
                    else {
                        sep = ' ';
                        ret.append((char) ch);
                    }
                    state = content;
                }
                break;
            case content:
                final String s0 = ret.toString().trim();
                if (ch == -1) {
                    log.debug("token=[{}]", s0);
                    return s0;
                }
                if (sep == ' ' && Character.isWhitespace((char) ch)) {
                    log.debug("token=[{}]", s0);
                    return s0;
                }
                if (sep == '"' && '"' == (char) ch) {
                    log.debug("token=[{}]", s0);
                    return s0;
                }
                if (sep == ' ' && '#' == (char) ch)
                    state = comment;
                else
                    ret.append((char) ch);
                break;
            case comment:
                final String s = ret.toString().trim();
                if (ch == -1) {
                    log.debug("token=[{}]", s);
                    return s;
                }
                if ('\n' == (char) ch || '\r' == (char) ch) {
                    if (s.length() == 0)
                        state = pre;
                    else {
                        log.debug("token=[{}]", s);
                        return s;
                    }
                }
                break;
            }
        }
    }

    private static int toIdx(final boolean isDark, final int no) {
        return 2 * no + (isDark ? 1 : 0);
    }

    private CollissionStrategy coll = null;

    private String iceComment = null;

    private PositionSet loc = PositionSet.allHome();

    private RockData[] rock = new RockData[RockSet.ROCKS_PER_SET];

    private SlideStrategy slide = null;

    private SpeedSet speed = null;

    public PositionSet getPos() {
        return loc;
    }

    public SlideStrategy getSlide() {
        if (slide == null) {
            // compute
        }
        return slide;
    }

    public SpeedSet getSpeed() {
        if (speed == null) {
            // compute
            final SlideStrategy ice = getSlide();
        }
        return speed;
    }

    private void setAngle(final boolean isDark, int no, final String a) {
        final DimVal angle = parseDim(a);
        log.debug((isDark ? "dark" : "light") + " " + no + ":" + angle);
        loc.getRock(toIdx(isDark, no)).setZ(angle.to(Dim.RADIANT).val);
    }

    private void setDraw(final String speed, final String curl) {
        final DimVal v = parseDim(speed);
        final DimVal c = parseDim(curl);
        log.debug(v + ", " + c);
        if (!v.dim.equals(Dim.SEC_HOG_TEE))
            throw new IllegalArgumentException("Must be seconds hog/tee.");
        slide.setDraw2Tee(v.val, c.to(Dim.METER).val);
    }

    private void setFrictionRockRock(final String type, final String amount) {
        final DimVal a = parseDim(amount);
        log.debug(a);
    }

    private void setFrom(final OldConfigReader ret, final boolean isDark,
            int no, final String x, final String y) {
        final DimVal _x = parseDim(x);
        final DimVal _y;
        if ("NHOG".equals(y))
            _y = new DimVal(21, Dim.FOOT);
        else
            _y = parseDim(y);
        log
                .debug((isDark ? "dark" : "light") + " " + no + ":" + _x + ", "
                        + _y);
        loc.getRock(toIdx(isDark, no)).setLocation(_x.to(Dim.METER).val,
                _x.to(Dim.METER).val);
    }

    private void setIceComment(final String s) {
        log.debug(s);
    }

    private void setLoss(final String amount) {
        final DimVal a = parseDim(amount);
        log.debug(a);
    }

    private void setOut(final OldConfigReader ret, final boolean isDark, int no) {
        log.debug((isDark ? "dark" : "light") + " " + no + ":" + "");
        Ice.setOut(loc.getRock(toIdx(isDark, no)), isDark, no);
    }

    private void setSpeed(final OldConfigReader ret, final boolean isDark,
            int no, final String v) {
        final DimVal _v = parseDim(v);
        log.debug((isDark ? "dark" : "light") + " " + no + ":" + _v);
        rock[toIdx(isDark, no)].speed = _v;
    }

    private void setSpin(final OldConfigReader ret, final boolean isDark,
            int no, final String v) {
        final DimVal _v = parseDim(v);
        log.debug((isDark ? "dark" : "light") + " " + no + ":" + _v);
        rock[toIdx(isDark, no)].spin = _v;
    }

    private void setTo(final OldConfigReader ret, final boolean isDark, int no,
            final String x, final String y) {
        final DimVal _x = parseDim(x);
        final DimVal _y = parseDim(y);
        log
                .debug((isDark ? "dark" : "light") + " " + no + ":" + _x + ", "
                        + _y);
        rock[toIdx(isDark, no)].to_x = _x;
        rock[toIdx(isDark, no)].to_y = _y;
    }
}
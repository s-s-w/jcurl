package org.jcurl.demo.tactics;

import org.jcurl.core.base.Rock;

/**
 * Data model for {@link RockControl}.
 */
interface RockMod {
    Rock getBroom();

    int getPlayed();

    double getSplitTime();

    void setBroom(Rock b);

    void setPlayed(int p);

    void setSplitTime(double s);
}
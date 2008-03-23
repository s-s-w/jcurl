package javax.measure;

import java.awt.geom.Point2D;

import javax.measure.quantity.Acceleration;
import javax.measure.quantity.Angle;
import javax.measure.quantity.AngularAcceleration;
import javax.measure.quantity.AngularVelocity;
import javax.measure.quantity.Duration;
import javax.measure.quantity.Force;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Quantity;
import javax.measure.quantity.Velocity;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import junit.framework.TestCase;

import org.jcurl.core.api.PositionSet;
import org.jcurl.core.api.Rock;

/**
 * Demo for a possible use of <a
 * href="http://www.jcp.org/en/jsr/detail?id=275">JSR-275</a>.
 * 
 * @author <a href="mailto:jcurl@gmx.net">M. Rohrmoser </a>
 * @version $Id$
 */
public class Jsr275Test extends TestCase {

	private static class RockDemo<T extends Quantity> extends Point2D.Double {}

	public void testDouble() {
		final Measure<Double, Length> x = Measure.valueOf(1.2, SI.METER);
		assertEquals("1.2 m", x.toString());
		assertEquals("3.937007874015748 ft", x.to(NonSI.FOOT).toString());

		final Measure<Double, Velocity> v = Measure.valueOf(1.2,
				SI.METERS_PER_SECOND);
		assertEquals("1.2 m/s", v.toString());
		assertEquals("2.3326133909287257 kn", v.to(NonSI.KNOT).toString());

		final Measure<Double, Acceleration> a = Measure.valueOf(1.2,
				SI.METERS_PER_SQUARE_SECOND);
		assertEquals("1.2 m/s²", a.toString());

		final Measure<Double, Angle> al = Measure.valueOf(1.2, SI.RADIAN);
		assertEquals("1.2 rad", al.toString());
		assertEquals("68.75493541569878 °", al.to(NonSI.DEGREE_ANGLE)
				.toString());

		final Measure<Double, AngularVelocity> w = Measure.valueOf(1.2,
				AngularVelocity.UNIT);
		assertEquals("1.2 rad/s", w.toString());

		final Measure<Double, AngularAcceleration> w_ = Measure.valueOf(1.2,
				AngularAcceleration.UNIT);
		assertEquals("1.2 rad/s²", w_.toString());

		final Measure<Double, Duration> t = Measure.valueOf(1.2, SI.SECOND);
		assertEquals("1.2 s", t.toString());
		assertEquals("0.02 min", t.to(NonSI.MINUTE).toString());

		final Measure<Double, Mass> m = Measure.valueOf(1.2, SI.GRAM);
		assertEquals("1.2 g", m.toString());
		assertEquals("0.0012 kg", m.to(SI.KILOGRAM).toString());

		final Measure<Double, Force> f = Measure.valueOf(1.2, SI.NEWTON);
		assertEquals("1.2 N", f.toString());
	}

	/**
	 * This could enable type-safe distinction between {@link PositionSet}/{@link VelocitySet}
	 * and {@link Rock} with position/velocity semantics.
	 */
	public void testRock() {
		VectorMeasure<Length> v_ = VectorMeasure.valueOf(1, 2, 3, SI.METER);

		final RockDemo<Length> x = new RockDemo<Length>();
		final RockDemo<Velocity> v = new RockDemo<Velocity>();
		Point2D p = x;
		p = v;
	}
}

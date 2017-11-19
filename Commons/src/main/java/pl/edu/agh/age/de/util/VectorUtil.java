package pl.edu.agh.age.de.util;

import javaslang.collection.Array;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class contains utility methods performing operations on vectors.
 *
 * @author Bartłomiej Grochal
 */
public final class VectorUtil {

	private VectorUtil() { }


	/**
	 * Returns a sum of two vectors given by {@code firstSummand} and {@code secondSummand}.
	 */
	public static Array<Double> addVectors(final Array<Double> firstSummand, final Array<Double> secondSummand) {
		checkArgument(firstSummand.length() == secondSummand.length());
		return firstSummand.zipWith(secondSummand, (first, second) -> first + second);
	}

	/**
	 * Returns a difference between two vectors given by {@code minuend} and {@code subtrahend}.
	 */
	public static Array<Double> subtractVectors(final Array<Double> minuend, final Array<Double> subtrahend) {
		checkArgument(minuend.length() == subtrahend.length());
		return minuend.zipWith(subtrahend, (first, second) -> first - second);
	}

	/**
	 * Returns a vector given by {@code vector} multiplied by a scalar value given by {@code scalar}.
	 */
	public static Array<Double> multiplyVectorByScalar(final Array<Double> vector, final double scalar) {
		return vector.map(element -> scalar * element);
	}

	/**
	 * Returns a vector given by {@code vector} as an array od primitive double values.
	 */
	public static double[] convertToPrimitiveDoubleArray(final Array<Double> vector) {
		return vector.toJavaStream()
			.mapToDouble(Double::doubleValue)
			.toArray();
	}

}

package pl.edu.agh.age.de.util;

import javaslang.collection.Array;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static pl.edu.agh.age.TestUtils.ACCURACY;

/**
 * This class contains tests for the {@link VectorUtil} class.
 *
 * @author Bartłomiej Grochal
 */
public class VectorUtilTest {

	private Array<Double> firstVector;
	private Array<Double> secondVector;
	private Array<Double> thirdVector;
	private Array<Double> shorterVector;


	@Before
	public void setUp() {
		firstVector = Array.of(-2.0d, -1.0d, 0.0d, 1.0d, 2.0d);
		secondVector = Array.of(1.0d, 2.0d, 1.0d, 0.0d, -5.0d);
		thirdVector = Array.of(-1.0d, 1.0d, 1.0d, 1.0d, -3.0d);
		shorterVector = Array.of(1.0d, 2.0d);
	}

	@Test
	public void addVectorsShouldReturnSumOfTwoVectors() {
		Assert.assertEquals(Array.empty(), VectorUtil.addVectors(Array.empty(), Array.empty()));

		Assert.assertEquals(thirdVector, VectorUtil.addVectors(firstVector, secondVector));
		Assert.assertEquals(thirdVector, VectorUtil.addVectors(secondVector, firstVector));
	}

	@Test(expected = IllegalArgumentException.class)
	public void addVectorsShouldThrowIllegalArgumentExceptionForVectorsOfDifferentLengths() {
		VectorUtil.addVectors(firstVector, shorterVector);
	}

	@Test
	public void subtractVectorsShouldReturnDifferenceOfTwoVectors() {
		Assert.assertEquals(Array.empty(), VectorUtil.subtractVectors(Array.empty(), Array.empty()));

		Assert.assertEquals(firstVector, VectorUtil.subtractVectors(thirdVector, secondVector));
		Assert.assertEquals(secondVector, VectorUtil.subtractVectors(thirdVector, firstVector));
	}

	@Test(expected = IllegalArgumentException.class)
	public void subtractVectorsShouldThrowIllegalArgumentExceptionForVectorsOfDifferentLengths() {
		VectorUtil.subtractVectors(firstVector, shorterVector);
	}

	@Test
	public void multiplyVectorByScalarShouldReturnScaledVector() {
		Assert.assertEquals(Array.empty(), VectorUtil.multiplyVectorByScalar(Array.empty(), 2.0d));
		Assert.assertEquals(firstVector, VectorUtil.multiplyVectorByScalar(firstVector, 1.0d));

		Assert.assertEquals(Array.of(0.0d, 0.0d, 0.0d, 0.0d, 0.0d),
			VectorUtil.multiplyVectorByScalar(firstVector, 0.0d).map(Math::abs));	// deals with -0.0d values
		Assert.assertEquals(Array.of(-4.0d, -2.0d, 0.0d, 2.0d, 4.0d),
			VectorUtil.multiplyVectorByScalar(firstVector, 2.0d));
		Assert.assertEquals(Array.of(4.0d, 2.0d, -0.0d, -2.0d, -4.0d),
			VectorUtil.multiplyVectorByScalar(firstVector, -2.0d));               // note that 0.0d * (-2.0d) = -0.0d
	}

	@Test
	public void convertToPrimitiveDoubleArrayShouldReturnArrayOfPrimitives() {
		Assert.assertArrayEquals(new double[]{}, VectorUtil.convertToPrimitiveDoubleArray(Array.empty()), ACCURACY);
		Assert.assertArrayEquals(new double[]{-2.0d, -1.0d, 0.0d, 1.0d, 2.0d}, VectorUtil.convertToPrimitiveDoubleArray(firstVector), ACCURACY);
	}

}

package pl.edu.agh.age.de.common.solution.evaluator;

import javaslang.collection.Array;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import static pl.edu.agh.age.TestUtils.ACCURACY;

/**
 * This class contains tests for the {@link RastriginEvaluator} class.
 *
 * @author Bartłomiej Grochal
 */
@RunWith(MockitoJUnitRunner.class)
public class RastriginEvaluatorTest {

	private RastriginEvaluator evaluator;
	private Array<Double> input;

	@Mock private EvaluatorCounter counter;


	@Before
	public void setUp() {
		evaluator = new RastriginEvaluator(counter);
	}

	@Test
	public void evaluateShouldReturnZeroForZerosVector() {
		input = Array.of(0.0d);
		Assert.assertEquals(0.0d, evaluator.evaluate(input), ACCURACY);

		input = Array.of(0.0d, 0.0d);
		Assert.assertEquals(0.0d, evaluator.evaluate(input), ACCURACY);

		input = Array.of(0.0d, 0.0d, 0.0d, 0.0d, 0.0d);
		Assert.assertEquals(0.0d, evaluator.evaluate(input), ACCURACY);
	}

	@Test
	public void evaluateShouldReturnProblemSizeForOnesVector() {
		input = Array.of(1.0d, 1.0d, 1.0d);
		Assert.assertEquals(3.0d, evaluator.evaluate(input), ACCURACY);

		input = Array.of(-1.0d, -1.0d, -1.0d);
		Assert.assertEquals(3.0d, evaluator.evaluate(input), ACCURACY);

		input = Array.of(1.0d, -1.0d, 1.0d, 1.0d, 1.0d, -1.0d, 1.0d, 1.0d, -1.0d, -1.0d);
		Assert.assertEquals(10.0d, evaluator.evaluate(input), ACCURACY);
	}

	@Test
	public void evaluateShouldReturnProperValueForNonHomogeneousVector() {
		input = Array.of(-1.0d, 0.25d, 0.5d, 1.0d, 2.0d, 3.0d);
		Assert.assertEquals(45.3125d, evaluator.evaluate(input), ACCURACY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void evaluateShouldThrowIllegalArgumentExceptionForEmptyVector() {
		input = Array.empty();
		evaluator.evaluate(input);
	}

}

package pl.edu.agh.age.de.classical.recombination;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.agh.age.TestUtils;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.edu.agh.age.TestUtils.ACCURACY;

/**
 * This class contains tests for the {@link RandomDiagonalRecombination} class.
 *
 * @author Bartłomiej Grochal
 */
@RunWith(MockitoJUnitRunner.class)
public class RandomDiagonalRecombinationTest {

	private RandomDiagonalRecombination recombination;

	@Mock private DoubleVectorSolutionFactory solutionFactory;
	@Mock private Random randomGenerator;


	@Before
	public void setUp() throws NoSuchFieldException, IllegalAccessException {
		recombination = new RandomDiagonalRecombination(solutionFactory);
		TestUtils.injectMockToField(recombination, randomGenerator, "randomGenerator");
	}

	@Test
	public void recombineShouldReturnSolutionWithProperlyMixedGenotype() {
		when(randomGenerator.nextDouble()).thenReturn(0.6d);
		when(solutionFactory.create(any())).thenAnswer(invocation ->
			new DoubleVectorSolution((double[]) invocation.getArguments()[0]));

		final DoubleVectorSolution firstSolution = new DoubleVectorSolution(new double[]{0.0d, 0.0d, 0.0d});
		final DoubleVectorSolution secondSolution = new DoubleVectorSolution(new double[]{1.0d, 2.0d, 3.0d});
		final DoubleVectorSolution recombinedSolution = recombination.recombine(firstSolution, secondSolution);

		Assert.assertArrayEquals(new double[]{0.6d, 1.2d, 1.8d}, recombinedSolution.values(), ACCURACY);
	}

}

package pl.edu.agh.age.de.differential.recombination;

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
import java.util.stream.DoubleStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.edu.agh.age.TestUtils.ACCURACY;

/**
 * This class contains tests for the {@link RandomCoordinatesRecombination} class.
 *
 * @author Bartłomiej Grochal
 */
@RunWith(MockitoJUnitRunner.class)
public class RandomCoordinatesRecombinationTest {

	private RandomCoordinatesRecombination recombination;

	@Mock private DoubleVectorSolutionFactory solutionFactory;
	@Mock private Random randomGenerator;


	@Before
	public void setUp() throws NoSuchFieldException, IllegalAccessException {
		recombination = new RandomCoordinatesRecombination(solutionFactory, 0.5d);
		TestUtils.injectMockToField(recombination, randomGenerator, "randomGenerator");
	}

	@Test
	public void recombineShouldReturnMixedGenotypesOfParentAndDonor() {
		final DoubleVectorSolution individualSolution = new DoubleVectorSolution(new double[]{2.0d, -1.0d, 0.0d, 1.0d});
		final DoubleVectorSolution donorSolution = new DoubleVectorSolution(new double[]{-3.0d, -2.0d, 3.0d, 4.0d});

		when(randomGenerator.doubles().limit(4)).thenReturn(DoubleStream.of(0.5d, 0.7d, 0.9d, 0.3d));
		when(randomGenerator.nextInt(4)).thenReturn(2);
		when(solutionFactory.create(any())).thenAnswer(invocation ->
			new DoubleVectorSolution((double[]) invocation.getArguments()[0]));

		final DoubleVectorSolution trialSolution = recombination.recombine(individualSolution, donorSolution);

		Assert.assertArrayEquals(new double[]{-3.0d, -1.0d, 3.0d, 4.0d}, trialSolution.values(), ACCURACY);
	}

}

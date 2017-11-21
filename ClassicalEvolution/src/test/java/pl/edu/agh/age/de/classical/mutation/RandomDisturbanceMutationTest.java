package pl.edu.agh.age.de.classical.mutation;

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
 * This class contains tests for the {@link RandomDisturbanceMutation} class.
 *
 * @author Bartłomiej Grochal
 */
@RunWith(MockitoJUnitRunner.class)
public class RandomDisturbanceMutationTest {

	private RandomDisturbanceMutation mutation;

	@Mock private DoubleVectorSolutionFactory solutionFactory;
	@Mock private Random randomGenerator;


	@Before
	public void setUp() throws NoSuchFieldException, IllegalAccessException {
		mutation = new RandomDisturbanceMutation(solutionFactory, 0.5d, 1.0d);
		TestUtils.injectMockToField(mutation, randomGenerator, "randomGenerator");
	}

	@Test
	public void mutateShouldReturnSolutionWithDisturbedGenotype() {
		when(randomGenerator.doubles(3)).thenReturn(DoubleStream.of(0.6d, 0.1d, 0.5d));
		when(randomGenerator.nextDouble()).thenReturn(0.3d).thenReturn(0.7d);
		when(solutionFactory.create(any())).thenAnswer(invocation ->
			new DoubleVectorSolution((double[]) invocation.getArguments()[0]));

		final DoubleVectorSolution solution = new DoubleVectorSolution(new double[]{-1.0d, 0.0d, 1.0d});
		final DoubleVectorSolution mutatedSolution = mutation.mutate(solution);

		Assert.assertArrayEquals(new double[]{-1.0d, -0.4d, 1.4d}, mutatedSolution.values(), ACCURACY);
	}

}

package pl.edu.agh.age.de.differential.mutation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.PopulationManager;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.edu.agh.age.TestUtils.ACCURACY;

/**
 * This class contains test for the {@link RandToBestOneMutation} class.
 *
 * @author Bartłomiej Grochal
 */
@RunWith(MockitoJUnitRunner.class)
public class RandToBestOneMutationTest {

	private RandToBestOneMutation mutation;

	@Mock private PopulationManager<EmasAgent> populationManager;
	@Mock private DoubleVectorSolutionFactory solutionFactory;


	@Before
	public void setUp() {
		mutation = new RandToBestOneMutation(populationManager, solutionFactory, new double[]{0.5d, 0.1d});
	}

	@Test
	public void mutateShouldReturnSolutionWithCombinedGenotypes() {
		final EmasAgent firstNeighbour = EmasAgent.create(0.0d, new DoubleVectorSolution(new double[]{0.0d, 1.0d, -2.0d, 3.0d}));
		final EmasAgent secondNeighbour = EmasAgent.create(0.0d, new DoubleVectorSolution(new double[]{-4.0d, 5.0d, 0.0d, -1.0d}));
		final EmasAgent thirdNeighbour = EmasAgent.create(0.0d, new DoubleVectorSolution(new double[]{1.0d, -1.0d, 1.0d, -1.0d}));
		final EmasAgent bestAgent = EmasAgent.create(0.0d, new DoubleVectorSolution(new double[]{2.0d, -10.0d, 3.0d, 0.0d}));

		when(populationManager.getRandom(3, 0))
			.thenReturn(Arrays.asList(firstNeighbour, secondNeighbour, thirdNeighbour));
		when(populationManager.getBest(0)).thenReturn(bestAgent);
		when(solutionFactory.create(any())).thenAnswer(invocation ->
			new DoubleVectorSolution((double[]) invocation.getArguments()[0]));

		final DoubleVectorSolution donorSolution = mutation.mutate(new DoubleVectorSolution(new double[]{Double.NaN}), 0);
		Assert.assertArrayEquals(new double[]{-2.3d, 2.9d, -2.0d, 2.7d}, donorSolution.values(), ACCURACY);
	}


}

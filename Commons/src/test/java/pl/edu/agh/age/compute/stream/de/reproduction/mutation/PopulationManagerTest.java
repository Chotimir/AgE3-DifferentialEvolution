package pl.edu.agh.age.compute.stream.de.reproduction.mutation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.age.compute.stream.Agent;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * This class contains tests for the {@link PopulationManager} class.
 *
 * @author Bartłomiej Grochal
 */
public class PopulationManagerTest {

	private PopulationManager<Agent> populationManager;


	@Before
	public void setUp() {
		populationManager = new PopulationManager<>(1);
	}

	@Test
	public void getRandomShouldReturnRandomlyChosenUniqueAgents() {
		final Agent firstAgent = mock(Agent.class);
		final Agent secondAgent = mock(Agent.class);
		final Agent thirdAgent = mock(Agent.class);

		final List<Agent> population = Arrays.asList(firstAgent, secondAgent, thirdAgent);
		populationManager.setPopulation(population, 0);

		final int expectedSize = 2;
		final List<Agent> randomAgents = populationManager.getRandom(2, 0);

		Assert.assertEquals(expectedSize, randomAgents.size());
		Assert.assertTrue(population.containsAll(randomAgents));
		Assert.assertFalse(randomAgents.get(0).equals(randomAgents.get(1)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRandomShouldThrowIllegalArgumentExceptionForZeroSize() {
		populationManager.getRandom(0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRandomShouldThrowIllegalArgumentExceptionForTooBigSize() {
		populationManager.getRandom(1, 0);
	}

	@Test
	public void getBestShouldReturnAgentWithLowestFitness() {
		final EmasAgent firstAgent = EmasAgent.create(0.0d, new DoubleVectorSolution(new double[]{0.0d}, 3.0d));
		final EmasAgent secondAgent = EmasAgent.create(0.0d, new DoubleVectorSolution(new double[]{1.0d}, 0.5d));
		final EmasAgent thirdAgent = EmasAgent.create(0.0d, new DoubleVectorSolution(new double[]{2.0d}, 2.3d));

		final List<Agent> population = Arrays.asList(firstAgent, secondAgent, thirdAgent);
		populationManager.setPopulation(population, 0);

		final EmasAgent bestAgent = populationManager.getBest(0);

		Assert.assertTrue(population.contains(bestAgent));
		Assert.assertEquals(secondAgent, bestAgent);
	}

}

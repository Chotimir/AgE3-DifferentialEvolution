package pl.edu.agh.age.compute.stream.de.reproduction.mutation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.edu.agh.age.compute.stream.Agent;

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
		populationManager = new PopulationManager<>();
	}

	@Test
	public void getRandomShouldReturnRandomlyChosenUniqueAgents() {
		final Agent firstAgent = mock(Agent.class);
		final Agent secondAgent = mock(Agent.class);
		final Agent thirdAgent = mock(Agent.class);

		final List<Agent> population = Arrays.asList(firstAgent, secondAgent, thirdAgent);
		populationManager.setPopulation(population);

		final int expectedSize = 2;
		final List<Agent> randomAgents = populationManager.getRandom(2);

		Assert.assertEquals(expectedSize, randomAgents.size());
		Assert.assertTrue(population.containsAll(randomAgents));
		Assert.assertFalse(randomAgents.get(0).equals(randomAgents.get(1)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRandomShouldThrowIllegalArgumentExceptionForZeroSize() {
		populationManager.getRandom(0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRandomShouldThrowIllegalArgumentExceptionForTooBigSize() {
		populationManager.getRandom(1);
	}

}

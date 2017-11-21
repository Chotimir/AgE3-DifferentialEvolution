package pl.edu.agh.age.compute.stream.de;

import javaslang.collection.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.agh.age.compute.stream.Agent;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.PopulationManager;

import static org.mockito.Mockito.*;

/**
 * This class contains tests for the {@link BeforeStepSimpleMergeWithPopulationManager} class.
 *
 * @author Bartłomiej Grochal
 */
@RunWith(MockitoJUnitRunner.class)
public class BeforeStepSimpleMergeWithPopulationManagerTest {

	private BeforeStepSimpleMergeWithPopulationManager<Agent> beforeStepAction;

	@Mock private PopulationManager<Agent> populationManager;


	@Before
	public void setUp() {
		beforeStepAction = new BeforeStepSimpleMergeWithPopulationManager<>(populationManager);
	}

	@Test
	public void applyShouldMergePopulationAndSetItInManager() {
		final Agent populationAgent = mock(Agent.class);
		final Agent newAgent = mock(Agent.class);

		final List<Agent> population = List.of(populationAgent);
		final List<Agent> newAgents = List.of(newAgent);

		final List<Agent> mergedPopulation = beforeStepAction.apply(0L, population, newAgents);

		Assert.assertTrue(mergedPopulation.containsAll(population) && mergedPopulation.containsAll(newAgents));
		Assert.assertEquals(population.size() + newAgents.size(), mergedPopulation.size());
		verify(populationManager, times(1)).setPopulation(mergedPopulation);
	}

}

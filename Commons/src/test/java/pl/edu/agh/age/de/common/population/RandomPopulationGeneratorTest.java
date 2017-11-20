package pl.edu.agh.age.de.common.population;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

/**
 * This class contains tests for the {@link RandomPopulationGenerator} class.
 *
 * @author Bartłomiej Grochal
 */
@RunWith(MockitoJUnitRunner.class)
public class RandomPopulationGeneratorTest {

	private RandomPopulationGenerator populationGenerator;
	private int agentsCount;

	@Mock private DoubleVectorSolutionFactory solutionFactory;


	@Before
	public void setUp() {
		agentsCount = 5;
		populationGenerator = new RandomPopulationGenerator(solutionFactory, agentsCount, 50);
	}

	@Test
	public void createPopulationShouldReturnListOfGivenSize() {
		Assert.assertEquals(agentsCount, populationGenerator.createPopulation().size());
	}

}

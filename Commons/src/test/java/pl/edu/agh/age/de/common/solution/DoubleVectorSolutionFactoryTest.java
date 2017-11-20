package pl.edu.agh.age.de.common.solution;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.compute.stream.problem.Evaluator;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.edu.agh.age.TestUtils.ACCURACY;

/**
 * This class contains tests for the {@link DoubleVectorSolutionFactory} class.
 *
 * @author Bartłomiej Grochal
 */
@RunWith(MockitoJUnitRunner.class)
public class DoubleVectorSolutionFactoryTest {

	private DoubleVectorSolutionFactory solutionFactory;

	private double minimalValue;
	private double maximalValue;

	private double[] genes;
	private double genesFitness;

	@Mock private Evaluator<DoubleVectorSolution> evaluator;


	@Before
	public void setUp() {
		minimalValue = -3.0d;
		maximalValue = 3.0d;

		solutionFactory = new DoubleVectorSolutionFactory(evaluator, 5, minimalValue, maximalValue);

		genes = new double[]{-2.0d, -1.0d, 0.0d, 1.0d, 2.0d};
		genesFitness = 7.0d;
	}

	@Test
	public void createWithGenesAndFitnessShouldReturnProperDoubleVectorSolution() {
		final DoubleVectorSolution solution = solutionFactory.create(genes, genesFitness);

		Assert.assertArrayEquals(genes, solution.values(), ACCURACY);
		Assert.assertEquals(genesFitness, solution.fitnessValue(), ACCURACY);
	}

	@Test
	public void createWithGenesShouldReturnProperDoubleVectorSolution() {
		when(evaluator.evaluate(any())).thenReturn(genesFitness);
		final DoubleVectorSolution solution = solutionFactory.create(genes);

		Assert.assertArrayEquals(genes, solution.values(), ACCURACY);
		Assert.assertEquals(genesFitness, solution.fitnessValue(), ACCURACY);
	}

	@Test
	public void createRandomShouldReturnProperDoubleVectorSolution() {
		when(evaluator.evaluate(any())).thenReturn(genesFitness);
		final DoubleVectorSolution solution = solutionFactory.createRandom();

		Arrays.stream(solution.values())
			.forEach(gene ->
				Assert.assertTrue(Double.compare(minimalValue, gene) <= 0 && Double.compare(maximalValue, gene) >= 0));

		Assert.assertEquals(genesFitness, solution.fitnessValue(), ACCURACY);
	}

}

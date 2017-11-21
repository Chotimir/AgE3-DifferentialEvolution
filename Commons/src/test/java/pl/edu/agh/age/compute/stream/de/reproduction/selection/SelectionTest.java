package pl.edu.agh.age.compute.stream.de.reproduction.selection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;

import static org.mockito.Mockito.when;

/**
 * This class contains tests for the {@link Selection} interface methods.
 *
 * @author Bartłomiej Grochal
 */
@RunWith(MockitoJUnitRunner.class)
public class SelectionTest {

	private Selection<Solution<?>> lowerFitnessSelection;
	private Selection<Solution<?>> higherFitnessSelection;

	@Mock private Solution<?> firstSolution;
	@Mock private Solution<?> secondSolution;
	@Mock private Solution<?> equalFitnessSolution;


	@Before
	public void setUp() {
		lowerFitnessSelection = Selection.lowerFitness();
		higherFitnessSelection = Selection.higherFitness();

		when(firstSolution.fitnessValue()).thenReturn(2.0d);
		when(secondSolution.fitnessValue()).thenReturn(5.0d);
		when(equalFitnessSolution.fitnessValue()).thenReturn(2.0d);
	}

	@Test
	public void lowerFitnessSelectionShouldReturnSolutionWithLowerFitness() {
		final Solution<?> selectedSolution = lowerFitnessSelection.select(firstSolution, secondSolution);
		Assert.assertEquals(firstSolution, selectedSolution);
	}

	@Test
	public void lowerFitnessSelectionShouldReturnFirstSolutionForEqualFitness() {
		final Solution<?> selectedSolution = lowerFitnessSelection.select(firstSolution, equalFitnessSolution);
		Assert.assertEquals(firstSolution, selectedSolution);
	}

	@Test
	public void higherFitnessSelectionShouldReturnSolutionWithHigherFitness() {
		final Solution<?> selectedSolution = higherFitnessSelection.select(firstSolution, secondSolution);
		Assert.assertEquals(secondSolution, selectedSolution);
	}

	@Test
	public void higherFitnessSelectionShouldReturnFirstSolutionForEqualFitness() {
		final Solution<?> selectedSolution = higherFitnessSelection.select(firstSolution, equalFitnessSolution);
		Assert.assertEquals(firstSolution, selectedSolution);
	}

}

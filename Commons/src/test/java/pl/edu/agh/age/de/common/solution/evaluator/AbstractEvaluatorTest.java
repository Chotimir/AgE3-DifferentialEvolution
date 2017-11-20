package pl.edu.agh.age.de.common.solution.evaluator;

import javaslang.collection.Array;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * This class contains tests for the {@link AbstractEvaluator} class.
 *
 * @author Bartłomiej Grochal
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractEvaluatorTest {

	private AbstractEvaluator evaluator;

	@Mock private EvaluatorCounter counter;
	@Mock private Solution<Array<Double>> solution;


	@Before
	public void setUp() {
		evaluator = new AbstractEvaluator(counter) {

			@Override
			double evaluate(Array<Double> genes) {
				return 0;
			}

		};
	}

	@Test
	public void evaluateShouldIncrementCounter() {
		evaluator.evaluate(solution);
		verify(counter, times(1)).increment();
	}

}

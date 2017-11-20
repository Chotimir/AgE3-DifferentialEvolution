package pl.edu.agh.age.de.common.solution.evaluator;

import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.compute.stream.problem.Evaluator;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

/**
 * This class contains common implementations for many evaluators of test functions for optimization.
 *
 * @author Bartłomiej Grochal
 */
public abstract class AbstractEvaluator implements Evaluator<DoubleVectorSolution> {

	private final EvaluatorCounter counter;


	/**
	 * @param counter Counter of the number of invocations of this evaluator.
	 */
	AbstractEvaluator(final EvaluatorCounter counter) {
		this.counter = counter;
	}


	/**
	 * Performs the update of the {@link #counter} and returns a particular fitness value for given {@code solution}.
	 */
	@Override
	public double evaluate(final DoubleVectorSolution solution) {
		counter.increment();
		return evaluate(solution.values());
	}


	abstract double evaluate(final double[] genes);

}

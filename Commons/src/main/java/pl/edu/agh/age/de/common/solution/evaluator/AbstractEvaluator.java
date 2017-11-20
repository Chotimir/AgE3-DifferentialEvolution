package pl.edu.agh.age.de.common.solution.evaluator;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;
import pl.edu.agh.age.compute.stream.problem.Evaluator;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

/**
 * This class contains common implementations for many evaluators of test functions for optimization.
 *
 * @author Bartłomiej Grochal
 */
public abstract class AbstractEvaluator implements Evaluator<Solution<Array<Double>>> {

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
	public double evaluate(final Solution<Array<Double>> solution) {
		counter.increment();
		return evaluate(solution.unwrap());
	}


	abstract double evaluate(final Array<Double> genes);

}

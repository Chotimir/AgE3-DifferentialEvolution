package pl.edu.agh.age.de.common.solution.evaluator;

import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.compute.stream.problem.Evaluator;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import java.util.stream.IntStream;

/**
 * This class implements the computation of the Rosenbrock test function for optimization.
 *
 * @author Bartłomiej Grochal
 */
public class RosenbrockEvaluator implements Evaluator<DoubleVectorSolution> {

	private static final double CONSTANT = 100.0d;

	private final EvaluatorCounter counter;


	/**
	 * @param counter Counter of the number of invocations of this evaluator.
	 */
	public RosenbrockEvaluator(final EvaluatorCounter counter) {
		this.counter = counter;
	}


	/**
	 * Performs the update of the {@link #counter} and returns a fitness value for given {@code solution}.
	 */
	@Override
	public double evaluate(final DoubleVectorSolution solution) {
		counter.increment();
		return evaluate(solution.values());
	}


	/**
	 * Calculates a value of the Rosenbrock function at given point represented by {@code genes}.
	 */
	private double evaluate(final double[] genes) {
		return IntStream.range(0, genes.length - 1)
			.sorted()
			.mapToDouble(index ->
				CONSTANT * Math.pow(genes[index + 1] - genes[index], 2.0d) + Math.pow(genes[index] - 1, 2.0d))
			.sum();
	}

}

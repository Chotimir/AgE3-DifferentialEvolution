package pl.edu.agh.age.de.common.solution.evaluator;

import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.compute.stream.problem.Evaluator;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import java.util.Arrays;

/**
 * This class implements the computation of the Sphere test function for optimization.
 *
 * @author Bartłomiej Grochal
 */
public class SphereEvaluator implements Evaluator<DoubleVectorSolution> {

	private final EvaluatorCounter counter;


	/**
	 * @param counter Counter of the number of invocations of this evaluator.
	 */
	public SphereEvaluator(final EvaluatorCounter counter) {
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
	 * Calculates a value of the Sphere function at given point represented by {@code genes}.
	 */
	private double evaluate(final double[] genes) {
		return Arrays.stream(genes)
			.map(gene -> Math.pow(gene, 2.0d))
			.sum();
	}

}

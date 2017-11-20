package pl.edu.agh.age.de.common.solution.evaluator;

import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import java.util.Arrays;

/**
 * This class implements the computation of the Rastrigin test function for optimization.
 *
 * @author Bartłomiej Grochal
 */
public class RastriginEvaluator extends AbstractEvaluator {

	private static final double A = 10.0d;


	public RastriginEvaluator(final EvaluatorCounter counter) {
		super(counter);
	}


	/**
	 * Calculates a value of the Rastrigin function at given point represented by {@code genes}.
	 */
	double evaluate(final double[] genes) {
		return A * genes.length +
			Arrays.stream(genes).map(gene -> Math.pow(gene, 2.0d) - A * Math.cos(2 * Math.PI * gene)).sum();
	}

}

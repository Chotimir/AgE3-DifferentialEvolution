package pl.edu.agh.age.de.common.solution.evaluator;

import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import java.util.Arrays;

/**
 * This class implements the computation of the Sphere test function for optimization.
 *
 * @author Bartłomiej Grochal
 */
public class SphereEvaluator extends AbstractEvaluator {

	public SphereEvaluator(final EvaluatorCounter counter) {
		super(counter);
	}


	/**
	 * Calculates a value of the Sphere function at given point represented by {@code genes}.
	 */
	double evaluate(final double[] genes) {
		return Arrays.stream(genes)
			.map(gene -> Math.pow(gene, 2.0d))
			.sum();
	}

}

package pl.edu.agh.age.de.common.solution.evaluator;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import static com.google.common.base.Preconditions.checkArgument;

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
	@Override
	double evaluate(final Array<Double> genes) {
		checkArgument(0 < genes.length());

		return A * genes.length() +
			genes.toJavaStream().mapToDouble(gene -> Math.pow(gene, 2.0d) - A * Math.cos(2 * Math.PI * gene)).sum();
	}

}

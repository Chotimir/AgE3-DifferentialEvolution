package pl.edu.agh.age.de.common.solution.evaluator;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implements the computation of the Rosenbrock test function for optimization.
 *
 * @author Bartłomiej Grochal
 */
public class RosenbrockEvaluator extends AbstractEvaluator {

	private static final double CONSTANT = 100.0d;


	public RosenbrockEvaluator(final EvaluatorCounter counter) {
		super(counter);
	}


	/**
	 * Calculates a value of the Rosenbrock function at given point represented by {@code genes}.
	 */
	double evaluate(final Array<Double> genes) {
		checkArgument(1 < genes.length());

		return IntStream.range(0, genes.length() - 1)
			.sorted()
			.mapToDouble(index -> CONSTANT * Math.pow(genes.get(index + 1) - Math.pow(genes.get(index), 2.0d), 2.0d)
				+ Math.pow(genes.get(index) - 1, 2.0d))
			.sum();
	}

}

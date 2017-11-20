package pl.edu.agh.age.de.common.solution.evaluator;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import static com.google.common.base.Preconditions.checkArgument;

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
	double evaluate(final Array<Double> genes) {
		checkArgument(0 < genes.length());

		return genes.toJavaStream()
			.mapToDouble(gene -> Math.pow(gene, 2.0d))
			.sum();
	}

}

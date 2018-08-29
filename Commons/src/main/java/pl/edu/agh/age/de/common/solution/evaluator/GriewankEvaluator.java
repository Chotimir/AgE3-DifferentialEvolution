package pl.edu.agh.age.de.common.solution.evaluator;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implements the computation of the Griewank test function for optimization.
 *
 * @author Jakub Piekarz
 */
public class GriewankEvaluator extends AbstractEvaluator {


	public GriewankEvaluator(final EvaluatorCounter counter) {
		super(counter);
	}


	/**
	 * Calculates a value of the Griewank function at given point represented by {@code genes}.
	 */
	@Override
	double evaluate(final Array<Double> genes) {
		checkArgument(0 < genes.length());

		int l = genes.length();

		double value1 = 0;
		double value2 = 1;
		for (int i=0; i<l; i++) {
			value1 += Math.pow(genes.get(i), 2)/4000.0;
			value2 *= Math.cos(genes.get(i)/Math.sqrt(i+1));
		}
		return value1 - value2 + 1;
	}

}

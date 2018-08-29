package pl.edu.agh.age.de.common.solution.evaluator;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implements the computation of the Ackley test function for optimization.
 *
 * @author Jakub Piekarz
 */
public class AckleyEvaluator extends AbstractEvaluator {

	private static final double A = 20.0d;
	private static final double B = 0.2d;
	private static final double C = 2*Math.PI;

	public AckleyEvaluator(final EvaluatorCounter counter) {
		super(counter);
	}


	/**
	 * Calculates a value of the Ackley function at given point represented by {@code genes}.
	 */
	@Override
	double evaluate(final Array<Double> genes) {
		checkArgument(0 < genes.length());

		int l = genes.length();

		double sum1 = 0;
		double sum2 = 0;
		for (int i = 0; i < l; i++) {
			sum1 += Math.pow(genes.get(i), 2);
			sum2 += Math.cos(C*genes.get(i));
		}
		return A + Math.exp(1) - A*Math.exp(-B*Math.sqrt(sum1/l)) - Math.exp(sum2/l);
	}

}

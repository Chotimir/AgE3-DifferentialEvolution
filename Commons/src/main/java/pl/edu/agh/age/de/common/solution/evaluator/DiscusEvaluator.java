package pl.edu.agh.age.de.common.solution.evaluator;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implements the computation of the Discus test function for optimization.
 *
 * @author Jakub Piekarz
 */
public class DiscusEvaluator extends AbstractEvaluator {

	private static final double A = 1000000.0d;


	public DiscusEvaluator(final EvaluatorCounter counter) {
		super(counter);
	}


	/**
	 * Calculates a value of the Discus function at given point represented by {@code genes}.
	 */
	@Override
	double evaluate(final Array<Double> genes) {
		checkArgument(1 < genes.length());

		double sum = IntStream.range(1, genes.length())
			.sorted()
			.mapToDouble(gene -> Math.pow(gene, 2.0d))
			.sum();

		return A*Math.pow(genes.get(0), 2.0d) + sum;
	}

}

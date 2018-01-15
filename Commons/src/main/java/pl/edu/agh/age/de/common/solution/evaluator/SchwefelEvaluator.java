package pl.edu.agh.age.de.common.solution.evaluator;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.problem.EvaluatorCounter;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implements the computation of the Schwefel test function for optimization.
 *
 * @author Bartłomiej Grochal
 */
public class SchwefelEvaluator extends AbstractEvaluator {

	private static final double CONSTANT = 418.9829d;


	public SchwefelEvaluator(EvaluatorCounter counter) {
		super(counter);
	}


	/**
	 * Calculates a value of the Schwefel function at given point represented by {@code genes}.
	 */
	@Override
	double evaluate(Array<Double> genes) {
		checkArgument(0 < genes.length());

		return CONSTANT * genes.length() -
			genes.toJavaStream().mapToDouble(gene -> gene * Math.sin(Math.sqrt(Math.abs(gene)))).sum();
	}

}

package pl.edu.agh.age.de.common.solution;

import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.compute.stream.problem.Evaluator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implement the factory method for creating new {@link DoubleVectorSolution solutions}.
 *
 * @author Bartłomiej Grochal
 */
public class DoubleVectorSolutionFactory {

	private final Evaluator<DoubleVectorSolution> evaluator;
	private final Random randomGenerator;

	private final int problemSize;
	private final double minimalValue;
	private final double maximalValue;


	/**
	 * @param evaluator    An object implementing the evaluation strategy for particular {@link DoubleVectorSolution
	 *                     solutions}.
	 * @param problemSize  A number of dimensions of the test function.
	 * @param minimalValue A lower boundary of the search space (i.e. the minimal value of a particular gene).
	 * @param maximalValue An upper boundary of the search space (i.e. the maximal value of a particular gene).
	 */
	public DoubleVectorSolutionFactory(final Evaluator<DoubleVectorSolution> evaluator, final int problemSize,
									   final double minimalValue, final double maximalValue) {
		checkArgument(0 < problemSize);
		checkArgument(minimalValue < maximalValue);

		this.evaluator = evaluator;
		randomGenerator = ThreadLocalRandom.current();

		this.problemSize = problemSize;
		this.minimalValue = minimalValue;
		this.maximalValue = maximalValue;
	}


	/**
	 * Returns a new {@link DoubleVectorSolution solution} holding given {@code genes} evaluated to given {@code
	 * fitness} value.
	 */
	public DoubleVectorSolution create(final double[] genes, final double fitness) {
		return new DoubleVectorSolution(genes, fitness);
	}

	/**
	 * Returns a new {@link DoubleVectorSolution solution} holding given {@code genes} with a fitness value evaluated by
	 * the {@link #evaluator}.
	 */
	public DoubleVectorSolution create(final double[] genes) {
		final DoubleVectorSolution solution = new DoubleVectorSolution(genes);
		return solution.updateFitness(evaluator.evaluate(solution));
	}

	/**
	 * Returns a new {@link DoubleVectorSolution solution} with random genotype and a fitness value evaluated by the
	 * {@link #evaluator}.
	 */
	public DoubleVectorSolution createRandom() {
		return create(randomGenerator.doubles(problemSize, minimalValue, maximalValue).toArray());
	}

}

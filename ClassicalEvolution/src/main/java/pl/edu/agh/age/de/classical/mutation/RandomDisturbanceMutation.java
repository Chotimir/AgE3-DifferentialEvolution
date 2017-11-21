package pl.edu.agh.age.de.classical.mutation;

import pl.edu.agh.age.compute.stream.emas.reproduction.mutation.Mutation;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implements the mutation operator for Classical EMAS Algorithm. This mutation strategy draws approximately
 * {@link #mutationRate} percent of genes and performs random disturbance of selected genes' values.
 *
 * @author Bartłomiej Grochal
 */
public class RandomDisturbanceMutation implements Mutation<DoubleVectorSolution> {

	private final DoubleVectorSolutionFactory solutionFactory;
	private final Random randomGenerator;

	private final double mutationRate;
	private final double disturbanceRange;


	/**
	 * @param mutationRate     A portion of genes to be mutated.
	 * @param disturbanceRange A range of disturbance of a gene's value (i.e. for a gene with given <b>value</b>, its
	 *                         disturbance is a random number not lesser than <b>value - disturbanceRange</b> and not
	 *                         greater than <b>value + disturbanceRange</b>).
	 */
	public RandomDisturbanceMutation(final DoubleVectorSolutionFactory solutionFactory, final double mutationRate,
									 final double disturbanceRange) {
		checkArgument(0 < mutationRate && 1 >= mutationRate);
		checkArgument(0 < disturbanceRange);

		this.solutionFactory = solutionFactory;
		randomGenerator = ThreadLocalRandom.current();

		this.mutationRate = mutationRate;
		this.disturbanceRange = disturbanceRange;
	}


	/**
	 * Performs a mutation of a genotype belonging to given {@code solution}.
	 */
	@Override
	public DoubleVectorSolution mutate(final DoubleVectorSolution solution) {
		final double[] mutationProbabilities = randomGenerator.doubles(solution.length()).toArray();

		final double[] mutatedGenotype = IntStream.range(0, solution.length())
			.sorted()
			.mapToDouble(index -> Double.compare(mutationProbabilities[index], mutationRate) <= 0 ?
				calculateDisturbance(solution.values()[index]) : solution.values()[index])
			.toArray();

		return solutionFactory.create(mutatedGenotype);
	}


	/**
	 * Calculates a new value for the mutated gene.
	 */
	private double calculateDisturbance(final double value) {
		// TODO: Trim the new value to the boundaries of the search space.
		double disturbance = disturbanceRange * (2 * randomGenerator.nextDouble() - 1);
		return value + disturbance;
	}

}

package pl.edu.agh.age.de.classical.mutation;

import pl.edu.agh.age.compute.stream.emas.reproduction.mutation.Mutation;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class contains common implementations for all the the mutation operator employed by the Classical EMAS Algorithm.
 * These mutation strategies strategy draw approximately {@link #mutationRate} percent of genes and perform random
 * disturbance of selected genes' values.
 *
 * @author Bartłomiej Grochal
 */
public abstract class AbstractDisturbanceMutation implements Mutation<DoubleVectorSolution> {

	final Random randomGenerator;

	private final DoubleVectorSolutionFactory solutionFactory;
	private final double mutationRate;


	/**
	 * @param solutionFactory A factory object creating new {@link DoubleVectorSolution solution} objects.
	 * @param mutationRate    A portion of genes to be mutated.
	 */
	public AbstractDisturbanceMutation(DoubleVectorSolutionFactory solutionFactory, double mutationRate) {
		checkArgument(0 < mutationRate && 1 >= mutationRate);

		randomGenerator = ThreadLocalRandom.current();
		this.solutionFactory = solutionFactory;
		this.mutationRate = mutationRate;
	}


	/**
	 * Draws a random number belonging to given distribution, which will be used as a disturbance value.
	 */
	public abstract double drawDisturbance();

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
		return value + drawDisturbance();
	}

}

package pl.edu.agh.age.de.differential.recombination;

import pl.edu.agh.age.compute.stream.emas.reproduction.recombination.Recombination;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implements a standard recombination mechanism employed by the Differential Evolution scheme. This
 * recombination strategy creates a trial vector by drawing approximately {@link #crossoverRatio} percent of genes and
 * taking the donor genes at selected positions (or the parent genes otherwise). Also, this algorithm ensures that at
 * least one (randomly chosen) gene of the trial vector is different than a corresponding gene of the parent vector.
 *
 * @author Bartłomiej Grochal
 */
public class RandomCoordinatesRecombination implements Recombination<DoubleVectorSolution> {

	private final DoubleVectorSolutionFactory solutionFactory;
	private final Random randomGenerator;

	private final double crossoverRatio;


	/**
	 * @param solutionFactory A factory object creating new {@link DoubleVectorSolution solution} objects belonging to
	 *                        the initial population.
	 * @param crossoverRatio  A portion of genes to be recombined.
	 */
	public RandomCoordinatesRecombination(final DoubleVectorSolutionFactory solutionFactory, final double crossoverRatio) {
		checkArgument(0 <= crossoverRatio && 1 >= crossoverRatio);

		this.solutionFactory = solutionFactory;
		randomGenerator = ThreadLocalRandom.current();

		this.crossoverRatio = crossoverRatio;
	}


	/**
	 * Performs a recombination of two genotypes belonging to given {@code individual} (parent) and {@code donor}.
	 */
	@Override
	public DoubleVectorSolution recombine(final DoubleVectorSolution individual, final DoubleVectorSolution donor) {
		checkArgument(individual.length() == donor.length());

		final double[] individualGenotype = individual.values();
		final double[] donorGenotype = donor.values();

		final double[] crossoverProbabilities = getCrossoverProbabilities(individualGenotype.length);
		final int fixedCoordinate = getFixedCoordinate(individualGenotype.length);

		final double[] trialGenotype = IntStream.range(0, individualGenotype.length)
			.sorted()
			.mapToDouble(index ->
				(Double.compare(crossoverProbabilities[index], crossoverRatio) <= 0 || index == fixedCoordinate) ?
					donorGenotype[index] : individualGenotype[index])
			.toArray();

		return solutionFactory.create(trialGenotype);
	}


	/**
	 * Returns an array of size {@code size} containing random numbers used to decide whether given gene should be
	 * recombined or not.
	 */
	private double[] getCrossoverProbabilities(final int size) {
		return randomGenerator.doubles()
			.limit(size)
			.toArray();
	}

	/**
	 * Draws a position of single gene (limited by a {@code bound}) to be recombined.
	 */
	private int getFixedCoordinate(final int bound) {
		return randomGenerator.nextInt(bound);
	}

}

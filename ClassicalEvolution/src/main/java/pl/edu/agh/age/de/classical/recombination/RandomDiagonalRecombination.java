package pl.edu.agh.age.de.classical.recombination;

import pl.edu.agh.age.compute.stream.emas.reproduction.recombination.Recombination;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implements the recombination operator for Classical EMAS Algorithm. This recombination strategy draws a
 * {@code N}-dimensional point (where {@code N} is the number of genes) located on a segment connecting two
 * {@code N}-dimensional points representing two genotypes to recombine.
 *
 * @author Bartłomiej Grochal
 */
public class RandomDiagonalRecombination implements Recombination<DoubleVectorSolution> {

	private final DoubleVectorSolutionFactory solutionFactory;
	private final Random randomGenerator;


	/**
	 * @param solutionFactory A factory object creating new {@link DoubleVectorSolution solution} objects.
	 */
	public RandomDiagonalRecombination(final DoubleVectorSolutionFactory solutionFactory) {
		this.solutionFactory = solutionFactory;
		randomGenerator = ThreadLocalRandom.current();
	}


	/**
	 * Performs a recombination of two genotypes belonging to given {@code firstSolution} and {@code secondSolution}.
	 */
	@Override
	public DoubleVectorSolution recombine(final DoubleVectorSolution firstSolution, final DoubleVectorSolution secondSolution) {
		checkArgument(firstSolution.length() == secondSolution.length());

		final double mutationVectorScale = randomGenerator.nextDouble();
		final double[] mutationVector = IntStream.range(0, firstSolution.length())
			.sorted()
			.mapToDouble(index -> mutationVectorScale * (secondSolution.values()[index] - firstSolution.values()[index]))
			.toArray();

		final double[] offspringGenotype = IntStream.range(0, mutationVector.length)
			.sorted()
			.mapToDouble(index -> firstSolution.values()[index] + mutationVector[index])
			.toArray();

		return solutionFactory.create(offspringGenotype);
	}

}

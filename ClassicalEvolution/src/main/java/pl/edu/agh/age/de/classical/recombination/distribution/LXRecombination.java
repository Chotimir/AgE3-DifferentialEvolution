package pl.edu.agh.age.de.classical.recombination.distribution;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.classical.recombination.AbstractRandomRecombination;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static pl.edu.agh.age.de.util.VectorUtil.*;

/**
 * This class implements the Laplace Crossover (LX) recombination operator for Classical EMAS Algorithm.
 *
 * @author Bartłomiej Grochal
 */
public class LXRecombination extends AbstractRandomRecombination {

	private final double distributionLocation;
	private final double distributionScale;


	public LXRecombination(final DoubleVectorSolutionFactory solutionFactory,
						   final double distributionLocation, final double distributionScale) {
		super(solutionFactory);

		checkArgument(0 < distributionScale);
		this.distributionLocation = distributionLocation;
		this.distributionScale = distributionScale;
	}


	@Override
	// TODO: Implement tests.
	public DoubleVectorSolution recombine(DoubleVectorSolution firstSolution, DoubleVectorSolution secondSolution) {
		checkArgument(firstSolution.length() == secondSolution.length());

		final Array<Double> firstGenotype = firstSolution.unwrap();
		final Array<Double> secondGenotype = secondSolution.unwrap();

		final Array<Double> disturbance = subtractVectors(secondGenotype, firstGenotype)
			.map(element -> randomLX() * Math.abs(element))
			.toArray();

		return solutionFactory.create(convertToPrimitiveDoubleArray(
			Double.compare(randomGenerator.nextDouble(), 0.5d) <= 0 ?
				addVectors(firstGenotype, disturbance) : addVectors(secondGenotype, disturbance)));
	}


	/**
	 * Transforms the uniform distribution to the Laplace distribution and returns a random number.
	 */
	private double randomLX() {
		final double uniformRandom = randomGenerator.nextDouble();

		return Double.compare(randomGenerator.nextDouble(), 0.5d) <= 0 ?
			distributionLocation + distributionScale * Math.log(uniformRandom) :
			distributionLocation - distributionScale * Math.log(uniformRandom);
	}

}

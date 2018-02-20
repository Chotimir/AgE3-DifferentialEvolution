package pl.edu.agh.age.de.classical.recombination.distribution;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.classical.recombination.AbstractRandomRecombination;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static pl.edu.agh.age.de.util.VectorUtil.*;

/**
 * This class implements the Simulated Binary Crossover (SBX) recombination operator for Classical EMAS Algorithm.
 *
 * @author Bartłomiej Grochal
 */
public class SBXRecombination extends AbstractRandomRecombination {

	private final double distributionScale;


	public SBXRecombination(final DoubleVectorSolutionFactory solutionFactory, final double distributionScale) {
		super(solutionFactory);

		checkArgument(0 <= distributionScale);
		this.distributionScale = distributionScale;
	}


	@Override
	// TODO: Implement tests.
	public DoubleVectorSolution recombine(final DoubleVectorSolution firstSolution, final DoubleVectorSolution secondSolution) {
		checkArgument(firstSolution.length() == secondSolution.length());

		final Array<Double> firstGenotype = firstSolution.unwrap();
		final Array<Double> secondGenotype = secondSolution.unwrap();

		final Array<Double> average = multiplyVectorByScalar(addVectors(firstGenotype, secondGenotype), 0.5d);
		final Array<Double> scaledDifference =
			multiplyVectorByScalar(subtractVectors(secondGenotype, firstGenotype), 0.5d * randomSBX());

		return solutionFactory.create(convertToPrimitiveDoubleArray(
			Double.compare(randomGenerator.nextDouble(), 0.5d) <= 0 ?
				subtractVectors(average, scaledDifference) : addVectors(average, scaledDifference)));
	}


	/**
	 * Transforms the uniform distribution to the SBX-beta distribution and returns a random number.
	 */
	private double randomSBX() {
		final double uniformRandom = randomGenerator.nextDouble();

		return Double.compare(uniformRandom, 0.5d) <= 0 ?
			Math.pow(2.0d * uniformRandom, 1.0d / (distributionScale + 1)) :
			Math.pow(2.0d * (1 - uniformRandom), -1.0d / (distributionScale + 1));
	}

}

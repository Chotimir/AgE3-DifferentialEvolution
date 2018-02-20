package pl.edu.agh.age.de.classical.recombination.uniform;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.classical.recombination.AbstractRandomRecombination;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import static com.google.common.base.Preconditions.checkArgument;
import static pl.edu.agh.age.de.util.VectorUtil.*;

/**
 * This class implements the recombination operator for Classical EMAS Algorithm. This recombination strategy draws a
 * {@code N}-dimensional point (where {@code N} is the number of genes) located on a segment connecting two
 * {@code N}-dimensional points representing two genotypes to recombine.
 *
 * @author Bartłomiej Grochal
 */
public class RandomDiagonalRecombination extends AbstractRandomRecombination {

	public RandomDiagonalRecombination(final DoubleVectorSolutionFactory solutionFactory) {
		super(solutionFactory);
	}


	@Override
	public DoubleVectorSolution recombine(final DoubleVectorSolution firstSolution, final DoubleVectorSolution secondSolution) {
		checkArgument(firstSolution.length() == secondSolution.length());

		final double mutationVectorScale = randomGenerator.nextDouble();

		final Array<Double> mutationVector =
			multiplyVectorByScalar(subtractVectors(secondSolution.unwrap(), firstSolution.unwrap()), mutationVectorScale);
		final Array<Double> offspringGenotype = addVectors(firstSolution.unwrap(), mutationVector);

		return solutionFactory.create(convertToPrimitiveDoubleArray(offspringGenotype));
	}

}

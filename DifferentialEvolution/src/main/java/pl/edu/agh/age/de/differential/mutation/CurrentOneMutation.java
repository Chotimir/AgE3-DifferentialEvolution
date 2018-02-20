package pl.edu.agh.age.de.differential.mutation;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.PopulationManager;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import java.util.List;

import static pl.edu.agh.age.de.util.VectorUtil.*;

/**
 * This class implements the DE/CURRENT/1 mutation strategy employed by the Differential Evolution scheme.
 *
 * @author Bartłomiej Grochal
 */
public class CurrentOneMutation extends AbstractMutation {

	CurrentOneMutation(final PopulationManager<EmasAgent> populationManager,
					   final DoubleVectorSolutionFactory solutionFactory, final double[] mutationFactors) {
		super(populationManager, solutionFactory, mutationFactors);
	}


	/**
	 * Returns a donor vector being a sum of two vectors:
	 * <ul>
	 * <li>the current vector to be mutated;</li>
	 * <li>a difference of two randomly chosen vectors scaled by the {@link #mutationFactors mutation factor}.</li>
	 * </ul>
	 */
	@Override
	public DoubleVectorSolution mutate(final DoubleVectorSolution solution, final long workplaceID) {
		final Array<Double> currentGenotype = solution.unwrap();
		final List<Array<Double>> randomGenotypes = getRandomGenotypes(2, workplaceID);

		final Array<Double> donor = addVectors(currentGenotype, multiplyVectorByScalar(
			subtractVectors(randomGenotypes.get(0), randomGenotypes.get(1)), mutationFactors[0]));
		return solutionFactory.create(convertToPrimitiveDoubleArray(donor));
	}

}

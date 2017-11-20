package pl.edu.agh.age.de.differential.mutation;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.PopulationManager;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import java.util.List;

import static pl.edu.agh.age.de.util.VectorUtil.*;

/**
 * This class implements the DE/RAND/1 mutation strategy employed by the Differential Evolution scheme.
 *
 * @author Bartłomiej Grochal
 */
public class RandOneMutation extends AbstractMutation {

	protected RandOneMutation(final PopulationManager<EmasAgent> populationManager,
							  final DoubleVectorSolutionFactory solutionFactory, final double mutationFactor) {
		super(populationManager, solutionFactory, mutationFactor);
	}


	/**
	 * Returns a donor vector being a sum of two vectors:
	 * <ul>
	 * <li>a randomly chosen one;</li>
	 * <li>a difference of two randomly chosen vectors scaled by the {@link #mutationFactor}.</li>
	 * </ul>
	 */
	@Override
	public DoubleVectorSolution mutate(final DoubleVectorSolution solution) {
		final List<Array<Double>> randomGenotypes = getRandomGenotypes(3);

		final Array<Double> donor = addVectors(randomGenotypes.get(0),
			multiplyVectorByScalar(subtractVectors(randomGenotypes.get(1), randomGenotypes.get(2)), mutationFactor));
		return solutionFactory.create(convertToPrimitiveDoubleArray(donor));
	}

}

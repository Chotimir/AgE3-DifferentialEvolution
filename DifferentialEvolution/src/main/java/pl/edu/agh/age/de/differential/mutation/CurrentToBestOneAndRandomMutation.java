package pl.edu.agh.age.de.differential.mutation;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.PopulationManager;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import java.util.List;

import static pl.edu.agh.age.de.util.VectorUtil.*;

/**
 * This class implements the DE/CURRENT to BEST/1 mutation strategy employed by the Differential Evolution scheme.
 *
 * @author Bartłomiej Grochal
 */
public class CurrentToBestOneAndRandomMutation extends AbstractMutation {

	CurrentToBestOneAndRandomMutation(final PopulationManager<EmasAgent> populationManager,
									  final DoubleVectorSolutionFactory solutionFactory, final double[] mutationFactors) {
		super(populationManager, solutionFactory, mutationFactors);
	}


	/**
	 * Returns a donor vector being a sum of three vectors:
	 * <ul>
	 * <li>the current vector to be mutated;</li>
	 * <li>a difference of two randomly chosen vectors scaled by the {@link #mutationFactors mutation factor};</li>
	 * <li>a difference of the best vector in a population (i.e. the one with the lowest fitness value) and the current
	 * vector.</li>
	 * </ul>
	 */
	@Override
	public DoubleVectorSolution mutate(final DoubleVectorSolution solution, final long workplaceID) {
		final Array<Double> currentGenotype = solution.unwrap();
		final Array<Double> bestGenotype = getBestGenotype(workplaceID);
		final List<Array<Double>> randomGenotypes = getRandomGenotypes(1, workplaceID);

		final Array<Double> donor = addVectors(currentGenotype, multiplyVectorByScalar(
			subtractVectors(randomGenotypes.get(0), bestGenotype), mutationFactors[0]));
		return solutionFactory.create(convertToPrimitiveDoubleArray(donor));
	}

}

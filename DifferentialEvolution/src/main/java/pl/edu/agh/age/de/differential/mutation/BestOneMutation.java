package pl.edu.agh.age.de.differential.mutation;

import javaslang.collection.Array;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.PopulationManager;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import java.util.List;

import static pl.edu.agh.age.de.util.VectorUtil.*;

/**
 * This class implements the DE/BEST/1 mutation strategy employed by the Differential Evolution scheme.
 *
 * @author Bartłomiej Grochal
 */
public class BestOneMutation extends AbstractMutation {

	BestOneMutation(final PopulationManager<EmasAgent> populationManager,
					final DoubleVectorSolutionFactory solutionFactory, final double[] mutationFactors) {
		super(populationManager, solutionFactory, mutationFactors);
	}


	/**
	 * Returns a donor vector being a sum of two vectors:
	 * <ul>
	 * <li>the best vector in a population (i.e. the one with the lowest fitness value);</li>
	 * <li>a difference of two randomly chosen vectors scaled by the {@link #mutationFactors mutation factor}.</li>
	 * </ul>
	 */
	@Override
	public DoubleVectorSolution mutate(final DoubleVectorSolution solution, final long workplaceID) {
		final Array<Double> bestGenotype = getBestGenotype(workplaceID);
		final List<Array<Double>> randomGenotypes = getRandomGenotypes(2, workplaceID);

		final Array<Double> donor = addVectors(bestGenotype,
			multiplyVectorByScalar(subtractVectors(randomGenotypes.get(0), randomGenotypes.get(1)), mutationFactors[0]));
		return solutionFactory.create(convertToPrimitiveDoubleArray(donor));
	}

}

package pl.edu.agh.age.de.differential.mutation;

import pl.edu.agh.age.compute.stream.de.reproduction.mutation.DifferentialEvolutionMutation;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.PopulationManager;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

/**
 * This class contains common implementations for many mutation strategies employed by the Differential Evolution scheme.
 *
 * @author Bartłomiej Grochal
 */
abstract class AbstractMutation extends DifferentialEvolutionMutation<DoubleVectorSolution> {

	final DoubleVectorSolutionFactory solutionFactory;


	/**
	 * @param solutionFactory A factory object creating new {@link DoubleVectorSolution solution} objects.
	 */
	AbstractMutation(final PopulationManager<EmasAgent> populationManager,
					 final DoubleVectorSolutionFactory solutionFactory, final double mutationFactor) {
		super(populationManager, mutationFactor);
		this.solutionFactory = solutionFactory;
	}

}

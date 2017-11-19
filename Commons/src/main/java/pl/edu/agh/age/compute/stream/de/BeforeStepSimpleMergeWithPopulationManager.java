package pl.edu.agh.age.compute.stream.de;

import javaslang.Function3;
import javaslang.collection.List;
import pl.edu.agh.age.compute.stream.Agent;
import pl.edu.agh.age.compute.stream.BeforeStepAction;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.PopulationManager;


/**
 * This class is responsible for capturing a population at the beginning of every
 * {@link pl.edu.agh.age.compute.stream.emas.EmasStep step} of the EMAS algorithm and saving it into the
 * {@link #populationManager}.
 *
 * @author Bartłomiej Grochal
 */
public class BeforeStepSimpleMergeWithPopulationManager<T extends Agent> implements BeforeStepAction<T> {

	private final PopulationManager<T> populationManager;


	/**
	 * @param populationManager An object containing a list of {@link pl.edu.agh.age.compute.stream.Agent agents}
	 *                          composing current population.
	 */
	public BeforeStepSimpleMergeWithPopulationManager(final PopulationManager<T> populationManager) {
		this.populationManager = populationManager;
	}


	/**
	 * Performs a {@link BeforeStepAction#simpleMerge() merge} of the {@code population} and {@code newAgents} lists and
	 * saves this merged population into the {@link #populationManager} for further processing by the Differential
	 * Evolution scheme.
	 */
	@Override
	public List<T> apply(final Long step, final List<T> population, final List<T> newAgents) {
		Function3<Long, List<T>, List<T>, List<T>> simpleMerge = BeforeStepAction.simpleMerge();

		List<T> updatedPopulation = simpleMerge.apply(step, population, newAgents);
		populationManager.setPopulation(updatedPopulation);

		return updatedPopulation;
	}

}

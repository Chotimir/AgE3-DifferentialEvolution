package pl.edu.agh.age.compute.stream.de.reproduction.mutation;

import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.reproduction.mutation.Mutation;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class implements the general behavior of the mutation operator employed by the Differential Evolution scheme.
 *
 * @author Bartłomiej Grochal
 */
public abstract class DifferentialEvolutionMutation<S extends Solution<?>> implements Mutation<S> {

	protected final double mutationFactor;

	private final PopulationManager<EmasAgent> populationManager;


	/**
	 * @param populationManager An object containing a list of {@link pl.edu.agh.age.compute.stream.Agent agents}
	 *                          composing current population.
	 * @param mutationFactor    A scalar value used to calculate a donor vector by the Differential Evolution scheme.
	 */
	protected DifferentialEvolutionMutation(final PopulationManager<EmasAgent> populationManager, final double mutationFactor) {
		this.populationManager = populationManager;
		this.mutationFactor = mutationFactor;
	}


	public PopulationManager<EmasAgent> getPopulationManager() {
		return populationManager;
	}


	/**
	 * Returns a list of given {@code size} composed of randomly chosen genotypes belonging to
	 * {@link pl.edu.agh.age.compute.stream.Agent agents} forming a population held by the {@link #populationManager}.
	 */
	protected <T> List<T> getRandomGenotypes(final int size) {
		return populationManager.getRandom(size)
			.stream()
			.map(agent -> (T) agent.solution.unwrap())
			.collect(Collectors.toList());
	}

}

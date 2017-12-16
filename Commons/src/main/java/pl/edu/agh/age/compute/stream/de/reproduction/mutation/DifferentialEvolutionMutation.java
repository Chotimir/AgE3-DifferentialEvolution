package pl.edu.agh.age.compute.stream.de.reproduction.mutation;

import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implements the general behavior of the mutation operator employed by the Differential Evolution scheme.
 *
 * @author Bartłomiej Grochal
 */
public abstract class DifferentialEvolutionMutation<S extends Solution<?>> {

	protected final double[] mutationFactors;

	private final PopulationManager<EmasAgent> populationManager;


	/**
	 * @param populationManager An object containing a list of {@link pl.edu.agh.age.compute.stream.Agent agents}
	 *                          composing current population.
	 * @param mutationFactors   Scalar values (usually one or two) used to calculate a donor vector by the Differential
	 *                          Evolution scheme.
	 */
	public DifferentialEvolutionMutation(final PopulationManager<EmasAgent> populationManager, final double[] mutationFactors) {
		checkArgument(0 < mutationFactors.length);
		checkArgument(0 < mutationFactors[0]);
		Arrays.stream(mutationFactors).skip(1).forEach(mutationFactor -> checkArgument(0 <= mutationFactor));

		this.populationManager = populationManager;
		this.mutationFactors = mutationFactors;
	}


	/**
	 * Performs a mutation according to the Differential Evolution scheme (returns a donor genotype derived from
	 * neighboring genotypes).
	 *
	 * @param solution    A current genotype for which a Differential Evolution step is performed.
	 * @param workplaceID An ID of a workplace containing the {@code solution} genotype.
	 */
	public abstract S mutate(final S solution, final long workplaceID);

	public PopulationManager<EmasAgent> getPopulationManager() {
		return populationManager;
	}


	/**
	 * Returns a list of given {@code size} composed of randomly chosen genotypes belonging to
	 * {@link pl.edu.agh.age.compute.stream.Agent agents} forming a population held by the {@link #populationManager}
	 * and identified by given {@code workplaceID}.
	 */
	protected <T> List<T> getRandomGenotypes(final int size, final long workplaceID) {
		return populationManager.getRandom(size, workplaceID)
			.stream()
			.map(agent -> (T) agent.solution.unwrap())
			.collect(Collectors.toList());
	}

	/**
	 * Returns a genotype with the lowest fitness value belonging to an {@link pl.edu.agh.age.compute.stream.Agent
	 * agent} living in a population held by the {@link #populationManager} and identified by given {@code workplaceID}.
	 */
	protected <T> T getBestGenotype(final long workplaceID) {
		return (T) populationManager.getBest(workplaceID).solution.unwrap();
	}
}

package pl.edu.agh.age.compute.stream.de.reproduction.mutation;

import pl.edu.agh.age.compute.stream.Agent;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.EmasAgentComparators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * <p>This class is responsible for holding a map between {@link pl.edu.agh.age.compute.stream.Workplace workplaces}
 * and lists of all {@link Agent agents} forming a population living on any of this workplaces in given
 * {@link pl.edu.agh.age.compute.stream.Step step}.</p>
 *
 * <p>Please note that the EMAS approach assumes that {@link Agent agents} do not possess any information about the
 * global state of computations, while the Differential Evolution scheme requires holding global knowledge about whole
 * population in given {@link pl.edu.agh.age.compute.stream.Step step}. Therefore, this class is responsible for holding
 * this global knowledge and keeping the agents unaware of this global state simultaneously.</p>
 *
 * @author Bartłomiej Grochal
 */
public class PopulationManager<T extends Agent> {

	private final Random randomGenerator;
	private final Map<Long, List<T>> populationByWorkplace;


	/**
	 * @param workplacesCount A number of workplaces in the environment.
	 */
	public PopulationManager(final int workplacesCount) {
		checkArgument(0 < workplacesCount);

		randomGenerator = ThreadLocalRandom.current();
		populationByWorkplace = new HashMap<>(workplacesCount);
	}


	/**
	 * Returns a list of size {@code size} composed of randomly chosen (with no return) {@link Agent agents} belonging
	 * to a current {@link #populationByWorkplace population} living on a workplace with given {@code workplaceID}.
	 */
	public synchronized List<T> getRandom(final int size, final long workplaceID) {
		checkArgument(0 <= workplaceID && populationByWorkplace.containsKey(workplaceID));
		final List<T> population = populationByWorkplace.get(workplaceID);

		checkArgument(0 < size && population.size() >= size);
		return randomGenerator
			.ints(0, population.size())
			.distinct()
			.limit(size)
			.mapToObj(population::get)
			.collect(Collectors.toList());
	}

	/**
	 * Returns an {@link EmasAgent} with the lowest fitness value belonging to a current {@link #populationByWorkplace
	 * population} living on a workplace with given {@code workplaceID}.
	 */
	public synchronized EmasAgent getBest(final long workplaceID) {
		checkArgument(0 <= workplaceID && populationByWorkplace.containsKey(workplaceID));
		final List<T> population = populationByWorkplace.get(workplaceID);

		checkArgument(0 < population.size());
		return population.stream()
			.map(agent -> (EmasAgent) agent)
			.sorted(EmasAgentComparators.lowerFitness().reversed())
			.findFirst()
			.get();
	}

	public synchronized void setPopulation(final List<T> population, final long workplaceID) {
		populationByWorkplace.put(workplaceID, population);
	}

	public synchronized void setPopulation(final javaslang.collection.List<T> population, final long workplaceID) {
		setPopulation(population.toJavaList(), workplaceID);
	}

}

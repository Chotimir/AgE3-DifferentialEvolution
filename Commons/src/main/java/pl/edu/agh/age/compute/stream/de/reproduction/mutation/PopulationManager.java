package pl.edu.agh.age.compute.stream.de.reproduction.mutation;

import pl.edu.agh.age.compute.stream.Agent;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * <p>This class is responsible for holding a list of all {@link Agent agents} forming a population in given
 * {@link pl.edu.agh.age.compute.stream.Step step}.</p>
 *
 * <p>Please note that the EMAS approach assumes that {@link Agent agents} do not possess information about the global
 * state of the computations, while the Differential Evolution scheme requires holding a global knowledge about the
 * whole population in given {@link pl.edu.agh.age.compute.stream.Step step}. Therefore, this class is responsible for
 * holding this global knowledge and keeping the agents unaware of this global state simultaneously.</p>
 *
 * @author Bartłomiej Grochal
 */
public class PopulationManager<T extends Agent> {

	private final Random randomGenerator;
	private List<T> population;


	public PopulationManager() {
		randomGenerator = ThreadLocalRandom.current();
		population = Collections.emptyList();
	}


	/**
	 * Returns a list of size {@code size} composed of randomly chosen (with no return) {@link Agent agents} belonging
	 * to a current {@link #population}.
	 */
	public List<T> getRandom(final int size) {
		checkArgument(0 < size && population.size() >= size);

		return randomGenerator
			.ints(0, population.size())
			.distinct()
			.limit(size)
			.mapToObj(index -> population.get(index))
			.collect(Collectors.toList());
	}

	public void setPopulation(final List<T> population) {
		this.population = population;
	}

	public void setPopulation(final javaslang.collection.List<T> population) {
		this.population = population.toJavaList();
	}

}

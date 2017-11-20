package pl.edu.agh.age.de.common.population;

import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.PopulationGenerator;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class is responsible for generating an initial population randomly.
 *
 * @author Bartłomiej Grochal
 */
public class RandomPopulationGenerator implements PopulationGenerator<EmasAgent> {

	private final DoubleVectorSolutionFactory solutionFactory;

	private final int agentsCount;
	private final int initialEnergy;


	/**
	 * @param solutionFactory A factory object creating new {@link DoubleVectorSolution solution} objects belonging to
	 *                        the initial population.
	 * @param agentsCount     A number of individuals to be generated.
	 * @param initialEnergy   Amount of energy deposited to each of newly-created individuals.
	 */
	public RandomPopulationGenerator(final DoubleVectorSolutionFactory solutionFactory, final int agentsCount,
									 final int initialEnergy) {
		checkArgument(0 < agentsCount);
		checkArgument(0 < initialEnergy);

		this.solutionFactory = solutionFactory;

		this.agentsCount = agentsCount;
		this.initialEnergy = initialEnergy;
	}


	/**
	 * Returns a list containing the initial population of {@link EmasAgent agents}.
	 */
	@Override
	public List<EmasAgent> createPopulation() {
		return IntStream.range(0, agentsCount)
			.mapToObj(value -> createAgent())
			.collect(Collectors.toList());
	}


	/**
	 * Returns single {@link EmasAgent agent} belonging to the initial population.
	 */
	private EmasAgent createAgent() {
		return EmasAgent.create(initialEnergy, solutionFactory.createRandom());
	}

}

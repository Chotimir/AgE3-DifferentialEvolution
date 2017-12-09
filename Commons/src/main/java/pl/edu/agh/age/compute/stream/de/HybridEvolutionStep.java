package pl.edu.agh.age.compute.stream.de;

import javaslang.Tuple2;
import javaslang.collection.List;
import pl.edu.agh.age.compute.stream.Environment;
import pl.edu.agh.age.compute.stream.Step;
import pl.edu.agh.age.compute.stream.de.reproduction.DifferentialEvolutionReproduction;
import pl.edu.agh.age.compute.stream.de.reproduction.DifferentialEvolutionReproductionBuilder;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.DifferentialEvolutionMutation;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.PopulationManager;
import pl.edu.agh.age.compute.stream.de.reproduction.selection.Selection;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.EmasStep;
import pl.edu.agh.age.compute.stream.emas.reproduction.recombination.Recombination;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * This class implements a {@link Step} performed by the Hybrid Evolution algorithm employing the Classical Evolution
 * first and then improving genotypes by performing a Differential Evolution step.
 *
 * @author Bartłomiej Grochal
 */
public class HybridEvolutionStep<S extends Solution<?>> implements Step<EmasAgent> {

	private final DifferentialEvolutionReproductionBuilder differentialEvolutionStepBuilder;
	private final PopulationManager<EmasAgent> populationManager;

	private final EmasStep<S> emasStep;
	private final Predicate<EmasAgent> differentialEvolutionPredicate;


	/**
	 * @param mutation                       A mutation operator employed by the Differential Evolution scheme.
	 * @param recombination                  A recombination operator employed by the Differential Evolution scheme.
	 * @param selection                      A selection operator employed by the Differential Evolution scheme.
	 * @param emasStep                       An implementation of a step performed by the Classical (EMAS) Algorithm.
	 * @param differentialEvolutionPredicate A predicate testing whether a Differential Evolution step will be performed.
	 */
	public HybridEvolutionStep(final DifferentialEvolutionMutation<S> mutation, final Recombination<S> recombination, final Selection<S> selection,
							   final EmasStep<S> emasStep, final Predicate<EmasAgent> differentialEvolutionPredicate) {
		differentialEvolutionStepBuilder = DifferentialEvolutionReproduction.<S>builder()
			.mutation(requireNonNull(mutation))
			.recombination(requireNonNull(recombination))
			.selection(requireNonNull(selection));
		populationManager = mutation.getPopulationManager();

		this.emasStep = requireNonNull(emasStep);
		this.differentialEvolutionPredicate = requireNonNull(differentialEvolutionPredicate);
	}


	/**
	 * <p>Performs single step of the Hybrid Evolution algorithm.</p>
	 *
	 * <p>Firstly, this implementation collects a population returned as a result of performing a Classical Evolution
	 * algorithm step. Please note that this population contains all alive agents, which are not migrated to another
	 * workplace and, thus, will be available on this workplace in the next iteration. Then, single Differential
	 * Evolution algorithm step is performed for all {@link EmasAgent agents} belonging to the aforementioned population
	 * and satisfying given {@link #differentialEvolutionPredicate predicate}. Finally, the population composed of:
	 * <ul>
	 * <li>agents not satisfying given predicate (and)</li>
	 * <li>agents holding affected solutions and unchanged amount of energy</li>
	 * </ul>
	 * is returned.</p>
	 *
	 * <p>Please note that using a {@link pl.edu.agh.age.compute.stream.emas.PopulationEvaluator PopulationEvaluator}
	 * with an {@link pl.edu.agh.age.compute.stream.emas.reproduction.improvement.Improvement Improvement} operator does
	 * not allow to perform a Differential Evolution step by non-child agents (therefore, each of the agents is allowed
	 * to perform a Differential Evolution step at most once).</p>
	 *
	 * <p>Please also note that a migrated agent is still able to perform a Differential Evolution step, but not during
	 * current iteration of the evolutionary algorithm (earliest within the next one).</p>
	 */
	@Override
	public List<EmasAgent> stepOn(final long stepNumber, final List<EmasAgent> population, final Environment environment) {
		populationManager.setPopulation(population, environment.workplaceId());
		final List<EmasAgent> afterStepPopulation = emasStep.stepOn(stepNumber, population, environment);
		final DifferentialEvolutionReproduction differentialEvolutionStep =
			differentialEvolutionStepBuilder.build(environment.workplaceId());

		final Tuple2<List<EmasAgent>, List<EmasAgent>> populationByPredicate =
			afterStepPopulation.partition(differentialEvolutionPredicate);
		return populationByPredicate._1
			.map(differentialEvolutionStep)
			.map(parentAndChild -> parentAndChild._1.withSolution(parentAndChild._2.solution))    // Already evaluated.
			.appendAll(populationByPredicate._2)
			.toList();
	}

}

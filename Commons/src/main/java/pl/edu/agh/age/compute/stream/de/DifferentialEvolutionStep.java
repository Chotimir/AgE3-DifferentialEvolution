package pl.edu.agh.age.compute.stream.de;

import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Set;
import pl.edu.agh.age.compute.stream.Environment;
import pl.edu.agh.age.compute.stream.Step;
import pl.edu.agh.age.compute.stream.de.reproduction.DifferentialEvolutionReproduction;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.DifferentialEvolutionMutation;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.PopulationManager;
import pl.edu.agh.age.compute.stream.de.reproduction.selection.Selection;
import pl.edu.agh.age.compute.stream.emas.*;
import pl.edu.agh.age.compute.stream.emas.migration.MigrationParameters;
import pl.edu.agh.age.compute.stream.emas.reproduction.recombination.Recombination;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;

import java.util.Comparator;
import java.util.UUID;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * This class implements a standard {@link Step} performed by the Differential Evolution scheme.
 *
 * @author Bartłomiej Grochal
 */
public class DifferentialEvolutionStep<S extends Solution<?>> implements Step<EmasAgent> {

	protected final DifferentialEvolutionMutation<S> mutation;
	protected final Recombination<S> recombination;
	protected final Selection<S> selection;

	protected final PopulationManager<EmasAgent> populationManager;
	protected final PopulationEvaluator<EmasAgent> populationEvaluator;

	protected final Comparator<EmasAgent> agentComparator;
	protected final MigrationParameters migrationParameters;


	/**
	 * Please note that all arguments must be non-null in order to perform a complete Differential Evolution step in the
	 * EMAS environment.
	 *
	 * @param mutation             A mutation operator employed by the Differential Evolution scheme.
	 * @param recombination        A recombination operator employed by the Differential Evolution scheme.
	 * @param selection            A selection operator employed by the Differential Evolution scheme.
	 * @param populationEvaluator  An evaluator of agents composing a population.
	 * @param agentComparator      A comparator of agents.
	 * @param migrationParameters  Parameters of agents migration between workplaces.
	 */
	public DifferentialEvolutionStep(final DifferentialEvolutionMutation<S> mutation, final Recombination<S> recombination, final Selection<S> selection,
									 final PopulationEvaluator<EmasAgent> populationEvaluator,
									 final Comparator<EmasAgent> agentComparator, final MigrationParameters migrationParameters) {
		this.mutation = requireNonNull(mutation);
		this.recombination = requireNonNull(recombination);
		this.selection = requireNonNull(selection);

		populationManager = mutation.getPopulationManager();
		this.populationEvaluator = requireNonNull(populationEvaluator);

		this.agentComparator = requireNonNull(agentComparator);
		this.migrationParameters = requireNonNull(migrationParameters);
	}


	/**
	 * Performs a single step of the Differential Evolution algorithm.
	 */
	@Override
	public List<EmasAgent> stepOn(final long stepNumber, final List<EmasAgent> population, final Environment environment) {
		populationManager.setPopulation(population);

		final DifferentialEvolutionReproduction reproductionStrategy = resolveReproductionStrategy();
		final Tuple2<Pipeline, Pipeline> reproducedPopulationPipelines =
			PipelineUtils.extractPipelineTuple(population.map(reproductionStrategy));

		final Pipeline parentAgentsPipeline = reproducedPopulationPipelines._1;
		final Pipeline childAgentsPipeline = reproducedPopulationPipelines._2.evaluate(populationEvaluator);

		environment.logPopulation("dead", parentAgentsPipeline.extract());
		return migrate(childAgentsPipeline, stepNumber, environment).extract();
	}


	/**
	 * Returns a reproduction strategy built on top of given operators.
	 */
	protected DifferentialEvolutionReproduction resolveReproductionStrategy() {
		return DifferentialEvolutionReproduction.<S>builder()
			.mutation(mutation)
			.recombination(recombination)
			.selection(selection)
			.build();
	}

	/**
	 * Performs a migration of agents between workplaces.
	 */
	protected Pipeline migrate(final Pipeline population, final long stepNumber, final Environment environment) {
		if (!shouldMigrate(stepNumber)) {
			return population;
		}

		final Predicate<EmasAgent> migrationPredicate = resolveMigrationPredicate(population.extract());
		final Tuple2<Pipeline, Pipeline> migratedPopulationPipeline = population.migrateWhen(migrationPredicate);

		migratedPopulationPipeline
			._1.extract()
			.forEach(agent -> environment.migrate(agent, environment.neighbours().get()._1));
		return migratedPopulationPipeline._2;
	}


	/**
	 * Returns a predicate selecting agents chosen to migrate between workplaces.
	 */
	private Predicate<EmasAgent> resolveMigrationPredicate(final List<EmasAgent> population) {
		if (!migrationParameters.migrateBestAgentsOnly()) {
			return Predicates.random(migrationParameters.partToMigrate());
		}

		final int numberToMigrate = (int) Math.ceil(migrationParameters.partToMigrate() * population.size());
		final Set<UUID> agentsToMigrate = population.toStream()
			.sorted(agentComparator)
			.reverse()
			.take(numberToMigrate)
			.map(agent -> agent.id)
			.toSet();

		return agent -> agentsToMigrate.contains(agent.id);
	}

	/**
	 * Checks whether the migration should be performed at the current step/iteration of the algorithm.
	 */
	private boolean shouldMigrate(final long currentStepNumber) {
		final long stepInterval = migrationParameters.stepInterval();
		return stepInterval != 0 && currentStepNumber % stepInterval == 0;
	}

}

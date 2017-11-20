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
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.Pipeline;
import pl.edu.agh.age.compute.stream.emas.PopulationEvaluator;
import pl.edu.agh.age.compute.stream.emas.Predicates;
import pl.edu.agh.age.compute.stream.emas.migration.MigrationParameters;
import pl.edu.agh.age.compute.stream.emas.reproduction.recombination.Recombination;
import pl.edu.agh.age.compute.stream.emas.reproduction.transfer.AsexualEnergyTransfer;
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

	private final PopulationManager<EmasAgent> populationManager;
	private final PopulationEvaluator<EmasAgent> populationEvaluator;

	private final Predicate<EmasAgent> deathPredicate;
	private final Predicate<EmasAgent> reproductionPredicate;

	private final AsexualEnergyTransfer asexualReproductionEnergyTransfer;
	private final DifferentialEvolutionReproduction reproductionStrategy;

	private final Comparator<EmasAgent> agentComparator;

	private final MigrationParameters migrationParameters;


	/**
	 * Please note that all arguments must be non-null in order to perform a complete Differential Evolution step in the
	 * EMAS environment.
	 *
	 * @param mutation                          A mutation operator employed by the Differential Evolution scheme.
	 * @param recombination                     A recombination operator employed by the Differential Evolution scheme.
	 * @param selection                         A selection operator employed by the Differential Evolution scheme.
	 * @param populationEvaluator               An evaluator of agents composing a population.
	 * @param deathPredicate                    A predicate for selecting agents to die.
	 * @param reproductionPredicate             A predicate for selecting agents to reproduce.
	 * @param asexualReproductionEnergyTransfer A strategy of transferring energy between parents and children.
	 * @param agentComparator                   A comparator of agents.
	 * @param migrationParameters               Parameters of agents migration between workplaces.
	 */
	public DifferentialEvolutionStep(final DifferentialEvolutionMutation<S> mutation, final Recombination<S> recombination, final Selection<S> selection,
									 final PopulationEvaluator<EmasAgent> populationEvaluator,
									 final Predicate<EmasAgent> deathPredicate, final Predicate<EmasAgent> reproductionPredicate,
									 final AsexualEnergyTransfer asexualReproductionEnergyTransfer,
									 final Comparator<EmasAgent> agentComparator,
									 final MigrationParameters migrationParameters) {
		populationManager = requireNonNull(mutation).getPopulationManager();
		this.populationEvaluator = requireNonNull(populationEvaluator);

		this.deathPredicate = requireNonNull(deathPredicate);
		this.reproductionPredicate = requireNonNull(reproductionPredicate);

		this.asexualReproductionEnergyTransfer = requireNonNull(asexualReproductionEnergyTransfer);
		reproductionStrategy = resolveReproductionStrategy(mutation, requireNonNull(recombination), requireNonNull(selection));

		this.agentComparator = requireNonNull(agentComparator);

		this.migrationParameters = requireNonNull(migrationParameters);
	}


	/**
	 * Performs a single step of the Differential Evolution algorithm.
	 */
	@Override
	public List<EmasAgent> stepOn(final long stepNumber, final List<EmasAgent> population, final Environment environment) {
		populationManager.setPopulation(population);

		final Tuple2<Pipeline, Pipeline> reproducedPopulationPipelines = Pipeline.on(population)
			.selfReproduce(reproductionPredicate, reproductionStrategy);

		final Tuple2<Pipeline, Pipeline> finalPopulationPipeline = reproducedPopulationPipelines
			._2.evaluate(populationEvaluator)
			.mergeWith(reproducedPopulationPipelines._1)
			.dieWhen(deathPredicate);

		environment.logPopulation("dead", finalPopulationPipeline._1.extract());
		return migrate(finalPopulationPipeline._2, stepNumber, environment).extract();
	}


	/**
	 * Returns a reproduction strategy built on top of given operators.
	 */
	private DifferentialEvolutionReproduction resolveReproductionStrategy(final DifferentialEvolutionMutation<S> mutation,
																		  final Recombination<S> recombination,
																		  final Selection<S> selection) {
		return DifferentialEvolutionReproduction.<S>builder()
			.mutation(mutation)
			.recombination(recombination)
			.selection(selection)
			.energyTransfer(asexualReproductionEnergyTransfer)
			.build();
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
	 * Performs a migration of agents between workplaces.
	 */
	private Pipeline migrate(final Pipeline population, final long stepNumber, final Environment environment) {
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
	 * Checks whether the migration should be performed at the current step/iteration of the algorithm.
	 */
	private boolean shouldMigrate(final long currentStepNumber) {
		final long stepInterval = migrationParameters.stepInterval();
		return stepInterval != 0 && currentStepNumber % stepInterval == 0;
	}

}

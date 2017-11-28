package pl.edu.agh.age.compute.stream.de;

import javaslang.Tuple2;
import javaslang.collection.List;
import pl.edu.agh.age.compute.stream.Environment;
import pl.edu.agh.age.compute.stream.Step;
import pl.edu.agh.age.compute.stream.de.reproduction.DifferentialEvolutionReproduction;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.DifferentialEvolutionMutation;
import pl.edu.agh.age.compute.stream.de.reproduction.selection.Selection;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.Pipeline;
import pl.edu.agh.age.compute.stream.emas.migration.MigrationParameters;
import pl.edu.agh.age.compute.stream.emas.reproduction.recombination.Recombination;
import pl.edu.agh.age.compute.stream.emas.reproduction.transfer.AsexualEnergyTransfer;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;

import java.util.Comparator;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * This class implements a {@link Step} performed by the Differential Evolution scheme with energy parameters.
 *
 * @author Bartłomiej Grochal
 */
public class DifferentialEvolutionStepWithEnergy<S extends Solution<?>> extends DifferentialEvolutionStep<S> {

	private final Predicate<EmasAgent> deathPredicate;
	private final Predicate<EmasAgent> reproductionPredicate;

	private final AsexualEnergyTransfer asexualReproductionEnergyTransfer;


	/**
	 * Please note that all arguments must be non-null in order to perform a complete Differential Evolution step in the
	 * EMAS environment.
	 *
	 * @param deathPredicate                    A predicate for selecting agents to die.
	 * @param reproductionPredicate             A predicate for selecting agents to reproduce.
	 * @param asexualReproductionEnergyTransfer A strategy of transferring energy between parents and children.
	 */
	public DifferentialEvolutionStepWithEnergy(final DifferentialEvolutionMutation<S> mutation, final Recombination<S> recombination, final Selection<S> selection,
											   final Comparator<EmasAgent> agentComparator, final MigrationParameters migrationParameters,
											   final Predicate<EmasAgent> deathPredicate, final Predicate<EmasAgent> reproductionPredicate,
											   final AsexualEnergyTransfer asexualReproductionEnergyTransfer) {
		super(mutation, recombination, selection, agentComparator, migrationParameters);

		this.deathPredicate = requireNonNull(deathPredicate);
		this.reproductionPredicate = requireNonNull(reproductionPredicate);

		this.asexualReproductionEnergyTransfer = requireNonNull(asexualReproductionEnergyTransfer);
	}


	@Override
	public List<EmasAgent> stepOn(final long stepNumber, final List<EmasAgent> population, final Environment environment) {
		populationManager.setPopulation(population, environment.workplaceId());

		final DifferentialEvolutionReproduction reproductionStrategy = resolveReproductionStrategy(environment);
		final Tuple2<Pipeline, Pipeline> reproducedPopulationPipelines = Pipeline.on(population)
			.selfReproduce(reproductionPredicate, reproductionStrategy);

		final Tuple2<Pipeline, Pipeline> finalPopulationPipeline = reproducedPopulationPipelines._1
			.mergeWith(reproducedPopulationPipelines._2)    // Already evaluated.
			.dieWhen(deathPredicate);

		environment.logPopulation("dead", finalPopulationPipeline._1.extract());
		return migrate(finalPopulationPipeline._2, stepNumber, environment).extract();
	}


	/**
	 * Returns a reproduction strategy built on top of given operators.
	 */
	protected DifferentialEvolutionReproduction resolveReproductionStrategy(final Environment environment) {
		return reproductionStrategyBuilder
			.workplaceID(environment.workplaceId())
			.energyTransfer(asexualReproductionEnergyTransfer)
			.build();
	}

}

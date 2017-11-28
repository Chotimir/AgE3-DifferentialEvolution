package pl.edu.agh.age.compute.stream.de.reproduction;

import javaslang.Tuple;
import javaslang.Tuple2;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.DifferentialEvolutionMutation;
import pl.edu.agh.age.compute.stream.de.reproduction.selection.Selection;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.reproduction.recombination.Recombination;
import pl.edu.agh.age.compute.stream.emas.reproduction.transfer.AsexualEnergyTransfer;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;

import static com.google.common.base.Preconditions.checkState;

/**
 * This class defines a standard pipeline for creating child {@link EmasAgent agents} according to the Differential
 * Evolution scheme.
 *
 * @author Bartłomiej Grochal
 */
public class DifferentialEvolutionReproductionPipeline<S extends Solution<?>> {

	private final EmasAgent parentAgent;
	private final EmasAgent childAgent;

	private final S donorSolution;
	private final S trialSolution;
	private final S childSolution;


	private DifferentialEvolutionReproductionPipeline(final EmasAgent parentAgent) {
		this(parentAgent, null);
	}

	private DifferentialEvolutionReproductionPipeline(final EmasAgent parentAgent, final S donorSolution) {
		this(parentAgent, donorSolution, null);
	}

	private DifferentialEvolutionReproductionPipeline(final EmasAgent parentAgent, final S donorSolution,
													  final S trialSolution) {
		this(parentAgent, donorSolution, trialSolution, null);
	}

	private DifferentialEvolutionReproductionPipeline(final EmasAgent parentAgent, final S donorSolution,
													  final S trialSolution, final S childSolution) {
		this(parentAgent, null, donorSolution, trialSolution, childSolution);
	}

	/**
	 * @param parentAgent   An agent representing a parent on a newly-created one (by this pipeline).
	 * @param childAgent    A newly-created agent (by this pipeline).
	 * @param donorSolution A donor solution created by combining neighboring solutions in a mutation operator according
	 *                      to the Differential Evolution scheme.
	 * @param trialSolution A trial solution created by recombining a parent solution and a donor solution according to
	 *                      the Differential Evolution scheme.
	 * @param childSolution A solution held by a {@link #childAgent}.
	 */
	private DifferentialEvolutionReproductionPipeline(final EmasAgent parentAgent, final EmasAgent childAgent,
													  final S donorSolution, final S trialSolution, final S childSolution) {
		this.parentAgent = parentAgent;
		this.childAgent = childAgent;

		this.donorSolution = donorSolution;
		this.trialSolution = trialSolution;
		this.childSolution = childSolution;
	}


	/**
	 * Returns a new instance of the reproduction pipeline built for given {@code parentAgent} parent.
	 */
	public static <S extends Solution<?>> DifferentialEvolutionReproductionPipeline<S> on(final EmasAgent parentAgent) {
		return new DifferentialEvolutionReproductionPipeline<>(parentAgent);
	}


	public DifferentialEvolutionReproductionPipeline<S> mutate(final DifferentialEvolutionMutation<S> mutation, final long workplaceID) {
		return new DifferentialEvolutionReproductionPipeline<>(parentAgent, mutation.mutate((S) parentAgent.solution, workplaceID));
	}

	public DifferentialEvolutionReproductionPipeline<S> recombine(final Recombination<S> recombination) {
		return new DifferentialEvolutionReproductionPipeline<>(parentAgent, donorSolution,
			recombination.recombine((S) parentAgent.solution, donorSolution));
	}

	public DifferentialEvolutionReproductionPipeline<S> select(final Selection<S> selection) {
		return new DifferentialEvolutionReproductionPipeline<>(parentAgent, donorSolution, trialSolution, selection.select((S) parentAgent.solution, trialSolution));
	}

	public DifferentialEvolutionReproductionPipeline<S> transferEnergy(final AsexualEnergyTransfer asexualReproductionEnergyTransfer) {
		final double[] depositedEnergy = asexualReproductionEnergyTransfer.transfer(parentAgent);
		final EmasAgent childAgent = this.childAgent != null ?
			this.childAgent.withEnergy(depositedEnergy[1]) : EmasAgent.create(depositedEnergy[1], childSolution);

		return new DifferentialEvolutionReproductionPipeline<>(parentAgent, childAgent, donorSolution, trialSolution, childSolution);
	}

	public DifferentialEvolutionReproductionPipeline<S> createChildWithNoEnergy() {
		final EmasAgent childAgent = this.childAgent != null ?
			this.childAgent.withEnergy(Double.NaN) : EmasAgent.create(Double.NaN, childSolution);

		return new DifferentialEvolutionReproductionPipeline<>(parentAgent, childAgent, donorSolution, trialSolution, childSolution);
	}

	/**
	 * Returns a tuple consisting of two agents: a parent and a child (created by an execution of this pipeline).
	 */
	public Tuple2<EmasAgent, EmasAgent> extract() {
		checkState(parentAgent != null && childAgent != null);
		return Tuple.of(parentAgent, childAgent);
	}

}

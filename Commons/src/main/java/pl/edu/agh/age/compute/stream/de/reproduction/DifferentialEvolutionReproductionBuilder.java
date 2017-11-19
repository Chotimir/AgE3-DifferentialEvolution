package pl.edu.agh.age.compute.stream.de.reproduction;

import com.google.common.base.Preconditions;
import pl.edu.agh.age.compute.stream.de.reproduction.mutation.DifferentialEvolutionMutation;
import pl.edu.agh.age.compute.stream.de.reproduction.selection.Selection;
import pl.edu.agh.age.compute.stream.emas.reproduction.recombination.Recombination;
import pl.edu.agh.age.compute.stream.emas.reproduction.transfer.AsexualEnergyTransfer;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;

/**
 * This class defines a builder pattern for creating a {@link DifferentialEvolutionReproduction reproduction} strategy.
 *
 * @author Bartłomiej Grochal
 */
public class DifferentialEvolutionReproductionBuilder<S extends Solution<?>> {

	private DifferentialEvolutionMutation<S> mutation;
	private Recombination<S> recombination;
	private Selection<S> selection;

	private AsexualEnergyTransfer asexualReproductionEnergyTransfer;


	DifferentialEvolutionReproductionBuilder() { }


	/**
	 * Adds a mutation operator to this builder.
	 */
	public DifferentialEvolutionReproductionBuilder<S> mutation(final DifferentialEvolutionMutation<S> mutation) {
		this.mutation = mutation;
		return this;
	}

	/**
	 * Adds a recombination operator to this builder.
	 */
	public DifferentialEvolutionReproductionBuilder<S> recombination(final Recombination<S> recombination) {
		this.recombination = recombination;
		return this;
	}

	/**
	 * Adds a selection operator to this builder.
	 */
	public DifferentialEvolutionReproductionBuilder<S> selection(final Selection<S> selection) {
		this.selection = selection;
		return this;
	}

	/**
	 * Adds an energy transfer strategy to this builder.
	 */
	public DifferentialEvolutionReproductionBuilder<S> energyTransfer(final AsexualEnergyTransfer asexualReproductionEnergyTransfer) {
		this.asexualReproductionEnergyTransfer = asexualReproductionEnergyTransfer;
		return this;
	}

	/**
	 * Returns a new instance of a {@link DifferentialEvolutionReproduction reproduction} strategy filled with the
	 * operators set previously.
	 */
	public DifferentialEvolutionReproduction build() {
		Preconditions.checkState(mutation != null && recombination != null && selection != null &&
			asexualReproductionEnergyTransfer != null);

		return parentAgent -> DifferentialEvolutionReproductionPipeline.<S>on(parentAgent)
			.mutate(mutation)
			.recombine(recombination)
			.select(selection)
			.transferEnergy(asexualReproductionEnergyTransfer)
			.extract();
	}

}

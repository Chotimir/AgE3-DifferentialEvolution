package pl.edu.agh.age.compute.stream.de.reproduction;

import pl.edu.agh.age.compute.stream.de.reproduction.mutation.DifferentialEvolutionMutation;
import pl.edu.agh.age.compute.stream.de.reproduction.selection.Selection;
import pl.edu.agh.age.compute.stream.emas.reproduction.recombination.Recombination;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;

import static com.google.common.base.Preconditions.checkState;

/**
 * This class defines a builder pattern for creating a {@link DifferentialEvolutionReproduction reproduction} strategy.
 *
 * @author Bartłomiej Grochal
 */
public class DifferentialEvolutionReproductionBuilder<S extends Solution<?>> {

	private DifferentialEvolutionMutation<S> mutation;
	private Recombination<S> recombination;
	private Selection<S> selection;


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
	 * Returns a new instance of a {@link DifferentialEvolutionReproduction reproduction} strategy filled with the
	 * operators set previously. Also, the {@code workplaceID} parameter must be fulfilled to indicate which population
	 * should be looked up when employing the {@link DifferentialEvolutionMutation Mutation} operator.
	 */
	public DifferentialEvolutionReproduction build(final long workplaceID) {
		checkState(mutation != null && recombination != null && selection != null && 0 <= workplaceID);

		return parentAgent -> {
			DifferentialEvolutionReproductionPipeline<S> reproductionPipeline = DifferentialEvolutionReproductionPipeline.<S>on(parentAgent)
				.mutate(mutation, workplaceID)
				.recombine(recombination)
				.select(selection);

			return reproductionPipeline.extract();
		};
	}

}

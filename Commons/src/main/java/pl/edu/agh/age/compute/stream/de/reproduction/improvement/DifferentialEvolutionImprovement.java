package pl.edu.agh.age.compute.stream.de.reproduction.improvement;

import pl.edu.agh.age.compute.stream.de.reproduction.mutation.DifferentialEvolutionMutation;
import pl.edu.agh.age.compute.stream.de.reproduction.selection.Selection;
import pl.edu.agh.age.compute.stream.emas.reproduction.improvement.Improvement;
import pl.edu.agh.age.compute.stream.emas.reproduction.recombination.Recombination;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;

/**
 * This class implements the improvement operator performing one step of the Differential Evolution scheme.
 *
 * @author Bartłomiej Grochal
 */
public class DifferentialEvolutionImprovement<S extends Solution<?>> implements Improvement<S> {

	private final DifferentialEvolutionMutation<S> mutation;
	private final Recombination<S> recombination;
	private final Selection<S> selection;


	/**
	 * @param mutation      A mutation operator compatible with the Differential Evolution scheme.
	 * @param recombination A recombination operator compatible with the Differential Evolution scheme.
	 * @param selection     A selection operator compatible with the Differential Evolution scheme.
	 */
	public DifferentialEvolutionImprovement(final DifferentialEvolutionMutation<S> mutation,
											final Recombination<S> recombination,
											final Selection<S> selection) {
		this.mutation = mutation;
		this.recombination = recombination;
		this.selection = selection;
	}


	/**
	 * Returns a new {@link Solution solution} produced by applying the improvement operator to given {@code solution}.
	 */
	@Override
	public S improve(final S solution) {
		// TODO: Consider applying the improvement operator occasionally (i.e. with given probability).
		final S donor = mutation.mutate(solution);
		final S trial = recombination.recombine(solution, donor);

		return selection.select(solution, trial);
	}

}

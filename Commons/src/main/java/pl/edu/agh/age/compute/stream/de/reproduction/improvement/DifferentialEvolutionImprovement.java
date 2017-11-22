package pl.edu.agh.age.compute.stream.de.reproduction.improvement;

import pl.edu.agh.age.compute.stream.de.reproduction.mutation.DifferentialEvolutionMutation;
import pl.edu.agh.age.compute.stream.de.reproduction.selection.Selection;
import pl.edu.agh.age.compute.stream.emas.reproduction.improvement.Improvement;
import pl.edu.agh.age.compute.stream.emas.reproduction.recombination.Recombination;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;

import java.util.function.Predicate;

/**
 * This class implements the improvement operator performing one step of the Differential Evolution scheme.
 *
 * @author Bartłomiej Grochal
 */
public class DifferentialEvolutionImprovement<S extends Solution<?>> implements Improvement<S> {

	private final DifferentialEvolutionMutation<S> mutation;
	private final Recombination<S> recombination;
	private final Selection<S> selection;

	private final Predicate<Solution<?>> improvementPredicate;


	/**
	 * @param mutation             A mutation operator compatible with the Differential Evolution scheme.
	 * @param recombination        A recombination operator compatible with the Differential Evolution scheme.
	 * @param selection            A selection operator compatible with the Differential Evolution scheme.
	 * @param improvementPredicate A predicate testing against the application of the improvement operator.
	 */
	public DifferentialEvolutionImprovement(final DifferentialEvolutionMutation<S> mutation,
											final Recombination<S> recombination,
											final Selection<S> selection,
											final Predicate<Solution<?>> improvementPredicate) {
		this.mutation = mutation;
		this.recombination = recombination;
		this.selection = selection;
		this.improvementPredicate = improvementPredicate;
	}


	/**
	 * Returns a new {@link Solution solution} produced by applying the improvement operator to given {@code solution}.
	 */
	@Override
	public S improve(final S solution) {
		if (!improvementPredicate.test(solution)) {
			return solution;
		}

		final S donor = mutation.mutate(solution);
		final S trial = recombination.recombine(solution, donor);

		return selection.select(solution, trial);
	}

}

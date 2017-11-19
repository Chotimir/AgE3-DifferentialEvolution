package pl.edu.agh.age.compute.stream.de.reproduction.selection;

import pl.edu.agh.age.compute.stream.emas.solution.Solution;

/**
 * This interface defines the selection operator taking two {@link Solution solutions} and returning one of them,
 * according to a predefined strategy.
 *
 * @param <S> An implementation of the {@link Solution} interface.
 *
 * @author Bartłomiej Grochal
 */
@FunctionalInterface
public interface Selection<S extends Solution<?>> {

	/**
	 * Returns one of given solutions according to a predefined strategy.
	 *
	 * @param firstSolution  The first {@link Solution} acting as an input for the selection operator.
	 * @param secondSolution The second {@link Solution} acting as an input for the selection operator.
	 */
	S select(S firstSolution, S secondSolution);


	/**
	 * Returns a {@link Solution} holding a genotype with lower fitness value.
	 */
	static <S extends Solution<?>> Selection<S> lowerFitness() {
		return (firstSolution, secondSolution) ->
			Double.compare(firstSolution.fitnessValue(), secondSolution.fitnessValue()) <= 0 ? firstSolution : secondSolution;
	}

	/**
	 * Returns a {@link Solution} holding a genotype with higher fitness value.
	 */
	static <S extends Solution<?>> Selection<S> higherFitness() {
		return (firstSolution, secondSolution) ->
			Double.compare(firstSolution.fitnessValue(), secondSolution.fitnessValue()) >= 0 ? firstSolution : secondSolution;
	}

}

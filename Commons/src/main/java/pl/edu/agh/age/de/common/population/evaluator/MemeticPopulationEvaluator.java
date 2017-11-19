package pl.edu.agh.age.de.common.population.evaluator;

import javaslang.collection.Seq;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.PopulationEvaluator;
import pl.edu.agh.age.compute.stream.emas.reproduction.improvement.Improvement;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;
import pl.edu.agh.age.compute.stream.problem.Evaluator;

import static java.util.Objects.requireNonNull;

/**
 * This class implements the memetic evaluation of {@link Solution solutions} belonging to {@link EmasAgent agents}
 * composing a population. The memetic evaluation combines the evolutionary algorithm with other optimization techniques
 * defined by the {@link Improvement} operator.
 *
 * @author Bartłomiej Grochal
 */
public class MemeticPopulationEvaluator<S extends Solution<?>> implements PopulationEvaluator<EmasAgent> {

	private final Evaluator<S> evaluator;
	private final Improvement<S> improvement;


	/**
	 * Please note that the {@code improvement} operator is necessary to pass. If you want to use classic evaluation
	 * scheme, please refer to the {@link SimplePopulationEvaluator} class.
	 *
	 * @param evaluator   An object implementing the evaluation strategy for particular {@link Solution solutions}.
	 * @param improvement An operator implementing additional optimization techniques applied on {@link Solution
	 *                    solutions}.
	 */
	public MemeticPopulationEvaluator(final Evaluator<S> evaluator, final Improvement<S> improvement) {
		this.evaluator = evaluator;
		this.improvement = requireNonNull(improvement);
	}


	/**
	 * Returns a list containing all agents belonging to given {@code population} with evaluated fitness values.
	 */
	@Override
	public Seq<EmasAgent> evaluate(final Seq<EmasAgent> population) {
		return population.map(agent -> {
			final S solution = (S) agent.solution;
			solution.updateFitness(evaluator.evaluate(solution));

			return EmasAgent.create(agent.energy, improvement.improve(solution));
		});
	}

}

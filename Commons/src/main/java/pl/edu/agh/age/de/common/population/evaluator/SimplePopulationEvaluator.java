package pl.edu.agh.age.de.common.population.evaluator;

import javaslang.collection.Seq;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.PopulationEvaluator;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;
import pl.edu.agh.age.compute.stream.problem.Evaluator;

/**
 * This class is responsible for evaluating {@link Solution solutions} belonging to {@link EmasAgent agents} composing
 * a population.
 *
 * @author Bartłomiej Grochal
 */
public class SimplePopulationEvaluator<S extends Solution<?>> implements PopulationEvaluator<EmasAgent> {

	private final Evaluator<S> evaluator;


	/**
	 * @param evaluator An object implementing the evaluation strategy for particular {@link Solution solutions}.
	 */
	public SimplePopulationEvaluator(final Evaluator<S> evaluator) {
		this.evaluator = evaluator;
	}


	/**
	 * Returns a list containing all agents belonging to given {@code population} with evaluated fitness values.
	 */
	@Override
	public Seq<EmasAgent> evaluate(final Seq<EmasAgent> population) {
		return population.map(agent -> {
			final S solution = (S) agent.solution;
			return EmasAgent.create(agent.energy, solution.updateFitness(evaluator.evaluate(solution)));
		});
	}

}

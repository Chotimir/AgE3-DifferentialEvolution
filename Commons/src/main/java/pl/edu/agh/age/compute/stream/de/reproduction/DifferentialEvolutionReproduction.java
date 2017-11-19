package pl.edu.agh.age.compute.stream.de.reproduction;

import javaslang.Tuple2;
import pl.edu.agh.age.compute.stream.emas.EmasAgent;
import pl.edu.agh.age.compute.stream.emas.solution.Solution;

import java.util.function.Function;

/**
 * This interface defines a reproduction strategy employed by the Differential Evolution scheme, which generates a new
 * agent from a given one (and returns a tuple consisting of two agents: a parent and a child).
 *
 * @author Bartłomiej Grochal
 */
@FunctionalInterface
public interface DifferentialEvolutionReproduction extends Function<EmasAgent, Tuple2<EmasAgent, EmasAgent>> {

	/**
	 * Returns a builder of the reproduction strategy designed for the Differential Evolution scheme.
	 */
	static <S extends Solution<?>> DifferentialEvolutionReproductionBuilder<S> builder() {
		return new DifferentialEvolutionReproductionBuilder<>();
	}

}

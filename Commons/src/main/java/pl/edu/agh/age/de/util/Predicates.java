package pl.edu.agh.age.de.util;

import pl.edu.agh.age.compute.stream.emas.solution.Solution;

import java.util.function.Predicate;

/**
 * This class contains implementations of the {@link Predicate} interface.
 *
 * @author Bartłomiej Grochal
 */
public class Predicates {

	public static Predicate<Solution<?>> always() {
		return solution -> true;
	}

}

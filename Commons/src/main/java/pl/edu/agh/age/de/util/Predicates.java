package pl.edu.agh.age.de.util;

import pl.edu.agh.age.compute.stream.emas.EmasAgent;

import java.util.function.Predicate;

/**
 * This interface contains implementations of the {@link Predicate} interface.
 *
 * @author Bartłomiej Grochal
 */
public interface Predicates {

	static Predicate<EmasAgent> always() {
		return agent -> true;
	}

	static Predicate<Long> everyIterations(final long iterations) {
		return iteration -> (iteration % iterations) == 0;
	}

}

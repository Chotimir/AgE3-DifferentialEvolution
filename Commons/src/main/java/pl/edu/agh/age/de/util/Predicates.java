package pl.edu.agh.age.de.util;

import pl.edu.agh.age.compute.stream.emas.EmasAgent;

import java.util.function.Predicate;

/**
 * This interface contains implementations of the {@link Predicate} interface.
 *
 * @author Bartłomiej Grochal
 */
public interface Predicates {

	/**
	 * Returns the {@link Predicate} which is always {@code true} for any {@link EmasAgent agent}.
	 */
	static Predicate<EmasAgent> always() {
		return agent -> true;
	}

	/**
	 * Returns the {@link Predicate} which is {@code true} if and only if given iteration number is not greater than
	 * {@code iterations}.
	 */
	static Predicate<Long> untilIterations(final long iterations) {
		return iteration -> iteration <= iterations;
	}

	/**
	 * Returns the {@link Predicate} which is {@code true} if and only if given iteration number is the multiple of
	 * {@code iterations}.
	 */
	static Predicate<Long> everyIterations(final long iterations) {
		return iteration -> (iteration % iterations) == 0;
	}

	/**
	 * Returns the conjunction of two {@link Predicate predicates}: {@link #everyIterations(long)} with the {@code
	 * iterationsInterval} argument and {@link #untilIterations(long)} with the {@code maxIterations} argument.
	 */
	static Predicate<Long> iterations(final long iterationsInterval, final long maxIterations) {
		return everyIterations(iterationsInterval).and(untilIterations(maxIterations));
	}

}

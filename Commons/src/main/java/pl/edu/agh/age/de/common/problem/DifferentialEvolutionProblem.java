package pl.edu.agh.age.de.common.problem;

import pl.edu.agh.age.compute.stream.logging.DefaultLoggingService;
import pl.edu.agh.age.compute.stream.problem.ProblemDefinition;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class contains a general definition of the optimization problem described by:
 * <ul>
 * <li>the optimization algorithm;</li>
 * <li>the test function to be optimized;</li>
 * <li>the number of dimensions of the test function.</li>
 * </ul>
 *
 * @author Bartłomiej Grochal
 */
public class DifferentialEvolutionProblem implements ProblemDefinition {

	private final String algorithm;
	private final String testFunction;

	private final int problemSize;


	/**
	 * @param algorithm    A name of the optimization algorithm.
	 * @param testFunction A name of the test function to be optimized.
	 * @param problemSize  A number of dimensions of the test function.
	 */
	public DifferentialEvolutionProblem(final String algorithm, final String testFunction, final int problemSize) {
		checkArgument(0 < problemSize);

		this.algorithm = algorithm;
		this.testFunction = testFunction;

		this.problemSize = problemSize;
	}


	/**
	 * Returns a string representation of the optimization problem.
	 */
	@Override
	public String representation() {
		final String space = " ";
		return String.join(DefaultLoggingService.DELIMITER,
			"DIFFERENTIAL EVOLUTION",
			String.join(space, algorithm, "algorithm"),
			String.join(space, testFunction, "test function in", Integer.toString(problemSize), "dimensions"));
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof DifferentialEvolutionProblem)) {
			return false;
		}
		final DifferentialEvolutionProblem that = (DifferentialEvolutionProblem) object;
		return Objects.equals(algorithm, that.algorithm) && Objects.equals(testFunction, that.testFunction) &&
			Objects.equals(problemSize, that.problemSize);
	}

	@Override
	public int hashCode() {
		return Objects.hash(algorithm, testFunction, problemSize);
	}

}

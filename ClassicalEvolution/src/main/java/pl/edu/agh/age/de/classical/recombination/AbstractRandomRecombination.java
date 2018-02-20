package pl.edu.agh.age.de.classical.recombination;

import pl.edu.agh.age.compute.stream.emas.reproduction.recombination.Recombination;
import pl.edu.agh.age.compute.stream.emas.solution.DoubleVectorSolution;
import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class contains common elements for all recombination operators designed for Classical EMAS algorithm and using
 * a random numbers generator.
 *
 * @author Bartłomiej Grochal
 */
public abstract class AbstractRandomRecombination implements Recombination<DoubleVectorSolution> {

	protected final DoubleVectorSolutionFactory solutionFactory;
	protected final Random randomGenerator;


	/**
	 * @param solutionFactory A factory object creating new {@link DoubleVectorSolution solution} objects.
	 */
	public AbstractRandomRecombination(final DoubleVectorSolutionFactory solutionFactory) {
		this.solutionFactory = solutionFactory;
		randomGenerator = ThreadLocalRandom.current();
	}

}

package pl.edu.agh.age.de.classical.mutation;

import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implements the Random Disturbance Mutation algorithm, where {@link #drawDisturbance disturbance} values
 * are drawn from the normal distribution with center 0 and given {@link #standardDeviation standard deviation}.
 *
 * @author Bartłomiej Grochal
 */
public class GaussianDisturbanceMutation extends AbstractDisturbanceMutation {

	private final double standardDeviation;


	/**
	 * @param standardDeviation A standard deviation of the normal distribution, which disturbance values are drawn
	 *                          from.
	 */
	public GaussianDisturbanceMutation(final DoubleVectorSolutionFactory solutionFactory, final double mutationRate,
									   double standardDeviation) {
		super(solutionFactory, mutationRate);

		checkArgument(0 < standardDeviation);
		this.standardDeviation = standardDeviation;
	}


	@Override
	public double drawDisturbance() {
		return standardDeviation * randomGenerator.nextGaussian();
	}

}

package pl.edu.agh.age.de.classical.mutation;

import pl.edu.agh.age.de.common.solution.DoubleVectorSolutionFactory;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This class implements the Random Disturbance Mutation algorithm, where {@link #drawDisturbance disturbance} values
 * are drawn from the uniform distribution on the interval [-{@link #disturbanceRange}, {@link #disturbanceRange}].
 *
 * @author Bartłomiej Grochal
 */
public class UniformDisturbanceMutation extends AbstractDisturbanceMutation {

	private final double disturbanceRange;


	/**
	 * @param disturbanceRange A range of disturbance of a gene's value (i.e. for a gene with given <b>value</b>, its
	 *                         disturbance is a random number not lesser than <b>value - disturbanceRange</b> and not
	 *                         greater than <b>value + disturbanceRange</b>).
	 */
	public UniformDisturbanceMutation(final DoubleVectorSolutionFactory solutionFactory, final double mutationRate,
									  final double disturbanceRange) {
		super(solutionFactory, mutationRate);

		checkArgument(0 < disturbanceRange);
		this.disturbanceRange = disturbanceRange;
	}


	@Override
	public double drawDisturbance() {
		return disturbanceRange * (2 * randomGenerator.nextDouble() - 1);
	}

}

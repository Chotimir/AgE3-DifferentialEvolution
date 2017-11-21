package pl.edu.agh.age;

import java.lang.reflect.Field;

/**
 * This interface contains common constants and implementations used thorough all tests.
 *
 * @author Bartłomiej Grochal
 */
public interface TestUtils {

	double ACCURACY = 1e-10;


	/**
	 * This method injects a mocked object (given by {@code mock}) to a private field (with name given by
	 * {@code fieldName}) enclosed by an outer object (given by {@code holder}).
	 */
	static void injectMockToField(Object holder, Object mock, String fieldName) throws NoSuchFieldException, IllegalAccessException {
		Field randomGeneratorField = holder.getClass().getDeclaredField(fieldName);
		randomGeneratorField.setAccessible(true);
		randomGeneratorField.set(holder, mock);
	}

}

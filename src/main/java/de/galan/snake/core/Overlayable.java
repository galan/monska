package de.galan.snake.core;

import org.apache.commons.lang3.NotImplementedException;


/**
 * daniel should have written a comment here.
 *
 * @author galan
 */
public interface Overlayable {

	default void set(String name, String value) {
		throw new NotImplementedException("Method not implemented");
	}


	default void setInt(String name, Integer value) {
		throw new NotImplementedException("Method not implemented");
	}


	default void setLong(String name, Long value) {
		throw new NotImplementedException("Method not implemented");
	}


	default void setDouble(String name, Double value) {
		throw new NotImplementedException("Method not implemented");
	}


	default void setBool(String name, Boolean value) {
		throw new NotImplementedException("Method not implemented");
	}


	default void setTime(String name, String value) {
		throw new NotImplementedException("Method not implemented");
	}


	default void remove(String name) {
		throw new NotImplementedException("Method not implemented");
	}

}

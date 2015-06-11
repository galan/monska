package de.galan.snake.core;

/**
 * Notifies about changes in the properties
 *
 * @author galan
 */
public interface SourceListener {

	/**
	 * A property was added
	 *
	 * @param name The key of the added property
	 * @param value The new value
	 */
	default void addedProperty(String name, String value) {
		// adapter
	}


	/**
	 * A property was removed
	 *
	 * @param name The key of the property
	 * @param oldValue The old value
	 */
	default void removedProperty(String name, String oldValue) {
		// adapter
	}


	/**
	 * An existing property changed it's value
	 *
	 * @param name Key of the property
	 * @param newValue The new value
	 * @param oldValue The old value
	 */
	default void updatedProperty(String name, String newValue, String oldValue) {
		// adapter
	}


	/**
	 * Properties have been refreshed.
	 */
	default void refreshedProperties() {
		// adapter
	}

}

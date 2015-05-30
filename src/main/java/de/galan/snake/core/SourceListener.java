package de.galan.snake.core;

/**
 * Notifies about changes in the properties
 *
 * @author daniel
 */
public interface SourceListener {

	/**
	 * A property was added
	 *
	 * @param name The key of the added property
	 * @param value The new value
	 */
	public void addedProperty(String name, String value);


	/**
	 * A property was removed
	 *
	 * @param name The key of the property
	 * @param oldValue The old value
	 */
	public void removedProperty(String name, String oldValue);


	/**
	 * An existing property changed it's value
	 *
	 * @param name Key of the property
	 * @param newValue The new value
	 * @param oldValue The old value
	 */
	public void updatedProperty(String name, String newValue, String oldValue);


	/**
	 * Properties have been refreshed.
	 */
	public void refreshedProperties();

}

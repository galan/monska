package de.galan.snake.core;

import java.util.Map;


/**
 * Contract to deliver configuration-properties.
 *
 * @author galan
 */
public interface SnakeSource {

	/*
	 * Returns a single configuration-property by name as String. Accesses directly the getProperties() method, so might
	 * be overriden by implementations to gain performance optimizations.
	 */
	/*
	default String get(String name) {
		return getProperties().get(name);
	}
	 */

	/** Returns all configuration-properties */
	public Map<String, String> getProperties();


	/** Actual initialization of the source, eg. connect to system, reading files, etc. */
	public void initialize(SnakeInstance instance);


	/** Adds a listener, that will be informed about changes in the configuration-properties. */
	public void addListener(SourceListener listener);


	/** Removes a listener from the list */
	public void removeListener(SourceListener listener);

}

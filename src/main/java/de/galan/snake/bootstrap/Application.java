package de.galan.snake.bootstrap;

/**
 * Simple lifecycle of an application
 *
 * @author galan
 */
public interface Application {

	default void initialize() {
		// can be overriden when required
	}


	public void start();


	default String getName() {
		return "default";
	}


	default void shutdown() {
		// can be overriden when required
	}

}

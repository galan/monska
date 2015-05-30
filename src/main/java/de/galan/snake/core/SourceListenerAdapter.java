package de.galan.snake.core;

/**
 * Adapter for {@link SourceListener}
 * 
 * @author daniel
 */
public class SourceListenerAdapter implements SourceListener {

	@Override
	public void addedProperty(String name, String value) {
		// Adapter method
	}


	@Override
	public void removedProperty(String name, String oldValue) {
		// Adapter method
	}


	@Override
	public void updatedProperty(String name, String newValue, String oldValue) {
		// Adapter method
	}


	@Override
	public void refreshedProperties() {
		// Adapter method
	}

}

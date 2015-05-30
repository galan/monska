package de.galan.snake.test;

import java.util.HashMap;
import java.util.Map;

import de.galan.snake.core.source.AbstractSnakeSource;


/**
 * daniel should have written a comment here.
 *
 * @author daniel
 */
public class TestSnakeSource extends AbstractSnakeSource {

	Map<String, String> testProperties = new HashMap<>();


	@Override
	protected Map<String, String> createDefaultProperties() {
		return testProperties;
	}


	public void set(String name, String value) {
		getProperties().put(name, value);
	}

}

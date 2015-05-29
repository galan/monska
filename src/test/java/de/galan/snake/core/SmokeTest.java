package de.galan.snake.core;

import org.junit.Test;

import de.galan.commons.time.Sleeper;
import de.galan.snake.boot.BootstrapSnake;
import de.galan.snake.client.Snake;


/**
 * daniel should have written a comment here.
 *
 * @author daniel
 */
public class SmokeTest {

	@Test
	public void testName() throws Exception {
		BootstrapSnake.init();
		Snake.get("a");
		Sleeper.sleep("1h");
	}

}

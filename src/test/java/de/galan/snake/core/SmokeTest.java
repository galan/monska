package de.galan.snake.core;

import static org.assertj.core.api.Assertions.*;

import org.junit.Ignore;
import org.junit.Test;

import de.galan.commons.time.Sleeper;
import de.galan.snake.boot.BootstrapSnake;
import de.galan.snake.client.Snake;


/**
 * daniel should have written a comment here.
 *
 * @author galan
 */
@Ignore
public class SmokeTest {

	@Test
	public void testName() throws Exception {
		BootstrapSnake.initWithDefaults();
		assertThat(Snake.get("a")).isNull();
		Sleeper.sleep("1h");
	}

}

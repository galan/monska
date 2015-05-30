package de.galan.snake.core;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import de.galan.commons.test.AbstractTestParent;
import de.galan.snake.test.TestSnakeSource;


/**
 * CUT OverlaySnakeModel
 *
 * @author daniel
 */
public class OverlaySnakeModelTest extends AbstractTestParent {

	private TestSnakeSource source;
	private OverlaySnakeModel model;


	@Before
	public void setup() {
		source = new TestSnakeSource();
		source.initialize(null);
	}


	@Test
	public void testName() throws Exception {
		source.set("aaa", "111");
		model = new OverlaySnakeModel(source, null);
		assertThat(model.get("aaa")).isEqualTo("111");
		model.set("aaa", "222");
		assertThat(model.get("aaa")).isEqualTo("222");
		model.overlayReset();
		assertThat(model.get("aaa")).isEqualTo("111");
		model.remove("aaa");
		assertThat(model.get("aaa")).isNull();
	}

}

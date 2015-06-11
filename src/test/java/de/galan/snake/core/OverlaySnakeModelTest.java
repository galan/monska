package de.galan.snake.core;

import static org.assertj.core.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import de.galan.commons.test.AbstractTestParent;
import de.galan.snake.test.TestSnakeSource;


/**
 * CUT OverlaySnakeModel
 *
 * @author galan
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
	public void overlayAndRemove() throws Exception {
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


	@Test
	public void overlayString() throws Exception {
		model = new OverlaySnakeModel(source, null);
		model.set("xyz", "hello");
		assertThat(model.get("xyz")).isEqualTo("hello");
	}


	@Test
	public void overlayInt() throws Exception {
		model = new OverlaySnakeModel(source, null);
		model.setInt("xyz", 1234567);
		assertThat(model.get("xyz")).isEqualTo("1234567");
	}


	@Test
	public void overlayLong() throws Exception {
		model = new OverlaySnakeModel(source, null);
		model.setLong("xyz", 1234567L);
		assertThat(model.get("xyz")).isEqualTo("1234567");
	}


	@Test
	public void overlayDouble() throws Exception {
		model = new OverlaySnakeModel(source, null);
		model.setDouble("xyz", 1234.4567d);
		assertThat(model.get("xyz")).isEqualTo("12345.4567");
	}


	@Test
	public void overlayBoolean() throws Exception {
		model = new OverlaySnakeModel(source, null);
		model.setBool("xtrue", true);
		assertThat(model.get("xtrue")).isEqualTo("true");
		model.setBool("ytrue", false);
		assertThat(model.get("ytrue")).isEqualTo("false");
		model.setBool("ztrue", null);
		assertThat(model.get("ztrue")).isEqualTo("false");
	}


	@Test
	public void overlayTime() throws Exception {
		model = new OverlaySnakeModel(source, null);
		model.setTime("xyz", "2h4m");
		assertThat(model.get("xyz")).isEqualTo("2h4m");
	}

}

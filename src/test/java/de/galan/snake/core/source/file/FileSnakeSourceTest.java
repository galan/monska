package de.galan.snake.core.source.file;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.StandardSystemProperty;
import com.google.common.io.Resources;

import de.galan.commons.test.AbstractTestParent;
import de.galan.commons.test.Tests;
import de.galan.commons.time.Sleeper;
import de.galan.snake.core.DefaultSnakeInstance;
import de.galan.snake.core.source.EventBean;
import de.galan.snake.core.source.TestSourceListener;
import de.galan.snake.core.source.UpdatedBean;


/**
 * CUT FileSnakeSource
 *
 * @author galan
 */
public class FileSnakeSourceTest extends AbstractTestParent {

	private FileSnakeSource source;
	private TestSourceListener listener;
	String dirTest;
	private String userPropertiesFile;


	@Before
	public void setup() {
		userPropertiesFile = StandardSystemProperty.USER_NAME.value() + ".properties";
		dirTest = Tests.getTestDirectory(true).getAbsolutePath();
		System.setProperty(DefaultSnakeInstance.SNAKE_DIR_CONFIGURATION, dirTest);
		source = new FileSnakeSource() {

			@Override
			protected void terminate(String message) {
				throw new RuntimeException(message);
			}

		};
		listener = new TestSourceListener();
		source.addListener(listener);
	}


	@Test
	public void noInstanceFile() throws Exception {
		try {
			source.initialize(new DefaultSnakeInstance());
			fail("should have terminated");
		}
		catch (RuntimeException rex) {
			assertThat(rex.getMessage()).isEqualTo("No snake instance properties-file exists.\n(searched in " + dirTest + "/instance.properties)");
		}
	}


	@Test
	public void removeInstanceFile() throws Exception {
		copy("instance.properties", "instance.properties");
		source.initialize(new DefaultSnakeInstance());
		remove("instance.properties");
		Sleeper.sleep("200ms");

		assertThat(listener.listAdded).hasSize(0);
		assertThat(listener.listRemoved).hasSize(2);
		assertThat(listener.listUpdated).hasSize(0);
		assertThat(listener.refreshed).isEqualTo(1);
		assertThat(listener.listRemoved).containsOnly(new EventBean("aaa", "111"), new EventBean("bbb", "222"));
	}


	@Test
	public void onlyInstanceFile() throws Exception {
		copy("instance.properties", "instance.properties");
		source.initialize(new DefaultSnakeInstance());
		assertThat(source.getProperties()).containsOnly(entry("aaa", "111"), entry("bbb", "222"));

		assertThat(listener.listAdded).hasSize(0);
		assertThat(listener.listRemoved).hasSize(0);
		assertThat(listener.listUpdated).hasSize(0);
		assertThat(listener.refreshed).isEqualTo(0);
	}


	@Test
	public void instanceAndUserProperties() throws Exception {
		copy("instance.properties", "instance.properties");
		copy("user.properties", userPropertiesFile);
		source.initialize(new DefaultSnakeInstance());
		assertThat(source.getProperties()).containsOnly(entry("aaa", "111"), entry("bbb", "333"), entry("ccc", "444"));

		assertThat(listener.listAdded).hasSize(0);
		assertThat(listener.listRemoved).hasSize(0);
		assertThat(listener.listUpdated).hasSize(0);
		assertThat(listener.refreshed).isEqualTo(0);
	}


	@Test
	public void onlyInstanceFileAddingUserFileLater() throws Exception {
		copy("instance.properties", "instance.properties");
		source.initialize(new DefaultSnakeInstance());
		copy("user.properties", userPropertiesFile);
		Sleeper.sleep("200ms");
		assertThat(source.getProperties()).containsOnly(entry("aaa", "111"), entry("bbb", "333"), entry("ccc", "444"));

		assertThat(listener.listAdded).hasSize(1);
		assertThat(listener.listRemoved).hasSize(0);
		assertThat(listener.listUpdated).hasSize(1);
		assertThat(listener.refreshed).isEqualTo(1);
		assertThat(listener.listAdded).containsOnly(new EventBean("ccc", "444"));
		assertThat(listener.listUpdated).containsOnly(new UpdatedBean("bbb", "333", "222"));
	}


	@Test
	public void removeUserPropertiesLater() throws Exception {
		copy("instance.properties", "instance.properties");
		copy("user.properties", userPropertiesFile);
		source.initialize(new DefaultSnakeInstance());
		remove(userPropertiesFile);
		Sleeper.sleep("200ms");
		assertThat(source.getProperties()).containsOnly(entry("aaa", "111"), entry("bbb", "222"));

		assertThat(listener.listAdded).hasSize(0);
		assertThat(listener.listRemoved).hasSize(1);
		assertThat(listener.listUpdated).hasSize(1);
		assertThat(listener.refreshed).isEqualTo(1);
		assertThat(listener.listRemoved).containsOnly(new EventBean("ccc", "444"));
		assertThat(listener.listUpdated).containsOnly(new UpdatedBean("bbb", "222", "333"));
	}


	protected void copy(String from, String to) throws IOException {
		FileUtils.copyFile(new File(Resources.getResource(getClass(), from).getFile()), new File(dirTest, to));
	}


	protected void remove(String file) {
		new File(dirTest, file).delete();
	}

}

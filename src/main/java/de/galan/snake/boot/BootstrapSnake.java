package de.galan.snake.boot;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import de.galan.commons.logging.BootstrapLogger;
import de.galan.commons.logging.Say;
import de.galan.commons.util.JvmUtils;
import de.galan.commons.util.MBeanUtil;
import de.galan.snake.client.Snake;
import de.galan.snake.core.DefaultSnakeInstance;
import de.galan.snake.core.OverlaySnakeModel;
import de.galan.snake.core.SnakeInstance;
import de.galan.snake.core.SnakeModel;
import de.galan.snake.core.SnakeSource;
import de.galan.snake.core.source.empty.EmptySnakeSource;
import de.galan.snake.core.source.file.FileSnakeSource;
import de.galan.snake.core.source.zookeeper.ZookeeperSnakeSource;
import de.galan.snake.management.SnakeManagement;


/**
 * Simple predefined standard bootstrapping for Snake.
 *
 * @author daniel
 */
public class BootstrapSnake {

	public static void init() {
		new BootstrapSnake().initializeSnake();
	}


	public void initializeSnake() {
		synchronized (BootstrapSnake.class) {
			if (Snake.getModel() != null) {
				Say.warn("Snake has been initialized already, doing nothing");
				return;
			}
			SnakeInstance instance = createInstance();
			// Set basic configuration-properties to Java's SystemProperties (in case they use the default values and are not defined),
			// other configuration-files or systems can benefit by substitute them (such as log4j2.xml)
			System.setProperty(DefaultSnakeInstance.SNAKE_BASE, instance.getDirectoryBase());
			System.setProperty(DefaultSnakeInstance.SNAKE_INSTANCE, instance.getInstance());
			System.setProperty("snake.dir.configuration", instance.getDirectoryConfiguration());
			System.setProperty("snake.dir.log", instance.getDirectoryLog());
			System.setProperty("snake.dir.script", instance.getDirectoryScript());
			System.setProperty("snake.dir.storage", instance.getDirectoryStorage());
			System.setProperty("snake.dir.temp", instance.getDirectoryTemp());

			createDirectories(instance);
			new BootstrapLogger().initializeLogger(instance.getDirectoryConfiguration() + BootstrapLogger.LOG4J2_XML);

			try {
				SnakeSource source = createSource();
				source.initialize(instance);
				SnakeModel model = new OverlaySnakeModel(source, instance);
				Snake.setModel(model);
			}
			catch (Throwable t) {
				Say.error("Failed initializing Snake", t);
				JvmUtils.terminate().message("Failed initializing Snake: " + t.getMessage()).returnCode(1).now();
			}
			MBeanUtil.registerMBean("de.galan", "snake", "Snake", new SnakeManagement());
		}
	}


	protected SnakeInstance createInstance() {
		return new DefaultSnakeInstance();
	}


	protected SnakeSource createSource() {
		// determine source by SystemProperties:
		// snake.source=file -> FileSnakeSource
		// snake.source=zk   -> ZookeeperSnakeSource
		// snake.source.zk.host=...
		SnakeSource result = null;
		String source = System.getProperty("snake.source", "file");
		if (StringUtils.equals(source, "zk")) {
			result = new ZookeeperSnakeSource();
		}
		else if (StringUtils.equals(source, "file")) {
			result = new FileSnakeSource();
		}
		else if (StringUtils.equals(source, "empty")) {
			result = new EmptySnakeSource();
		}
		else {
			throw new RuntimeException("Invalid source for system property 'snake.source':" + source); // TODO Exception handling
		}
		return result;
	}


	protected void createDirectories(SnakeInstance instance) {
		File dirBase = new File(instance.getDirectoryBase());
		if (!dirBase.isDirectory() || !dirBase.exists() || !dirBase.canExecute() || !dirBase.canWrite()) {
			JvmUtils.terminate().message("Instance-directory not available to write {" + dirBase + "}").now();
		}
		createDirectory(instance.getDirectoryConfiguration());
		createDirectory(instance.getDirectoryLog());
		createDirectory(instance.getDirectoryScript());
		createDirectory(instance.getDirectoryStorage());
		createDirectory(instance.getDirectoryTemp());
	}


	protected void createDirectory(String directory) {
		try {
			FileUtils.forceMkdir(new File(directory));
		}
		catch (IOException ex) {
			JvmUtils.terminate().message("Unable to create directory {" + directory + "}").now();
		}
	}

}

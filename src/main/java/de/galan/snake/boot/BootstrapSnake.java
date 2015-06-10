package de.galan.snake.boot;

import static org.apache.commons.lang3.StringUtils.*;

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
 * Simplified initialization for Snake using predefined builder.
 *
 * @author daniel
 */
public class BootstrapSnake {

	/** Create builder to set initialization parameter. */
	public static BootstrapBuilder build() {
		return new BootstrapBuilder();
	}


	/** Initialize Snake using the default initialization parameter and nothing else. */
	public static void initWithDefaults() {
		build().init();
	}

	public static class BootstrapBuilder {

		private boolean builderDirectories = true;
		private String builderBase;
		private String builderInstance;
		private String builderSource;
		private boolean builderLogging;


		public BootstrapBuilder setupDirectories(boolean directories) {
			builderDirectories = directories;
			return this;
		}


		public BootstrapBuilder setupLogging(boolean logging) {
			builderLogging = logging;
			return this;
		}


		public BootstrapBuilder base(String base) {
			builderBase = base;
			return this;
		}


		public BootstrapBuilder base(File base) {
			builderBase = base.getAbsolutePath();
			return this;
		}


		public BootstrapBuilder instance(String instance) {
			builderInstance = instance;
			return this;
		}


		public BootstrapBuilder source(String source) {
			builderSource = source;
			return this;
		}


		public void init() {
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
				System.setProperty(DefaultSnakeInstance.SNAKE_DIR_CONFIGURATION, instance.getDirectoryConfiguration());
				System.setProperty(DefaultSnakeInstance.SNAKE_DIR_LOG, instance.getDirectoryLog());
				System.setProperty(DefaultSnakeInstance.SNAKE_DIR_SCRIPT, instance.getDirectoryScript());
				System.setProperty(DefaultSnakeInstance.SNAKE_DIR_STORAGE, instance.getDirectoryStorage());
				System.setProperty(DefaultSnakeInstance.SNAKE_DIR_TEMP, instance.getDirectoryTemp());

				if (builderDirectories) {
					createDirectories(instance);
				}
				if (builderLogging) {
					new BootstrapLogger().initializeLogger(instance.getDirectoryConfiguration() + BootstrapLogger.LOG4J2_XML);
				}

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
			return new DefaultSnakeInstance(builderBase, builderInstance);
		}


		/** Determine source by builder property, else SystemProperties, else defaults to file */
		protected SnakeSource createSource() {
			SnakeSource result = null;
			String source = defaultString(builderSource, System.getProperty("snake.source", "file"));
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

}

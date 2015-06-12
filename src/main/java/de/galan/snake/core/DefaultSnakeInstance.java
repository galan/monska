package de.galan.snake.core;

import static org.apache.commons.lang3.StringUtils.*;


/**
 * daniel should have written a comment here.
 *
 * @author galan
 */
public class DefaultSnakeInstance implements SnakeInstance {

	public static final String SNAKE_INSTANCE = "snake.instance";
	public static final String SNAKE_BASE = "snake.base";
	public static final String SNAKE_DIR_CONFIGURATION = "snake.dir.configuration";
	public static final String SNAKE_DIR_LOG = "snake.dir.log";
	public static final String SNAKE_DIR_SCRIPT = "snake.dir.script";
	public static final String SNAKE_DIR_STORAGE = "snake.dir.storage";
	public static final String SNAKE_DIR_TEMP = "snake.dir.temp";

	public static final String WORK_DIRECTORY = System.getProperty("user.dir");
	public static final String DEFAULT_INSTANCE = "instance";

	private String snakeBase;
	private String snakeInstance;
	private String dirConfiguration;
	private String dirLog;
	private String dirScript;
	private String dirStorage;
	private String dirTemp;


	public DefaultSnakeInstance() {
		this(null, null);
	}


	public DefaultSnakeInstance(String directoryBase, String instance) {
		snakeBase = defaultString(directoryBase, System.getProperty(SNAKE_BASE, WORK_DIRECTORY));
		snakeInstance = defaultString(instance, System.getProperty(SNAKE_INSTANCE, DEFAULT_INSTANCE));
		dirConfiguration = postfixPath(System.getProperty(SNAKE_DIR_CONFIGURATION, SnakeInstance.super.getDirectoryConfiguration()));
		dirLog = postfixPath(System.getProperty(SNAKE_DIR_LOG, SnakeInstance.super.getDirectoryLog()));
		dirScript = postfixPath(System.getProperty(SNAKE_DIR_SCRIPT, SnakeInstance.super.getDirectoryScript()));
		dirStorage = postfixPath(System.getProperty(SNAKE_DIR_STORAGE, SnakeInstance.super.getDirectoryStorage()));
		dirTemp = postfixPath(System.getProperty(SNAKE_DIR_TEMP, SnakeInstance.super.getDirectoryTemp()));
	}


	protected String postfixPath(String path) {
		return !endsWith(path, FILE_SEPERATOR) ? path + FILE_SEPERATOR : path;
	}


	/**
	 * The base directory which is used in conjunction with the instance name.
	 *
	 * @return Path
	 */
	@Override
	public String getDirectoryBase() {
		return snakeBase;
	}


	/**
	 * The name of the instance, which is used to define the application-name and that acts as root for the standard
	 * directories required by an application
	 *
	 * @return The value of the property "snake.instance"
	 */
	@Override
	public String getInstance() {
		return snakeInstance;
	}


	@Override
	public String getDirectoryStorage() {
		return dirStorage;
	}


	@Override
	public String getDirectoryLog() {
		return dirLog;
	}


	@Override
	public String getDirectoryConfiguration() {
		return dirConfiguration;
	}


	@Override
	public String getDirectoryTemp() {
		return dirTemp;
	}


	@Override
	public String getDirectoryScript() {
		return dirScript;
	}

}

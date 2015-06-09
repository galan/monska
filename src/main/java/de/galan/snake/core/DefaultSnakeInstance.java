package de.galan.snake.core;

import static org.apache.commons.lang3.StringUtils.*;


/**
 * daniel should have written a comment here.
 *
 * @author daniel
 */
public class DefaultSnakeInstance implements SnakeInstance {

	public static final String SNAKE_INSTANCE = "snake.instance";
	public static final String SNAKE_BASE = "snake.base";

	public static final String WORK_DIRECTORY = System.getProperty("user.dir");
	public static final String DEFAULT_INSTANCE = "instance";

	private String snakeBase;
	private String snakeInstance;


	public DefaultSnakeInstance() {
		this(null, null);
	}


	public DefaultSnakeInstance(String directoryBase, String instance) {
		snakeBase = defaultString(directoryBase, System.getProperty(SNAKE_BASE, WORK_DIRECTORY));
		snakeInstance = defaultString(instance, System.getProperty(SNAKE_INSTANCE, DEFAULT_INSTANCE));
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

}

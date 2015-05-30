package de.galan.snake.core;

/**
 * daniel should have written a comment here.
 *
 * @author daniel
 */
public interface SnakeInstance {

	/** The platform dependent directory separator */
	public final static String FILE_SEPERATOR = System.getProperty("file.separator");


	/**
	 * The base directory which is used in conjunction with the instance name.
	 *
	 * @return Path
	 */
	public String getDirectoryBase();


	/**
	 * The name of the instance, which is used to define the application-name and that acts as root for the standard
	 * directories required by an application
	 *
	 * @return The value of the property "snake.instance"
	 */
	public String getInstance();


	/**
	 * Directory which is configured as base for further application specific directories.
	 *
	 * @return Absolute path of the base directory instance
	 */
	default String getDirectoryInstance() {
		return getDirectoryBase() + FILE_SEPERATOR + getInstance() + FILE_SEPERATOR;
	}


	/**
	 * This directory denotes the place for data in the application.
	 *
	 * @return Absolute path of the data directory
	 */
	default String getDirectoryStorage() {
		return getDirectoryInstance() + "storage" + FILE_SEPERATOR;
	}


	/**
	 * This directory denotes the place for a specific subdirectory within the directory for data (convenience method).
	 *
	 * @param subdirectory name
	 * @return Absolute path of the data directory
	 */
	default String getDirectoryStorage(String subdirectory) {
		return getDirectoryStorage() + subdirectory + FILE_SEPERATOR;
	}


	/**
	 * This directory denotes the place for logfiles.
	 *
	 * @return Absolute path of the log file directory
	 */
	default String getDirectoryLog() {
		return getDirectoryInstance() + "log" + FILE_SEPERATOR;
	}


	/**
	 * In this directory all application specific configuration is located.
	 *
	 * @return Absolute path of the configuration directory
	 */
	default String getDirectoryConfiguration() {
		return getDirectoryInstance() + "configuration" + FILE_SEPERATOR;
	}


	/**
	 * In this directory all temporarily created file are located. Could be deleted at any time.
	 *
	 * @return The absolute path of the temporary directory
	 */
	default String getDirectoryTemp() {
		return getDirectoryInstance() + "temp" + FILE_SEPERATOR;
	}


	/**
	 * In this directory all executable scripts are located.
	 *
	 * @return Absolute path of the script directory.
	 */
	default String getDirectoryScript() {
		return getDirectoryInstance() + "script" + FILE_SEPERATOR;
	}
}

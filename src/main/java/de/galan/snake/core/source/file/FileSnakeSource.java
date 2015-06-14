package de.galan.snake.core.source.file;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.common.base.StandardSystemProperty;

import de.galan.commons.io.file.FileListener;
import de.galan.commons.io.file.FilesystemObserver;
import de.galan.commons.logging.Say;
import de.galan.commons.util.JvmUtils;
import de.galan.snake.core.SnakeInstance;
import de.galan.snake.core.source.AbstractSnakeSource;


/**
 * Source for properties that come from two watched files, which has to placed in the configuration-directory. First the
 * properties from the file 'instance.properties' are loaded. The values can be overwritten using a user-specific file
 * with the name '&lt;username&gt;.properties' (where &lt;username&gt; is replaced with the currently logged in
 * username).<br/>
 * Changes in the files will be observed, so that the current properties will be refreshed and reflected in the source
 * during runtime.
 *
 * @author galan
 */
public class FileSnakeSource extends AbstractSnakeSource {

	public static final String PROPERTY_OBSERVE = "snake.source.file.observe";
	public static final String PROPERTY_INSTANCEFILE = "snake.source.file.instance";
	public static final String PROPERTY_USERFILE = "snake.source.file.user";
	public static final String PROPERTY_TRACK_ANY_CHANGE = "snake.source.file.trackAnyChange";

	private File fileInstance;
	private File fileUser;


	@Override
	public void initialize(SnakeInstance instance) {
		super.initialize(instance);
		fileInstance = determineInstanceFile(getInstance());
		fileUser = determineUserFile(getInstance());
		boolean observe = "true".equals(System.getProperty(PROPERTY_OBSERVE, "true"));
		if (observe) {
			try {
				//FilePropertySupplier fileSupplier = new FilePropertySupplier(fileInstance, fileUser);
				//RefreshPropertiesListener listener = new RefreshPropertiesListener(access, fileSupplier);
				PropertiesFileListener listener = new PropertiesFileListener();
				FilesystemObserver observer = new FilesystemObserver();
				observer.registerFileListener(listener, fileInstance);
				observer.registerFileListener(listener, fileUser);
			}
			catch (Exception ex) {
				terminate("Snake failed observing properties-file.");
			}
		}
		refresh(false);
	}


	protected void refresh(boolean notify) {
		Map<String, String> result = new HashMap<>();
		Properties properties = new Properties();
		properties.putAll(loadProperties(fileInstance));
		properties.putAll(loadProperties(fileUser));
		for (Object key: properties.keySet()) { // TODO filter defaults?
			// Whitespaces may lead to unrecognized properties
			result.put((String)key, trimToEmpty(properties.getProperty((String)key)));
		}
		mergeProperties(result, notify);
	}


	protected Properties loadProperties(File propertyFile) {
		Properties result = new Properties();
		if (propertyFile != null && propertyFile.exists()) {
			try (FileReader reader = new FileReader(propertyFile)) {
				result.load(reader);
			}
			catch (Exception ex) {
				Say.error("Snake properties file {} could not be read", ex, propertyFile.getName());
			}
		}
		return result;
	}


	protected File determineInstanceFile(SnakeInstance instance) {
		String defaultFile = instance.getDirectoryConfiguration() + "instance.properties";
		File result = new File(System.getProperty(PROPERTY_INSTANCEFILE, defaultFile));
		if (!result.exists() || result.isDirectory()) {
			terminate("No snake instance properties-file exists.\n(searched in " + result + ")");
		}
		return result;
	}


	protected void terminate(String message) {
		JvmUtils.terminate().message(message).returnCode(2).now();
	}


	protected File determineUserFile(SnakeInstance instance) {
		String defaultFile = instance.getDirectoryConfiguration() + StandardSystemProperty.USER_NAME.value() + ".properties";
		return new File(System.getProperty(PROPERTY_USERFILE, defaultFile));
	}

	/** Notification on changes for a file */
	private class PropertiesFileListener implements FileListener {

		@Override
		public void notifyFileCreated(File file) {
			if (isTrackingAll()) {
				refresh(true);
			}
		}


		@Override
		public void notifyFileChanged(File file) {
			refresh(true);
		}


		@Override
		public void notifyFileDeleted(File file) {
			if (isTrackingAll()) {
				refresh(true);
			}
		}


		// http://stackoverflow.com/questions/607435/why-does-vim-save-files-with-a-extension
		protected boolean isTrackingAll() {
			String modificationsOnly = System.getProperty(PROPERTY_TRACK_ANY_CHANGE, "true");
			return "true".equals(modificationsOnly);
		}

	}

}

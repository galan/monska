package de.galan.snake.core.source.zookeeper;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.RetryNTimes;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;

import de.galan.commons.logging.Say;
import de.galan.commons.time.HumanTime;
import de.galan.snake.core.SnakeInstance;
import de.galan.snake.core.source.AbstractSnakeSource;


/**
 * daniel should have written a comment here.
 *
 * @author galan
 */
public class ZookeeperSnakeSource extends AbstractSnakeSource {

	private static final String SEPARATOR = "/";
	public static final String PROPERTY_CONNECTION = "snake.source.zk.connection";
	public static final String PROPERTY_RETRY_SLEEP = "snake.source.zk.retrySleep";
	private static final String PROPERTY_PATH = "snake.source.zk.path";
	private static final String NAMESPACE = "snake";

	private CuratorFramework client;
	private String path;

	private List<NodeCache> caches;


	@Override
	public void initialize(SnakeInstance instance) {
		super.initialize(instance);
		caches = new ArrayList<>();
		String connection = System.getProperty(PROPERTY_CONNECTION, "localhost");
		String retrySleep = System.getProperty(PROPERTY_RETRY_SLEEP, "4s");
		RetryNTimes retry = new RetryNTimes(Integer.MAX_VALUE, HumanTime.dehumanizeTime(retrySleep).intValue());
		//.namespace(NAMESPACE)
		client = CuratorFrameworkFactory.builder().connectString(connection).canBeReadOnly(true).retryPolicy(retry).build(); // TODO create more intervention possiblities
		client.start();
		path = System.getProperty(PROPERTY_PATH, SEPARATOR + "undefined");
		try {
			readProperties();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}


	protected String getPath() {
		return path;
	}


	protected CuratorFramework getClient() {
		return client;
	}


	protected List<NodeCache> getCaches() {
		return caches;
	}


	protected void readProperties() throws Exception {
		List<String> nodes = Splitter.on(SEPARATOR).omitEmptyStrings().trimResults().splitToList(NAMESPACE + SEPARATOR + getPath());
		StringBuilder currentPath = new StringBuilder();
		for (String node: nodes) {
			currentPath.append(SEPARATOR).append(node);
			NodeCache cache = new NodeCache(client, currentPath.toString());
			getCaches().add(cache);
			cache.getListenable().addListener(() -> recalculateProperties(true));
			cache.start(true);
		}
		if (!recalculateProperties(false)) {
			throw new Exception("Unable to parse data from znode");
		}
	}


	protected boolean recalculateProperties(boolean notify) {
		Map<String, String> newProperties = new HashMap<>();
		for (NodeCache cache: getCaches()) {
			ChildData data = cache.getCurrentData();
			if (data == null) {
				break;
			}
			String propertyString = new String(data.getData(), Charsets.UTF_8);
			Properties props = new Properties();
			try {
				props.load(new StringReader(propertyString));
				for (Entry<Object, Object> entry: props.entrySet()) {
					newProperties.put((String)entry.getKey(), (String)entry.getValue());
				}
			}
			catch (IOException ex) {
				Say.warn("Unable to parse data from znode '" + data.getPath() + "'", ex);
				return false;
			}
		}
		mergeProperties(newProperties, notify);
		return true;
	}

}

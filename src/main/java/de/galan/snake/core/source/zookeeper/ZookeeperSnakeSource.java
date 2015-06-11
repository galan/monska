package de.galan.snake.core.source.zookeeper;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.RetryNTimes;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;

import de.galan.snake.core.SnakeInstance;
import de.galan.snake.core.source.AbstractSnakeSource;


/**
 * daniel should have written a comment here.
 *
 * @author galan
 */
public class ZookeeperSnakeSource extends AbstractSnakeSource {

	private static final String NAMESPACE = "snake";
	private CuratorFramework client;
	private String path;

	private List<NodeCache> caches;


	@Override
	public void initialize(SnakeInstance instance) {
		super.initialize(instance);
		caches = new ArrayList<>();
		String connection = System.getProperty("snake.source.zk.connection", "localhost");
		RetryNTimes retry = new RetryNTimes(Integer.MAX_VALUE, 4000);
		//.namespace(NAMESPACE)
		client = CuratorFrameworkFactory.builder().connectString(connection).canBeReadOnly(true).retryPolicy(retry).build(); // TODO create more intervention possiblities
		client.start();
		path = System.getProperty("snake.source.zk.path", "/undefined");
		try {
			readProperties();
		}
		catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}


	protected CuratorFramework getClient() {
		return client;
	}


	protected List<NodeCache> getCaches() {
		return caches;
	}


	protected void readProperties() throws Exception {
		List<String> nodes = Splitter.on("/").omitEmptyStrings().splitToList(NAMESPACE + "/" + path);
		StringBuilder currentPath = new StringBuilder();
		for (String node: nodes) {
			currentPath.append("/").append(node);
			NodeCache cache = new NodeCache(client, currentPath.toString());
			getCaches().add(cache);
			cache.getListenable().addListener(() -> recalculateProperties(true));
			cache.start(true);
		}
		recalculateProperties(false);
	}


	protected void recalculateProperties(boolean notify) {
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
				Map<String, String> map = new HashMap<String, String>();
				props.keySet().forEach(key -> map.put((String)key, (String)props.get(key)));
				newProperties.putAll(map);
			}
			catch (IOException ex) {
				//Say.warn("Unspecified error from daniel", ex);
			}

		}
		mergeProperties(newProperties, notify);
	}

}

package hr.best.aibg2018.logic.entites;

import hr.best.aibg2018.logic.entites.properties.IProperty;
import java.util.HashMap;
import java.util.Set;

public class Entity {

	private java.util.Map<String, IProperty> properties;

	public Entity() {
		properties = new HashMap<>();
	}

	public void addProperty(String key, IProperty property) {
		properties.put(key, property);
	}

	public IProperty getProperty(String key) {
		return properties.get(key);
	}

	public Entity cloneEntity() {
		Entity entity = new Entity();
		Set<String> keys = properties.keySet();

		for (String k : keys) {
			entity.properties.put(k, properties.get(k).cloneProperty());
		}

		return entity;
	}
}
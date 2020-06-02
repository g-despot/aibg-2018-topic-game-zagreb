package hr.best.aibg2018.logic.map;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class Tile implements Serializable {

	TileType type;
	ItemType item;

	public Tile(TileType type) {
		this.type = type;
	}

	public Tile() {
		this(TileType.NORMAL);
	}

	@Override
	public String toString() {
		if (type.equals(TileType.WATER)) {
			return ("w");
		} else if (type.equals(TileType.GRASS)) {
			return ("g");
		} else if (type.equals(TileType.FIRE)) {
			return ("f");
		}
		return ("o");

	}

	@JsonIgnore
	public boolean isObstacle() {
		if (item != null && item.equals(ItemType.OBSTACLE)) {
			return true;
		}
		return false;
	}

	public void setType(TileType type) {
		this.type = type;
	}

	public TileType getType() {
		return type;
	}

	public void setItem(ItemType item) {
		this.item = item;
	}

	public ItemType getItem() {
		return item;
	}
}

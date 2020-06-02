package hr.best.aibg2018.logic.map;

import hr.best.aibg2018.logic.entites.Player;
import hr.best.aibg2018.logic.game.ConfigException;
import hr.best.aibg2018.logic.game.GameParameters;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Map of tiles where x coordinate ranges from [0,width-1] and y ranges from
 * [0,height-1]. It is expected to load the map from a txt file
 */
@SuppressWarnings("serial")
public class Map implements Serializable {

	private Tile[][] tiles;
	private int width;
	private int height;
	private int shrinkProgress;
	private int shrinkOffset;

	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new Tile[height][width];

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				tiles[i][j] = new Tile();
			}
		}
	}

	/**
	 * Gets a config file that is parsed as a map. Expects:
	 *
	 * o - normal tile
	 *
	 * ...
	 *
	 * Expects a valid file that contains rows with equal lengths.
	 */
	public Map(Path configFile) {
		// PROVJERE
		List<String> lines;
		try {
			lines = Files.readAllLines(configFile);
		} catch (IOException e) {
			throw new ConfigException(e);
		}
		if (lines.size() == 0) {
			throw new ConfigException("Empty file found on: " + configFile.toString());

		}

		if (!validLines(lines)) {
			throw new ConfigException("Lines are not of equal length in: " + configFile.toString());
		}

		height = lines.size();
		width = lines.get(0).length();

		if (width != 40 || height != 20) {
			throw new ConfigException("Map configuration is not 20x20!");
		}

		tiles = new Tile[height][width / 2];
		shrinkProgress = 0;
		shrinkOffset = 0;

		for (int i = 0; i < height; i++) {
			String line = lines.get(i);
			char[] row = line.toCharArray();

			for (int j = 0; j < width; j++) {
				char c = row[j];

				// The type of the tiles is defined and if there are items on them.
				switch (c) {
				case ('o'):
					tiles[i][j / 2] = new Tile();
					break;
				case ('w'):
					tiles[i][j / 2] = new Tile(TileType.WATER);
					break;
				case ('f'):
					tiles[i][j / 2] = new Tile(TileType.FIRE);
					break;
				case ('g'):
					tiles[i][j / 2] = new Tile(TileType.GRASS);
					break;
				case ('0'):
					tiles[i][(int) (Math.round(j / 2.0) - 1)].setItem(null);
					break;
				case ('1'):
					tiles[i][(int) (Math.round(j / 2.0) - 1)].setItem(ItemType.FIRE);
					break;
				case ('2'):
					tiles[i][(int) (Math.round(j / 2.0) - 1)].setItem(ItemType.GRASS);
					break;
				case ('3'):
					tiles[i][(int) (Math.round(j / 2.0) - 1)].setItem(ItemType.WATER);
					break;
				case ('4'):
					tiles[i][(int) (Math.round(j / 2.0) - 1)].setItem(ItemType.OBSTACLE);
					break;
				default:
					throw new ConfigException("Illegal character found: " + c);
				}
			}
		}
		System.out.println("Ucitao sam mapu!");
	}

	/**
	 * Checks if all lines are of same length
	 */
	private boolean validLines(List<String> lines) {
		int supposedWidth = lines.get(0).length();
		for (String line : lines) {
			if (line.length() != supposedWidth) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		// Giving impossible coordinated to get a clear map.
		return stateWithPlayersOn(-1, -1, -1, -1);
	}

	/**
	 * Returns a string representation of object with players on (x1,y1) and
	 * (x2,y2).
	 */
	public String stateWithPlayersOn(int x1, int y1, int x2, int y2) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[0].length; j++) {

				if (j == x1 && i == y1) {
					sb.append("1"); // Player 1
				} else {
					if (j == x2 && i == y2) {
						sb.append("2"); // Player 2
					} else {
						Tile tile = tiles[i][j];
						String toAppend;
						if (tile.getItem() != null && tile.getItem().equals(ItemType.OBSTACLE)) {
							toAppend = "x";
						} else {
							toAppend = tile.toString();
						}
						sb.append(toAppend); // Let the tile print
						// itself out
					}

				}
			}

			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Returns whether or not a player can move to a position (x,y)
	 */
	public boolean validPosition(int x, int y) {

		// Checking if the player wants to make a move outside of the map.
		if (x < 0 || y < 0 || x >= width / 2 || y >= height) {
			return false;
		}

		// Checking if the tile has an obstacle on it.
		if (tiles[y][x].isObstacle()) {
			return false;
		}

		return true;
	}

	public boolean shrink(Player player1, Player player2) {
		if (shrinkOffset % GameParameters.SHRINK_OFFSET == 0) {
			shrinkOffset++;
			for (int i = shrinkProgress; i < height; i++) {
				for (int j = 0; j < width / 2; j++) {
					if (i == shrinkProgress || i == (height - 1 - shrinkProgress) || j == shrinkProgress
							|| j == (height - 1 - shrinkProgress)) {
						tiles[i][j].setItem(ItemType.OBSTACLE);
					}
				}
			}

			if (player1.getInitX() < player2.getInitX()) {
				player1.setInitX(player1.getInitX() + 1);
				player1.setInitY(player1.getInitY() + 1);
				player2.setInitX(player2.getInitX() - 1);
				player2.setInitY(player2.getInitY() - 1);
			} else {
				player1.setInitX(player1.getInitX() - 1);
				player1.setInitY(player1.getInitY() - 1);
				player2.setInitX(player2.getInitX() + 1);
				player2.setInitY(player2.getInitY() + 1);
			}

			if (tiles[player1.getX()][player1.getY()].getItem() == ItemType.OBSTACLE) {
				if (player2.getX() == player1.getInitX() && player2.getY() == player1.getInitY()) {
					player1.resetPosition(player2.getInitX(), player2.getInitY());
				} else {
					player1.resetPosition();
				}
			}

			if (tiles[player2.getX()][player2.getY()].getItem() == ItemType.OBSTACLE) {
				if (player1.getX() == player2.getInitX() && player1.getY() == player2.getInitY()) {
					player2.resetPosition(player1.getInitX(), player1.getInitY());
				} else {
					player2.resetPosition();
				}
			}

			shrinkProgress++;

			return shrinkProgress >= width / 4 || shrinkProgress >= height / 2;
		} else {
			shrinkOffset++;
			return false;
		}
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public Tile getTile(int x, int y) {
		return tiles[y][x];
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
}

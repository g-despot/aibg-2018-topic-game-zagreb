package hr.best.aibg2018.logic.game;

import hr.best.aibg2018.logic.map.Map;
import hr.best.aibg2018.logic.map.Tile;
import hr.best.aibg2018.logic.map.ItemType;
import hr.best.aibg2018.logic.entites.Player;
import hr.best.aibg2018.logic.actions.Direction;
import hr.best.aibg2018.logic.entites.MonsterType;
import java.io.Serializable;
import java.util.Random;

/**
 * Represents a game. Game can move players around the map. It is responsible
 * for being the connection between the players and the map.
 */
@SuppressWarnings("serial")
public class Game implements Serializable {

	private final int id;
	public Integer winner;

	private int nextPlayer;

	private Map map;

	private Player player1;

	private Player player2;

	private int turn;

	public Game(Map map) {
		this(map, null, null);
	}

	public Game(Map map, int id) {
		this(map, null, null, id);
	}

	public Game(Map map, Player player1, Player player2) {
		this(map, player1, player2, randomId());
	}

	private static int randomId() {
		return (new Random()).nextInt(Integer.MAX_VALUE);
	}

	public Game(Map map, Player player1, Player player2, int id) {
		this.player1 = player1;
		this.player2 = player2;
		this.map = map;
		this.id = id;
		nextPlayer = 1;
		winner = null;
		turn = 0;
	}

	public boolean doAction(String action, int playerId) {
		if (winner != null) {
			throw new GameFinishedException("The game is finished.");
		}

		if (player1 == null || player2 == null) {
			throw new ConfigException("Players not defined.");
		}

		int player;
		if (nextPlayer == 1 && player1.getId() == playerId) {
			player = 1;
		} else if (nextPlayer == 2 && player2.getId() == playerId) {
			player = 2;
		} else {
			throw new GameException("Player " + playerId + " not part of the game.");
		}

		player1.resetHealthChange();
		player2.resetHealthChange();
		boolean actionSuccess;
		switch (action) {
		case "w":
			actionSuccess = move(Direction.NORTH, player, action);
			break;
		case "s":
			actionSuccess = move(Direction.SOUTH, player, action);
			break;
		case "a":
			actionSuccess = move(Direction.WEST, player, action);
			break;
		case "d":
			actionSuccess = move(Direction.EAST, player, action);
			break;
		case "wd":
			actionSuccess = move(Direction.NORTHEAST, player, action);
			break;
		case "wa":
			actionSuccess = move(Direction.NORTHWEST, player, action);
			break;
		case "sd":
			actionSuccess = move(Direction.SOUTHEAST, player, action);
			break;
		case "sa":
			actionSuccess = move(Direction.SOUTHWEST, player, action);
			break;
		case "rw":
			actionSuccess = rangedAttack(Direction.NORTH, player, action);
			break;
		case "rs":
			actionSuccess = rangedAttack(Direction.SOUTH, player, action);
			break;
		case "ra":
			actionSuccess = rangedAttack(Direction.WEST, player, action);
			break;
		case "rd":
			actionSuccess = rangedAttack(Direction.EAST, player, action);
			break;
		case "mf":
			actionSuccess = morph(MonsterType.FIRE, player, action);
			break;
		case "mw":
			actionSuccess = morph(MonsterType.WATER, player, action);
			break;
		case "mg":
			actionSuccess = morph(MonsterType.GRASS, player, action);
			break;
		case "mn":
			actionSuccess = morph(MonsterType.NEUTRAL, player, action);
			break;
		default:
			actionSuccess = false;
		}

		if (!actionSuccess) {
			action = null;
		}

		nextPlayer = (nextPlayer % 2) + 1;

		if (player1.getHealth() <= 0) {
			player1.setLives(player1.getLives() - 1);
			player2.setKills(player2.getKills() + 1);
			if (player1.getLives() != 0) {
				player1.reset();
				player2.reset();
			}
		} else if (player2.getHealth() <= 0) {
			player2.setLives(player2.getLives() - 1);
			player1.setKills(player1.getKills() + 1);
			if (player2.getLives() != 0) {
				player1.reset();
				player2.reset();
			}
		}

		if (player1.getLives() == 0) {
			winner = 2;
		} else if (player2.getLives() == 0) {
			winner = 1;
		}

		turn++;

		if (turn >= GameParameters.MAX_TURNS) {
			if (map.shrink(player1, player2)) {
				if (player1.getKills() > player2.getKills()) {
					winner = 1;
				} else if (player1.getKills() < player2.getKills()) {
					winner = 2;
				} else {
					if (player1.getHealth() > player2.getHealth()) {
						winner = 1;
					} else if (player1.getHealth() < player2.getHealth()) {
						winner = 2;
					} else {
						winner = 0;
						System.out.println(winner);
					}
				}
			}
		}

		return actionSuccess;
	}

	/**
	 * Move a player "player" in direction "d"
	 *
	 * @param player if 1 given then player 1 is moved... if any other number then
	 *               player 2 is moved.
	 * @return false if cannot be moved or true if moved successfully.
	 */
	public boolean move(Direction d, int player, String action) {
		if (player1 == null || player2 == null) {
			throw new ConfigException("Players not defined.");
		}
		Player active = player == 1 ? player1 : player2;
		Player passive = player == 1 ? player2 : player1;

		active.passTime();

		int newX = active.getX() + d.dx;
		int newY = active.getY() + d.dy;

		if (!map.validPosition(newX, newY)) {
			active.setLastAction(null);
			return false;
		}
		if (passive.getX() == newX && passive.getY() == newY) {
			passive.takeMeleeDamage();
			active.setLastAction("melee_" + action);
			return true;
		}

		active.move(d);

		Tile newTile = map.getTile(newX, newY);
		active.terrainEffect(newTile.getType());

		ItemType item = newTile.getItem();
		if ((item != null) && (item != ItemType.OBSTACLE)) {
			active.addMorphItem(item);
		}

		active.setLastAction(action);
		return true;
	}

	/**
	 * Morph player "playerNumber" into type "newType"
	 *
	 * @return false if player failed to morph, true otherwise
	 */
	public boolean morph(MonsterType newType, int playerNumber, String action) {
		if (player1 == null || player2 == null) {
			throw new ConfigException("Players not defined.");
		}
		Player player = playerNumber == 1 ? player1 : player2;

		player.passTime();

		if (player.isAbleToMorph(newType) && !player.getType().equals(newType)) {
			player.morph(newType);
			player.setLastAction(action);
			return true;
		} else {
			player.setLastAction(null);
			return false;
		}

	}

	/**
	 * Preform a ranged attack by player "playerNumber" in direction "d"
	 *
	 * @param playerNumber if 1 given then player 1 attacks... if any other number
	 *                     then player 2 attacks.
	 * @return false if the ranged attack failed, true otherwise
	 */
	public boolean rangedAttack(Direction d, int playerNumber, String action) {
		if (player1 == null || player2 == null) {
			throw new ConfigException("Players not defined.");
		}
		Player active = playerNumber == 1 ? player1 : player2;
		Player passive = playerNumber == 1 ? player2 : player1;

		active.passTime();

		if (active.getType().equals(MonsterType.NEUTRAL)) {
			active.setLastAction(null);
			return false;
		}

		int activeAttackX = active.getX();
		int activeAttackY = active.getY();

		for (int i = 0; i < GameParameters.RANGE_DISTANCE; i++) {
			activeAttackX += d.dx;
			activeAttackY += d.dy;

			if (!map.validPosition(activeAttackX, activeAttackY)) {
				active.setLastAction(null);
				return false;
			}

			if (activeAttackX == passive.getX() && activeAttackY == passive.getY()) {
				passive.takeRangedDamage(active);
				active.setLastAction(action);
				return true;
			}
		}

		active.setLastAction(null);
		return false;
	}

	public Map getMap() {
		return map;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public int getId() {
		return id;
	}

	public Player getNextPlayer() {
		if (player1 == null || player2 == null) {
			throw new ConfigException("Players not defined.");
		}

		return (nextPlayer == 1) ? player1 : player2;
	}

	/**
	 * @return string representation of the game where numbers 1 and 2 represent
	 *         players.
	 */
	public String currentState() {
		return map.stateWithPlayersOn(player1.getX(), player1.getY(), player2.getX(), player2.getY());
	}

	public int getTurn() {
		return this.turn;
	}

	@Override
	public String toString() {
		return currentState();
	}

}

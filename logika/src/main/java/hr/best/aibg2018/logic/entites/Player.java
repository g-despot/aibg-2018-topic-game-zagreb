package hr.best.aibg2018.logic.entites;

import hr.best.aibg2018.logic.game.GameParameters;
import hr.best.aibg2018.logic.map.ItemType;
import hr.best.aibg2018.logic.map.TileType;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import hr.best.aibg2018.logic.actions.Direction;
import hr.best.aibg2018.logic.actions.IMovable;

/**
 * Represents a player object
 */
public class Player implements IMovable {

	@JsonIgnore
	private final int id;

	private int x;
	private int y;

	@JsonIgnore
	private int initX;
	@JsonIgnore
	private int initY;

	private MonsterType type;
	private List<ItemType> morphItems;
	private int timeSinceMorphing;

	private int health;
	private int lives;
	private int kills;

	private String lastAction;
	private int healthChange;

	private String teamName;

	/**
	 * Will initialize player to NEUTRAL type
	 *
	 * @param x the x coordinate of the player
	 * @param y the y coordinate of the player
	 */
	public Player(int x, int y, int id) {
		this(x, y, MonsterType.NEUTRAL, id, null);
	}

	/**
	 * Will initialize player with Team name
	 *
	 * @param x the x coordinate of the player
	 * @param y the y coordinate of the player
	 */
	public Player(int x, int y, int id, String teamName) {
		this(x, y, MonsterType.NEUTRAL, id, teamName);
	}

	/**
	 * Initializes player object
	 *
	 * @param x    the x coordinate of the player
	 * @param y    the y coordinate of the player
	 * @param type the initial type of the player
	 */
	public Player(int x, int y, MonsterType type, int id, String teamName) {
		this.x = x;
		this.y = y;
		this.initX = x;
		this.initY = y;
		this.type = type;
		this.id = id;
		this.health = GameParameters.MAX_HEALTH;
		this.timeSinceMorphing = GameParameters.MORPHING_BREAK_LENGTH;
		this.morphItems = new ArrayList<ItemType>();
		this.lives = GameParameters.LIVES;
		this.kills = 0;
		this.lastAction = null;
		this.healthChange = 0;
		this.teamName = teamName;
	}

	/**
	 * @return x coordinate of the player
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return y coordinate of the player
	 */
	public int getY() {
		return y;
	}

	public int getInitX() {
		return initX;
	}

	public void setInitX(int initX) {
		this.initX = initX;
	}

	public int getInitY() {
		return initY;
	}

	public void setInitY(int initY) {
		this.initY = initY;
	}

	/**
	 * @return current player's health
	 */
	public int getHealth() {
		return health;
	}

	public int getId() {
		return id;
	}

	public boolean move(Direction d) {
		x = x + d.dx;
		y = y + d.dy;
		return true;
	}

	public MonsterType getType() {
		return type;
	}

	public void morph(MonsterType type) {
		this.type = type;
		timeSinceMorphing = 0;
		removeMorphItem(MonsterTypeUtils.determineMorphItemRequirement(type));
	}

	public boolean isAbleToMorph(MonsterType morphType) {

		return timeSinceMorphing >= GameParameters.MORPHING_BREAK_LENGTH
				&& (morphItems.contains(MonsterTypeUtils.determineMorphItemRequirement(morphType))
						|| morphType == MonsterType.NEUTRAL);
	}

	public void passTime() {
		timeSinceMorphing++;
	}

	public void takeMeleeDamage() {
		health -= GameParameters.MELEE_DAMAGE;
		healthChange -= GameParameters.MELEE_DAMAGE;
	}

	public void takeRangedDamage(Player opponent) {
		int multiplierIndex = MonsterTypeUtils.determineStronger(type, opponent.type);

		health -= GameParameters.BASE_DAMAGE * GameParameters.RANGED_DAMAGE_MULTIPLIERS.get(multiplierIndex);
		healthChange -= GameParameters.BASE_DAMAGE * GameParameters.RANGED_DAMAGE_MULTIPLIERS.get(multiplierIndex);
	}

	public void terrainEffect(TileType tileType) {
		int terrainEffectValue = MonsterTypeUtils.determineTerrainEffect(type, tileType);
		// Limiting health to [0, MAX_HEALTH]
		int newHealth = Math.max(Math.min(health + terrainEffectValue, GameParameters.MAX_HEALTH), 0);
		healthChange = newHealth - health;
		health = newHealth;
	}

	public void addMorphItem(ItemType morphItem) {
		this.morphItems.add(morphItem);
	}

	public void removeMorphItem(ItemType morphItem) {
		this.morphItems.remove(morphItem);
	}

	public List<ItemType> getMorphItems() {
		return this.morphItems;
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public void reset() {
		this.x = initX;
		this.y = initY;
		this.type = MonsterType.NEUTRAL;
		this.health = GameParameters.MAX_HEALTH;
		this.timeSinceMorphing = GameParameters.MORPHING_BREAK_LENGTH;
		this.morphItems = new ArrayList<ItemType>();
	}

	public void resetPosition() {
		this.x = initX;
		this.y = initY;
	}

	public void resetPosition(int oppositePlayerX, int oppositePlayerY) {
		this.x = oppositePlayerX;
		this.y = oppositePlayerY;
	}

	public void resetHealthChange() {
		healthChange = 0;
	}

	public int getHealthChange() {
		return healthChange;
	}

	public void setHealthChange(int healthChange) {
		this.healthChange = healthChange;
	}

	public String getLastAction() {
		return lastAction;
	}

	public void setLastAction(String lastAction) {
		this.lastAction = lastAction;
	}

	public String getTeamName() {
		return teamName;
	}
}

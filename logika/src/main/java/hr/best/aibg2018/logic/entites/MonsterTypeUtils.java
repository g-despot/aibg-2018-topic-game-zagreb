package hr.best.aibg2018.logic.entites;

import hr.best.aibg2018.logic.game.GameParameters;
import hr.best.aibg2018.logic.map.ItemType;
import hr.best.aibg2018.logic.map.TileType;

/**
 * This class helps with working with monster types. Provides functionalities
 * such as determining which one of two monsters is stronger by type
 */
public class MonsterTypeUtils {

	/**
	 * Determines which one of the two monster types has the upper hand
	 *
	 * @param type1 type of the first monster
	 * @param type2 type of the second monster
	 * @return 1 if the first one is stronger, 2 if the second one is stronger and 0
	 *         if any of them is neutral type or they're the same. If something went
	 *         wrong, will return -1
	 */
	public static int determineStronger(MonsterType type1, MonsterType type2) {
		if (type1.equals(MonsterType.NEUTRAL) || type2.equals(MonsterType.NEUTRAL)) {
			return 0;
		}

		if (type1.equals(type2)) {
			return 3;
		}

		switch (type1) {
		case GRASS:
			return type2.equals(MonsterType.WATER) ? 1 : 2;
		case WATER:
			return type2.equals(MonsterType.FIRE) ? 1 : 2;
		case FIRE:
			return type2.equals(MonsterType.GRASS) ? 1 : 2;
		case NEUTRAL:
		default:
			// fall through
		}

		return -1;
	}

	public static ItemType determineMorphItemRequirement(MonsterType type) {
		switch (type) {
		case GRASS:
			return ItemType.GRASS;
		case WATER:
			return ItemType.WATER;
		case FIRE:
			return ItemType.FIRE;
		case NEUTRAL:
		default:
			// fall through
		}
		return null;
	}

	public static int determineTerrainEffect(MonsterType monsterType, TileType tileType) {
		switch (tileType) {
		case GRASS:
			if (monsterType == MonsterType.GRASS) {
				return GameParameters.TERRAIN_EFFECT_HEAL;
			} else if (monsterType == MonsterType.WATER) {
				return GameParameters.TERRAIN_EFFECT_DAMAGE;
			}
			break;
		case WATER:
			if (monsterType == MonsterType.WATER) {
				return GameParameters.TERRAIN_EFFECT_HEAL;
			} else if (monsterType == MonsterType.FIRE) {
				return GameParameters.TERRAIN_EFFECT_DAMAGE;
			}
			break;
		case FIRE:
			if (monsterType == MonsterType.FIRE) {
				return GameParameters.TERRAIN_EFFECT_HEAL;
			} else if (monsterType == MonsterType.GRASS) {
				return GameParameters.TERRAIN_EFFECT_DAMAGE;
			}
			break;
		default:
			return 0;
		}
		return 0;
	}
}

package hr.best.aibg2018.logic.game;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameParameters {

	public static final int RANGE_DISTANCE = 3;
	public static final int MAX_HEALTH = 100;
	public static final int MELEE_DAMAGE = 10;
	public static final int BASE_DAMAGE = 10;
	public static final int MORPHING_BREAK_LENGTH = 7;
	public static final List<Double> RANGED_DAMAGE_MULTIPLIERS = Collections
			.unmodifiableList(Arrays.asList(1.5, 0.5, 2.0, 1.0));
	public static final int TERRAIN_EFFECT_HEAL = 1;
	public static final int TERRAIN_EFFECT_DAMAGE = -1;
	public static final int LIVES = 5;
	public static final int MAX_TURNS = 300;
	public static final int SHRINK_OFFSET = 25;

}

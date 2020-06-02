package hr.best.aibg2018.bots;

import hr.best.aibg2018.logic.game.GameParameters;
import hr.best.aibg2018.logic.entites.MonsterType;

import java.util.*;

public class NotSoRandomBot implements IBot {

	// TOTO remove if unnecessary
	@SuppressWarnings("unused")
	private final int gameId;
	@SuppressWarnings("unused")
	private final int playerId;

	private int timeSinceMorph = GameParameters.MORPHING_BREAK_LENGTH;
	private MonsterType type = MonsterType.NEUTRAL;

	private Random random = new Random();
	private List<String> moveActions = Collections.unmodifiableList(Arrays.asList("w", "s", "a", "d"));
	private List<String> attackActions = Collections.unmodifiableList(Arrays.asList("rw", "rs", "ra", "rd"));
	private List<String> morphActions = Collections.unmodifiableList(Arrays.asList("mf", "mw", "mg", "mn"));

	public NotSoRandomBot(int gameId, int playerId) {
		this.gameId = gameId;
		this.playerId = playerId;
	}

	private String getAction() {
		List<String> actions = new ArrayList<>();
		actions.addAll(moveActions);
		if (timeSinceMorph >= GameParameters.MORPHING_BREAK_LENGTH) {
			actions.addAll(morphActions);
		}
		if (!type.equals(MonsterType.NEUTRAL)) {
			actions.addAll(attackActions);
		}

		String action = actions.get(random.nextInt(actions.size()));

		if (action.charAt(0) == 'm') {
			switch (action.charAt(1)) {
			case 'f':
				type = MonsterType.FIRE;
				break;
			case 'w':
				type = MonsterType.WATER;
				break;
			case 'g':
				type = MonsterType.GRASS;
				break;
			case 'n':
				type = MonsterType.NEUTRAL;
				break;
			}
			timeSinceMorph = 0;
		}

		timeSinceMorph++;
		return action;
	}

	public String act(String game) {
		return getAction();
	}

}

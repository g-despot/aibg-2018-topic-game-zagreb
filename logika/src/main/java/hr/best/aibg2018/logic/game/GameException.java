package hr.best.aibg2018.logic.game;

/**
 * Cool exception for reading config files
 */
public class GameException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GameException(String s) {
		super(s);
	}

	public GameException(Exception e) {
		super(e);
	}
}

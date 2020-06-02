package hr.best.aibg2018.logic.game;

/**
 * Cool exception for reading config files
 */
public class GameFinishedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GameFinishedException(String s) {
		super(s);
	}

	public GameFinishedException(Exception e) {
		super(e);
	}
}

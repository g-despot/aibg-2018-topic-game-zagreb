package hr.best.aibg2018.logic.game;

/**
 * Cool exception for reading config files
 */
public class ConfigException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConfigException(String s) {
		super(s);
	}

	public ConfigException(Exception e) {
		super(e);
	}
}

package hr.best.aibg2018.logic.actions;

/**
 * Interface represents movable object
 */
public interface IMovable {

	/**
	 * Objects has to move in a direction d
	 *
	 * @param d direction in which object moves
	 * @return true if move was successful, and false if not
	 */
	public boolean move(Direction d);
}

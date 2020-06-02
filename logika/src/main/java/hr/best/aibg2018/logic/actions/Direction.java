package hr.best.aibg2018.logic.actions;

public enum Direction {
	NORTH(0, -1), EAST(1, 0), SOUTH(0, 1), WEST(-1, 0), NORTHEAST(1, -1), NORTHWEST(-1, -1), SOUTHEAST(1, 1),
	SOUTHWEST(-1, 1);

	public int dx;
	public int dy;

	Direction(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
}

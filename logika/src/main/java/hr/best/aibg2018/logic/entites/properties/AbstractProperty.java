package hr.best.aibg2018.logic.entites.properties;

public abstract class AbstractProperty implements IProperty {

	protected PropertyType type;

	protected AbstractProperty(PropertyType type) {
		this.type = type;
	}

	@Override
	public PropertyType getType() {
		return type;
	}

	@Override
	public boolean getBool() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getDouble() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getString() {
		// TODO Auto-generated method stub
		return null;
	}
}

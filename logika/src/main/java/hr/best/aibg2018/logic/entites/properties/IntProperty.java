package hr.best.aibg2018.logic.entites.properties;

public class IntProperty extends AbstractProperty {

	private int value;

	public IntProperty(int value) {
		super(PropertyType.PROPERTY_INT);
		this.value = value;
	}

	@Override
	public int getInt() {
		return value;
	}

	@Override
	public IProperty cloneProperty() {
		return new IntProperty(value);
	}

}

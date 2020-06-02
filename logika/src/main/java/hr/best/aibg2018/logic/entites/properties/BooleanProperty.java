package hr.best.aibg2018.logic.entites.properties;

public class BooleanProperty extends AbstractProperty {

	private boolean value;

	public BooleanProperty(boolean value) {
		super(PropertyType.PROPERTY_BOOLEAN);
		this.value = value;
	}

	@Override
	public boolean getBool() {
		return value;
	}

	@Override
	public IProperty cloneProperty() {
		return new BooleanProperty(value);
	}

}

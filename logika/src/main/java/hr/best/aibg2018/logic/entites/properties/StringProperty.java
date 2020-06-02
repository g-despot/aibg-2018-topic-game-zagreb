package hr.best.aibg2018.logic.entites.properties;

public class StringProperty extends AbstractProperty {

	private String value;

	public StringProperty(String value) {
		super(PropertyType.PROPERTY_STRING);
		this.value = value;
	}

	@Override
	public String getString() {
		return value;
	}

	@Override
	public IProperty cloneProperty() {
		return new StringProperty(value);
	}

}

package hr.best.aibg2018.logic.entites.properties;

public class DoubleProperty extends AbstractProperty {

	private double value;

	public DoubleProperty(double value) {
		super(PropertyType.PROPERTY_DOUBLE);
		this.value = value;
	}

	@Override
	public double getDouble() {
		return value;
	}

	@Override
	public IProperty cloneProperty() {
		return new DoubleProperty(value);
	}

}

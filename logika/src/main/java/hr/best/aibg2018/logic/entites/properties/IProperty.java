package hr.best.aibg2018.logic.entites.properties;

public interface IProperty {

	PropertyType getType();

	String getString();

	int getInt();

	double getDouble();

	boolean getBool();

	IProperty cloneProperty();

}

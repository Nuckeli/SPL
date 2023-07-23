// Abstrakte Klasse, die die Basis für verschiedene Werttypen darstellt.
abstract class SPLValue {
    protected double value; // Der Wert, der von den abgeleiteten Klassen verwendet wird.

    // Methoden, die von den abgeleiteten Klassen implementiert werden müssen, um den Wert in verschiedenen Formaten zurückzugeben.
    public abstract double asDouble(); // Als Gleitkommazahl zurückgeben.
    public abstract String asString(); // Als Zeichenkette zurückgeben.
    public abstract boolean asBoolean(); // Als booleschen Wert zurückgeben.

    public boolean isNumeric() { // Prüfen, ob der Wert numerisch ist.
        return false;
    }
    public boolean isBoolean() { // Prüfen, ob der Wert boolesch ist.
        return false;
    }
    public boolean isString() { // Prüfen, ob der Wert eine Zeichenkette ist.
        return false;
    }

    // Die Methode, die den Wert als Zeichenkette repräsentiert.
    @Override
    public abstract String toString();

    // Methoden, die von den abgeleiteten Klassen implementiert werden müssen, um arithmetische Operationen auszuführen.
    public abstract SPLValue add(SPLValue other); // Addition.
    public abstract SPLValue subtract(SPLValue other); // Subtraktion.
    public abstract SPLValue multiply(SPLValue other); // Multiplikation.
    public abstract SPLValue divide(SPLValue other); // Division.
    public abstract SPLValue negate(); // Negation.
}

// Eine Klasse, die eine Zahl repräsentiert.
class SPLNumberValue extends SPLValue {
    final double value;

    public SPLNumberValue(double value) {
        this.value = value;
    }

    // Implementierung der abstrakten Methoden für die Zahl.
    @Override
    public double asDouble() {
        return value;
    }

    @Override
    public String asString() {
        return Double.toString(value);
    }

    @Override
    public boolean asBoolean() {
        return value != 0.0;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    // Implementierung der arithmetischen Operationen für die Zahl.
    @Override
    public SPLValue add(SPLValue other) { // Addition.
        double otherValue = ((SPLNumberValue) other).value;
        return new SPLNumberValue(value + otherValue);
    }

    @Override
    public SPLValue subtract(SPLValue other) { // Subtraktion.
        double otherValue = ((SPLNumberValue) other).value;
        return new SPLNumberValue(value - otherValue);
    }

    @Override
    public SPLValue multiply(SPLValue other) { // Multiplikation.
        double otherValue = ((SPLNumberValue) other).value;
        return new SPLNumberValue(value * otherValue);
    }

    @Override
    public SPLValue divide(SPLValue other) { // Division.
        double otherValue = ((SPLNumberValue) other).value;
        if (otherValue == 0.0) {
            // Division durch Null vermeiden
            System.out.println("Division durch Null!");
            return new SPLUndefinedValue();
        }
        return new SPLNumberValue(value / otherValue);
    }

    @Override
    public boolean isNumeric() {
        return true;
    }

    // Implementierung der Negation für die Zahl.
    @Override
    public SPLValue negate() {
        return new SPLNumberValue(-value);
    }
}

// Eine Klasse, die einen Zeichenkettenwert repräsentiert.
class SPLStringValue extends SPLValue {
    private final String value;

    public SPLStringValue(String value) {
        this.value = value;
    }

    // Implementierung der abstrakten Methoden für die Zeichenkette.
    @Override
    public double asDouble() {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public boolean asBoolean() {
        return Boolean.parseBoolean(value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public SPLValue add(SPLValue other) {
        return null;
    }

    @Override
    public SPLValue subtract(SPLValue other) {
        return null;
    }

    @Override
    public SPLValue multiply(SPLValue other) {
        return null;
    }

    @Override
    public SPLValue divide(SPLValue other) {
        return null;
    }

    @Override
    public SPLValue negate() {
        return null;
    }

    @Override
    public boolean isString() {
        return true;
    }
}

// Eine Klasse, die einen booleschen Wert repräsentiert.
class SPLBooleanValue extends SPLValue {
    private final boolean value;

    public SPLBooleanValue(boolean value) {
        this.value = value;
    }

    // Implementierung der abstrakten Methoden für den booleschen Wert.
    @Override
    public double asDouble() {
        return value ? 1.0 : 0.0;
    }

    @Override
    public String asString() {
        return Boolean.toString(value);
    }

    @Override
    public boolean asBoolean() {
        return value;
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }

    @Override
    public SPLValue add(SPLValue other) {
        return null;
    }

    @Override
    public SPLValue subtract(SPLValue other) {
        return null;
    }

    @Override
    public SPLValue multiply(SPLValue other) {
        return null;
    }

    @Override
    public SPLValue divide(SPLValue other) {
        return null;
    }

    @Override
    public boolean isBoolean() {
        return true;
    }

    // Implementierung der Negation für den booleschen Wert.
    @Override
    public SPLValue negate() {
        return new SPLBooleanValue(!value);
    }
}

// Eine Klasse, die einen undefinierten Wert repräsentiert.
class SPLUndefinedValue extends SPLValue {

    // Implementierung der abstrakten Methoden für den undefinierten Wert.
    @Override
    public double asDouble() {
        return 0.0;
    }

    @Override
    public String asString() {
        return "undefined";
    }

    @Override
    public boolean asBoolean() {
        return false;
    }

    @Override
    public String toString() {
        return "undefined";
    }

    @Override
    public SPLValue add(SPLValue other) {
        return null;
    }

    @Override
    public SPLValue subtract(SPLValue other) {
        return null;
    }

    @Override
    public SPLValue multiply(SPLValue other) {
        return null;
    }

    @Override
    public SPLValue divide(SPLValue other) {
        return null;
    }

    @Override
    public SPLValue negate() {
        return null;
    }
}

abstract class SPLValue {
    protected double value;
    // Implementations for casting to specific types

    public abstract double asDouble();

    public abstract String asString();

    public abstract boolean asBoolean();

    // Prüft, ob der Wert numerisch ist (nur in SPLNumberValue implementiert)
    public boolean isNumeric() {
        return false;
    }
    public boolean isBoolean() {
        return false;
    }

    public boolean isString() {
        return false;
    }
    // Override toString() to provide a meaningful representation of the value
    @Override
    public abstract String toString();

    public abstract SPLValue add(SPLValue other);
    public abstract SPLValue subtract(SPLValue other);
    public abstract SPLValue multiply(SPLValue other);
    public abstract SPLValue divide(SPLValue other);
    public abstract SPLValue negate();

    public abstract boolean equals(SPLValue other);

}

class SPLNumberValue extends SPLValue {
    final double value;

    public SPLNumberValue(double value) {
        this.value = value;
    }

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

    @Override
    public SPLValue add(SPLValue other) {
        double otherValue = ((SPLNumberValue) other).value;
        return new SPLNumberValue(value + otherValue);
    }

    @Override
    public SPLValue subtract(SPLValue other) {
        double otherValue = ((SPLNumberValue) other).value;
        return new SPLNumberValue(value - otherValue);
    }

    @Override
    public SPLValue multiply(SPLValue other) {
        double otherValue = ((SPLNumberValue) other).value;
        return new SPLNumberValue(value * otherValue);
    }

    @Override
    public SPLValue divide(SPLValue other) {
        double otherValue = ((SPLNumberValue) other).value;
        if (otherValue == 0.0) {
            // Division durch Null sollte vermieden werden
            System.out.println("Division by zero!");
            return new SPLUndefinedValue();
        }
        return new SPLNumberValue(value / otherValue);
    }

    @Override
    public SPLValue negate() {
        return new SPLNumberValue(-value);
    }

    @Override
    public boolean isNumeric() {
        return true;
    }


    @Override
    public boolean equals(SPLValue other) {
        // Hier implementierst du den Vergleich für SPLNumberValue
        if (other instanceof SPLNumberValue) {
            return this.value == ((SPLNumberValue) other).value;
        }
        return false;
    }
}

class SPLStringValue extends SPLValue {
    private final String value;

    public SPLStringValue(String value) {
        this.value = value;
    }

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
    public boolean isNumeric() {
        return false;
    }

    @Override
    public boolean isString() {
        return true;
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
    public boolean equals(SPLValue other) {
        // Hier implementierst du den Vergleich für SPLStringValue
        if (other instanceof SPLStringValue) {
            return this.value.equals(((SPLStringValue) other).value);
        }
        return false;
    }
}

class SPLBooleanValue extends SPLValue {
    private final boolean value;

    public SPLBooleanValue(boolean value) {
        this.value = value;
    }

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
    public boolean isNumeric() {
        return false;
    }

    @Override
    public boolean isBoolean() { return true; }

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
        return new SPLBooleanValue(!value);
    }

    @Override
    public boolean equals(SPLValue other) {
        // Hier implementierst du den Vergleich für SPLBooleanValue
        if (other instanceof SPLBooleanValue) {
            return this.value == ((SPLBooleanValue) other).value;
        }
        return false;
    }
}

class SPLUndefinedValue extends SPLValue {

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
    public boolean isNumeric() {
        return false;
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
    public boolean equals(SPLValue other) {
        return other instanceof SPLUndefinedValue;
    }
}

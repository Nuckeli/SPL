abstract class SPLValue {
    // Implementations for casting to specific types

    public abstract double asDouble();

    public abstract String asString();

    public abstract boolean asBoolean();

    // Override toString() to provide a meaningful representation of the value
    @Override
    public abstract String toString();
}

class SPLNumberValue extends SPLValue {
    private final double value;

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
}

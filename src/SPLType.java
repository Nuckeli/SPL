public class SPLType {
    public static final SPLType NONE = new SPLType("None");

    private final String typeName;

    private SPLType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public String toString() {
        return typeName;
    }
}

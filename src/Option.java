public abstract class Option {
    public abstract SPLValue unwrap();
    public abstract boolean isNone();
    public abstract boolean isSome();
}

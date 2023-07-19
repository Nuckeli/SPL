public final class Some extends Option {
    private final SPLValue value;

    public Some(SPLValue value) {
        this.value = value;
    }

    @Override
    public SPLValue unwrap() {
        return value;
    }

    @Override
    public boolean isNone() {
        return false;
    }

    @Override
    public boolean isSome() {
        return true;
    }
}
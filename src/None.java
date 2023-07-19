public final class None extends Option {
    @Override
    public SPLValue unwrap() {
        throw new RuntimeException("Cannot unwrap a None value.");
    }

    @Override
    public boolean isNone() {
        return true;
    }

    @Override
    public boolean isSome() {
        return false;
    }
}
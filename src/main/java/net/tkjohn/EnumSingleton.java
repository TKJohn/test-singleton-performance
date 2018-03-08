package net.tkjohn;

public enum EnumSingleton implements ISingleton {
    INSTANCE;

    public static EnumSingleton getInstance() {
        return INSTANCE;
    }

    @Override
    public void method() {
    }
}

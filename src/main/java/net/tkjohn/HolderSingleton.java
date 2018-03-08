package net.tkjohn;

public class HolderSingleton implements ISingleton {

    private HolderSingleton() {
    }

    private static class Holder {
        private static final HolderSingleton INSTANCE = new HolderSingleton();
    }

    public static HolderSingleton getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void method() {
    }
}

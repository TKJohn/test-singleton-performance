package net.tkjohn;

public class DoubleCheckedSingleton implements ISingleton {
    private volatile static DoubleCheckedSingleton INSTANCE;

    private DoubleCheckedSingleton() {
    }

    public static DoubleCheckedSingleton getInstance() {
        if (INSTANCE == null) {
            synchronized (DoubleCheckedSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DoubleCheckedSingleton();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void method() {
    }
}

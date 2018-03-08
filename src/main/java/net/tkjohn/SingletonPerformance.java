package net.tkjohn;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
@Measurement(iterations = 5)
@Warmup(iterations = 5)
public class SingletonPerformance {
    private static final int PARALLEL_SIZE = 10000;
    private static final int TIMES_PER_THREAD = 500000;

    private static ExecutorService executorService;

    public static void main(String[] args) {
        SingletonPerformance testMain = new SingletonPerformance();
        testMain.setup();

        testMain.doubleCheckedTest();
        testMain.enumTest();
        testMain.holderTest();

        testMain.tearDown();
    }

    @Setup
    public void setup() {
        executorService = Executors.newFixedThreadPool(PARALLEL_SIZE);
    }

    @TearDown
    public void tearDown() {
        executorService.shutdown();
    }

    @Benchmark
    public void doubleCheckedTest() {
        runTest(RunnableFactory.RUNNABLE_TYPE_DOUBLE_CHECKED);
    }

    @Benchmark
    public void enumTest() {
        runTest(RunnableFactory.RUNNABLE_TYPE_ENUM);
    }

    @Benchmark
    public void holderTest() {
        runTest(RunnableFactory.RUNNABLE_TYPE_HOLDER);
    }

    private void runTest(int type) {
        CountDownLatch countDownLatch = new CountDownLatch(PARALLEL_SIZE);

        for (int i = 0; i < PARALLEL_SIZE; i++) {
            CountdownRunnable r = RunnableFactory.getRunnable(type);
            r.addCountDownLatch(countDownLatch);
            executorService.submit(r);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException ignored) {
        }
    }

    private static class RunnableFactory {
        private static final int RUNNABLE_TYPE_ENUM = 1;
        private static final int RUNNABLE_TYPE_DOUBLE_CHECKED = 2;
        private static final int RUNNABLE_TYPE_HOLDER = 3;

        static CountdownRunnable getRunnable(int type) {
            switch (type) {
                case RUNNABLE_TYPE_DOUBLE_CHECKED:
                    return new DoubleCheckedSingletonRunnable();
                case RUNNABLE_TYPE_ENUM:
                    return new EnumSingletonRunnable();
                case RUNNABLE_TYPE_HOLDER:
                    return new HolderSingletonRunnable();
                default:
                    return new CountdownRunnable() {
                        @Override
                        protected void actualRun() {
                        }
                    };
            }
        }
    }

    private abstract static class CountdownRunnable implements Runnable {
        private CountDownLatch cd;

        final void addCountDownLatch(CountDownLatch cd) {
            this.cd = cd;
        }

        @Override
        public void run() {
            for (int i = 0; i < TIMES_PER_THREAD; i++) actualRun();
            cd.countDown();
        }

        abstract protected void actualRun();
    }

    private static class DoubleCheckedSingletonRunnable extends CountdownRunnable {
        @Override
        protected void actualRun() {
            DoubleCheckedSingleton.getInstance().method();
        }
    }

    private static class EnumSingletonRunnable extends CountdownRunnable {
        @Override
        protected void actualRun() {
            EnumSingleton.getInstance().method();
        }
    }

    private static class HolderSingletonRunnable extends CountdownRunnable {
        @Override
        protected void actualRun() {
            HolderSingleton.getInstance().method();
        }
    }

}
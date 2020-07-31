package cn.edu.sustech.dbgroup;

public class utils {
    private utils() {};
    private static long startTime;
    public static void startTimeCounter() {
        startTime = System.nanoTime();
    }
    public static void PrintTimeCounterAndRestart() {
        System.err.println((System.nanoTime() - startTime) / 1000000 + " ms");
        startTimeCounter();
    }
}

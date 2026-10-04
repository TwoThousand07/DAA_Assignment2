import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class Benchmark {
    private static final int[] SIZES = {100, 1000, 10000, 100000};
    private static final int REPEATS = 5;

    public static void main(String[] args) throws IOException {
        File dir = new File("results");
        if (!dir.exists()) dir.mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter("results/results.csv"))) {
            writer.println("workload,variant,structure,n,time_ms,steps,moves,comparisons");

            for (int n : SIZES) {
                runW1(writer, n);
                runW2(writer, n);
                runW3(writer, n, "head");
                runW3(writer, n, "middle");
                runW4(writer, n);
            }
        }
        System.out.println("Benchmark completed. Results exported to results/results.csv");
    }

    private static void runW1(PrintWriter writer, int n) {
        long[] daTimes = new long[REPEATS];
        long[] listTimes = new long[REPEATS];
        Metrics daM = null, listM = null;

        for (int r = 0; r < REPEATS + 1; r++) {
            Random rand = new Random(42);
            DynamicArray da = new DynamicArray();
            MyLinkedList list = new MyLinkedList();
            for (int i = 0; i < n; i++) { int val = rand.nextInt(); da.add(val); list.add(val); }

            da.metrics.reset();
            long t0 = System.nanoTime();
            for (int i = 0; i < 10000; i++) da.get(rand.nextInt(n));
            long t1 = System.nanoTime();

            list.metrics.reset();
            long t2 = System.nanoTime();
            for (int i = 0; i < 10000; i++) list.get(rand.nextInt(n));
            long t3 = System.nanoTime();

            if (r > 0) {
                daTimes[r - 1] = t1 - t0;
                listTimes[r - 1] = t3 - t2;
                daM = da.metrics; listM = list.metrics;
            }
        }
        writeRow(writer, "W1", "-", "DynamicArray", n, median(daTimes), daM);
        writeRow(writer, "W1", "-", "MyLinkedList", n, median(listTimes), listM);
    }

    private static void runW2(PrintWriter writer, int n) {
        long[] daTimes = new long[REPEATS];
        long[] listTimes = new long[REPEATS];
        Metrics daM = null, listM = null;

        for (int r = 0; r < REPEATS + 1; r++) {
            Random rand = new Random(42);
            DynamicArray da = new DynamicArray();
            MyLinkedList list = new MyLinkedList();
            int[] elements = new int[n];
            for (int i = 0; i < n; i++) {
                elements[i] = rand.nextInt(1000000);
                da.add(elements[i]);
                list.add(elements[i]);
            }

            int[] queries = new int[1000];
            for (int i = 0; i < 500; i++) queries[i] = elements[rand.nextInt(n)];
            for (int i = 500; i < 1000; i++) queries[i] = rand.nextInt(1000000) + 1000000;

            da.metrics.reset();
            long t0 = System.nanoTime();
            for (int q : queries) da.contains(q);
            long t1 = System.nanoTime();

            list.metrics.reset();
            long t2 = System.nanoTime();
            for (int q : queries) list.contains(q);
            long t3 = System.nanoTime();

            if (r > 0) {
                daTimes[r - 1] = t1 - t0;
                listTimes[r - 1] = t3 - t2;
                daM = da.metrics; listM = list.metrics;
            }
        }
        writeRow(writer, "W2", "-", "DynamicArray", n, median(daTimes), daM);
        writeRow(writer, "W2", "-", "MyLinkedList", n, median(listTimes), listM);
    }

    private static void runW3(PrintWriter writer, int n, String variant) {
        long[] daTimes = new long[REPEATS];
        long[] listTimes = new long[REPEATS];
        Metrics daM = null, listM = null;

        for (int r = 0; r < REPEATS + 1; r++) {
            Random rand = new Random(42);
            DynamicArray da = new DynamicArray();
            MyLinkedList list = new MyLinkedList();
            for (int i = 0; i < n; i++) { int val = rand.nextInt(); da.add(val); list.add(val); }

            int idx = variant.equals("head") ? 0 : n / 2;

            da.metrics.reset();
            long t0 = System.nanoTime();
            for (int i = 0; i < 1000; i++) da.add(idx, 99);
            for (int i = 0; i < 1000; i++) da.remove(idx);
            long t1 = System.nanoTime();

            list.metrics.reset();
            long t2 = System.nanoTime();
            for (int i = 0; i < 1000; i++) list.add(idx, 99);
            for (int i = 0; i < 1000; i++) list.remove(idx);
            long t3 = System.nanoTime();

            if (r > 0) {
                daTimes[r - 1] = t1 - t0;
                listTimes[r - 1] = t3 - t2;
                daM = da.metrics; listM = list.metrics;
            }
        }
        writeRow(writer, "W3", variant, "DynamicArray", n, median(daTimes), daM);
        writeRow(writer, "W3", variant, "MyLinkedList", n, median(listTimes), listM);
    }

    private static void runW4(PrintWriter writer, int n) {
        long[] heapTimes = new long[REPEATS];
        Metrics heapM = null;

        for (int r = 0; r < REPEATS + 1; r++) {
            Random rand = new Random(42);
            MinHeap heap = new MinHeap(n);
            int[] values = new int[n];
            for (int i = 0; i < n; i++) values[i] = rand.nextInt();

            heap.metrics.reset();
            long t0 = System.nanoTime();
            for (int val : values) heap.insert(val);
            for (int i = 0; i < n; i++) heap.extractMin();
            long t1 = System.nanoTime();

            if (r > 0) {
                heapTimes[r - 1] = t1 - t0;
                heapM = heap.metrics;
            }
        }
        writeRow(writer, "W4", "-", "MinHeap", n, median(heapTimes), heapM);
    }

    private static double median(long[] times) {
        Arrays.sort(times);
        return times[times.length / 2] / 1e6;
    }

    private static void writeRow(PrintWriter w, String wlk, String var, String struct, int n, double timeMs, Metrics m) {
        w.printf("%s,%s,%s,%d,%.4f,%d,%d,%d\n", wlk, var, struct, n, timeMs, m.steps, m.moves, m.comparisons);
    }
}
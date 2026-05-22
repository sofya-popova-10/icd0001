import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

/**
 * Sorting benchmark for double arrays.
 */
public class DoubleSorting {

    static final int[] SIZES = {64000, 128000, 256000, 512000};
    static final int REPETITIONS = 5;
    static final long RANDOM_SEED = 20260522L;

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        System.out.println("Sorting benchmark");
        System.out.println("Repetitions per method: " + REPETITIONS);
        System.out.println();

        String csvFile = "sorting-results.csv";

        try (PrintWriter out = new PrintWriter(new FileWriter(csvFile))) {
            out.println("size,insertion_ms,binary_insertion_ms,merge_ms,quick_ms,arrays_sort_ms");

            for (int size : SIZES) {
                double[] origArray = createRandomArray(size, RANDOM_SEED + size);

                double insertionAvg = measureAverage(origArray, "insertion");
                double binaryInsertionAvg = measureAverage(origArray, "binaryInsertion");
                double mergeAvg = measureAverage(origArray, "merge");
                double quickAvg = measureAverage(origArray, "quick");
                double arraysAvg = measureAverage(origArray, "arrays");

                System.out.println("Length: " + size);
                System.out.printf("Insertion sort:           %10.3f ms%n", insertionAvg);
                System.out.printf("Binary insertion sort:    %10.3f ms%n", binaryInsertionAvg);
                System.out.printf("Merge sort:               %10.3f ms%n", mergeAvg);
                System.out.printf("Quick sort:               %10.3f ms%n", quickAvg);
                System.out.printf("Java Arrays.sort:         %10.3f ms%n", arraysAvg);
                System.out.println();

                out.printf(Locale.US, "%d,%.3f,%.3f,%.3f,%.3f,%.3f%n",
                        size, insertionAvg, binaryInsertionAvg, mergeAvg, quickAvg, arraysAvg);
            }

            System.out.println("CSV written to: " + csvFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static double[] createRandomArray(int size, long seed) {
        double[] a = new double[size];
        Random generator = new Random(seed);
        for (int i = 0; i < size; i++) {
            a[i] = generator.nextDouble() * 1000.0;
        }
        return a;
    }

    static double measureAverage(double[] origArray, String method) {
        long totalNanos = 0L;

        for (int rep = 0; rep < REPETITIONS; rep++) {
            double[] copy = Arrays.copyOf(origArray, origArray.length);

            long start = System.nanoTime();

            switch (method) {
                case "insertion":
                    insertionSort(copy);
                    break;
                case "binaryInsertion":
                    binaryInsertionSort(copy);
                    break;
                case "merge":
                    mergeSort(copy, 0, copy.length);
                    break;
                case "quick":
                    quickSort(copy, 0, copy.length);
                    break;
                case "arrays":
                    Arrays.sort(copy);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown method: " + method);
            }

            long finish = System.nanoTime();
            totalNanos += (finish - start);

            checkOrder(copy);
        }

        return totalNanos / 1_000_000.0 / REPETITIONS;
    }

    public static void insertionSort(double[] a) {
        if (a.length < 2)
            return;

        for (int i = 1; i < a.length; i++) {
            double b = a[i];
            int j;

            for (j = i - 1; j >= 0; j--) {
                if (a[j] <= b)
                    break;
                a[j + 1] = a[j];
            }
            a[j + 1] = b;
        }
    }

    public static void binaryInsertionSort(double[] a) {
        if (a.length < 2)
            return;

        for (int i = 1; i < a.length; i++) {
            double b = a[i];
            int left = 0;
            int right = i - 1;

            while (left <= right) {
                int mid = (left + right) / 2;
                if (a[mid] <= b)
                    left = mid + 1;
                else
                    right = mid - 1;
            }

            System.arraycopy(a, left, a, left + 1, i - left);
            a[left] = b;
        }
    }

    public static void mergeSort(double[] array, int left, int right) {
        if (array.length < 2)
            return;
        if ((right - left) < 2)
            return;

        int k = (left + right) / 2;
        mergeSort(array, left, k);
        mergeSort(array, k, right);
        merge(array, left, k, right);
    }

    public static void merge(double[] array, int left, int k, int right) {
        if (array.length < 2 || (right - left) < 2 || k <= left || k >= right)
            return;

        double[] tmp = new double[right - left];
        int n1 = left;
        int n2 = k;
        int m = 0;

        while (true) {
            if ((n1 < k) && (n2 < right)) {
                if (array[n1] > array[n2]) {
                    tmp[m++] = array[n2++];
                } else {
                    tmp[m++] = array[n1++];
                }
            } else {
                if (n1 >= k) {
                    for (int i = n2; i < right; i++) {
                        tmp[m++] = array[i];
                    }
                    break;
                } else {
                    for (int i = n1; i < k; i++) {
                        tmp[m++] = array[i];
                    }
                    break;
                }
            }
        }

        System.arraycopy(tmp, 0, array, left, right - left);
    }

    public static void quickSort(double[] array, int l, int r) {
        if (array == null || array.length < 1 || l < 0 || r <= l)
            throw new IllegalArgumentException("quickSort: wrong parameters");

        if ((r - l) < 2)
            return;

        int i = l;
        int j = r - 1;
        double x = array[(i + j) / 2];

        do {
            while (array[i] < x)
                i++;
            while (x < array[j])
                j--;
            if (i <= j) {
                double tmp = array[i];
                array[i] = array[j];
                array[j] = tmp;
                i++;
                j--;
            }
        } while (i < j);

        if (l < j)
            quickSort(array, l, j + 1);

        if (i < r - 1)
            quickSort(array, i, r);
    }

    static void checkOrder(double[] a) {
        if (a.length < 2)
            return;

        for (int i = 0; i < a.length - 1; i++) {
            if (a[i] > a[i + 1])
                throw new IllegalArgumentException(
                        "array not ordered: a[" + i + "]=" + a[i] +
                                " a[" + (i + 1) + "]=" + a[i + 1]);
        }
    }
}
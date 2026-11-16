package core.java.concurrency;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Demonstrates RecursiveTask with ForkJoinPool (Divide-and-Conquer parallelism).
 * Splits a large array sum calculation into smaller subtasks when above a threshold,
 * executes them in parallel, and joins their results.
 */
public class Recursive_Task_ForkJoin_Demo {

    public static void main(String[] args) {
        System.out.println("=== RecursiveTask & ForkJoinPool Demo ===\n");

        // Create a sample dataset of 20 numbers (1 to 20)
        int[] numbers = new int[20];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i + 1;
        }

        // Create ForkJoinPool (uses available CPU cores by default)
        try (ForkJoinPool forkJoinPool = new ForkJoinPool()) {
            ArraySumRecursiveTask mainTask = new ArraySumRecursiveTask(numbers, 0, numbers.length);

            // Invoke the root task and wait for the combined result
            long totalSum = forkJoinPool.invoke(mainTask);

            System.out.println("\nFinal Combined Sum (1 to 20): " + totalSum);
        }
    }
}

class ArraySumRecursiveTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 5; // Max elements processed directly by one task
    private final int[] array;
    private final int start;
    private final int end;

    public ArraySumRecursiveTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        int length = end - start;

        // Base Case: Task is small enough to compute directly
        if (length <= THRESHOLD) {
            long sum = 0;
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            System.out.println(Thread.currentThread().getName()
                    + " computed range [" + start + ".." + (end - 1) + "] = " + sum);
            return sum;
        }

        // Recursive Case: Split task into two halves
        int mid = start + (length / 2);
        ArraySumRecursiveTask leftTask = new ArraySumRecursiveTask(array, start, mid);
        ArraySumRecursiveTask rightTask = new ArraySumRecursiveTask(array, mid, end);

        // Fork the left subtask asynchronously onto another pool thread
        leftTask.fork();

        // Compute the right subtask directly in the current thread
        long rightResult = rightTask.compute();

        // Wait for the forked left subtask result and combine
        long leftResult = leftTask.join();

        return leftResult + rightResult;
    }
}
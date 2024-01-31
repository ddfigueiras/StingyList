package aed.collections;

import aed.collections.StingyList;

import java.util.Arrays;

public class StingyListTest {
    public static void main(String[] args) {
        testGetMethods();
        testReverseMethod();
    }

    public static void testGetMethods() {
        System.out.println("Testing get(int index) and getSlow(int index) methods:");

        int[] sizes = {100, 200, 400, 1000, 10000, 100000};
        int[] indices = {0, 50, 99}; // Test first, middle, and last elements

        for (int size : sizes) {
            StingyList<Integer> list = new StingyList<>();
            for (int i = 0; i < size; i++) {
                list.add(i);
            }

            System.out.println("List size: " + size);

            for (int index : indices) {
                long startTime = System.nanoTime();
                list.get(index);
                long endTime = System.nanoTime();
                long duration = endTime - startTime;
                System.out.println("get(" + index + ") took " + duration + " nanoseconds");

                startTime = System.nanoTime();
                list.getSlow(index);
                endTime = System.nanoTime();
                duration = endTime - startTime;
                System.out.println("getSlow(" + index + ") took " + duration + " nanoseconds");
            }

            System.out.println("=================================");
        }
    }

    public static void testReverseMethod() {
        System.out.println("Testing reverse() method:");

        int[] sizes = {100, 1000, 10000, 100000};

        for (int size : sizes) {
            StingyList<Integer> list = new StingyList<>();
            for (int i = 0; i < size; i++) {
                list.add(i);
            }

            long startTime = System.nanoTime();
            list.reverse();
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            System.out.println("reverse() took " + duration + " nanoseconds for list size " + size);

            // Verify if the list is reversed
            boolean isReversed = true;
            int expectedValue = size - 1;
            for (int value : list) {
                if (value != expectedValue) {
                    isReversed = false;
                    break;
                }
                expectedValue--;
            }
            System.out.println("Is list reversed: " + isReversed);
            System.out.println("=================================");
        }
    }
}

package aed.sorting;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//podem alterar esta classe, se quiserem
class Limits
{
    char minChar;
    char maxChar;
    int maxLength;
}

public class RecursiveStringSort extends aed.sorting.Sort
{
    private static final Random R = new Random();


    //esta implementação base do quicksort é fornecida para que possam comparar o tempo de execução do quicksort
    //com a vossa implementação do RecursiveStringSort
    public static <T extends Comparable<T>> void quicksort(T[] a)
    {
        qsort(a, 0, a.length-1);   
    }

    private static <T extends Comparable<T>> void qsort(T[] a, int low, int high)
    {
        if (high <= low) return;
        int j = partition(a, low, high);
        qsort(a, low, j-1);
        qsort(a, j+1, high);
    }

    private static <T extends Comparable<T>> int partition(T[] a, int low, int high)
    {
        //partition into a[low...j-1],a[j],[aj+1...high] and return j
        //choose a random pivot
        int pivotIndex = low + R.nextInt(high+1-low);
        exchange(a,low,pivotIndex);
        T v = a[low];
        int i = low, j = high +1;

        while(true)
        {
            while(less(a[++i],v)) if(i == high) break;
            while(less(v,a[--j])) if(j == low) break;

            if(i >= j) break;
            exchange(a , i, j);
        }
        exchange(a, low, j);

        return j;
    }

    

    //método de ordenação insertionSort
    //no entanto este método recebe uma Lista de Strings em vez de um Array de Strings
    public static void insertionSort(List<String> a)
    {
        int n = a.size();
        for(int i = 1; i < n; i++){
            String key = a.get(i);
            int before = i - 1;
            while (before >= 0 && a.get(before).compareTo(key) > 0){
                a.set(before + 1, a.get(before));
                before--;
            }
            a.set(before + 1, key);
        }

    }
    // 0 - banana, 1- alberto

    public static Limits determineLimits(List<String> a, int characterIndex)
    {

        Limits limits = new Limits();
        if (a.isEmpty()) {
            limits.minChar = Character.MIN_VALUE;
        } else {
            limits.minChar = Character.MAX_VALUE;
        }
        for (String current : a) {
            if (current.length() > characterIndex) {
                char currentChar = current.charAt(characterIndex);
                limits.minChar = (currentChar < limits.minChar) ? currentChar : limits.minChar;
                limits.maxChar = (currentChar > limits.maxChar) ? currentChar : limits.maxChar;

            }
            else limits.minChar = 0;
            limits.maxLength = Math.max(current.length(), limits.maxLength);
        }

        return limits;
    }




    //ponto de entrada principal para o vosso algoritmo de ordenação
    public static void sort(String[] a) {
        recursive_sort(Arrays.asList(a),0);
    }

    public void recursiveSort(List<String> a, int characterIndex)
    {
        if (a.size() <= 70) {
            insertionSort(a);
            return;
        }

        Limits limits = determineLimits(a, characterIndex);

        List<List<String>> buckets = new ArrayList<>(limits.maxChar - limits.minChar + 1);
        for (int i = 0; i < limits.maxChar - limits.minChar + 1; i++) {
            buckets.add(new ArrayList<>());
        }

        for (String str : a) {
            char currentChar = (str.length() > characterIndex) ? str.charAt(characterIndex) : limits.minChar;
            int bucketIndex = currentChar - limits.minChar;
            buckets.get(bucketIndex).add(str);
        }

        for (List<String> bucket : buckets) {
            recursiveSort(bucket, characterIndex + 1);
        }

        int index = 0;
        for (List<String> bucket : buckets) {
            for (String str : bucket) {
                a.set(index++, str);
            }
        }
    }


    public static void fasterSort(String[] a)
    {
        //TODO: implement
    }
    public static void main(String[] args) {
        System.out.println("TEST INFO: Testing sort");
        String[] arr1 = {"Zeus", "alameda", "alfredo", "chouriço", "joão", "maria", "ola", "ola", "teste", "único"};
        System.out.println(Arrays.toString(arr1));
        //sort(arr1);
        System.out.println(Arrays.toString(arr1));

        // Add more test cases
        String[] arr2 = {"banana", "apple", "cherry"};
        System.out.println(Arrays.toString(arr2));
        //sort(arr2);
        System.out.println(Arrays.toString(arr2));

        String[] arr3 = {"zeus", "dog","dog", "elephant", "bird"};
        System.out.println(Arrays.toString(arr3));
        //sort(arr3);
        System.out.println(Arrays.toString(arr3));
    }
}


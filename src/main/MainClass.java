package main;

import algorithm.*;
import utility.Stopwatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class MainClass {

    private final static String DIR = System.getProperty("user.dir");
    private final static int MAX_DATA_NUM = 4000;

    public static void main(String[] args){
        try {
            int[] data = new int[MAX_DATA_NUM];
            int[] raw_data = new int[MAX_DATA_NUM];
            File inputFile = new File(DIR + "/random.txt");
            int index = 0;
            FileInputStream fileInputStream = new FileInputStream(inputFile);
            Scanner scanner = new Scanner(fileInputStream);
            while (scanner.hasNext()){
                raw_data[index++] = scanner.nextInt();
            }
            fileInputStream.close();
            System.arraycopy(raw_data, 0, data, 0, raw_data.length);

            Stopwatch stopwatch = new Stopwatch();

            /**
             * QuickSort (Serial)
             */
            Quicksort qs = new Quicksort(data);
            double beforeQs = stopwatch.elapsedTime();
            qs.sort(0, data.length - 1);
            double afterQs = stopwatch.elapsedTime();
            assertSort(data);
            outputToTxt(1, data);
            System.arraycopy(raw_data, 0, data, 0, raw_data.length);

            /**
             * EnumSort (Serial)
             */
            EnumSort es = new EnumSort(data);
            double beforeEs = stopwatch.elapsedTime();
            es.sort();
            double afterEs = stopwatch.elapsedTime();
            assertSort(es.result);
            outputToTxt(2, es.result);
            System.arraycopy(raw_data, 0, data, 0, raw_data.length);

            /**
             * MergeSort (Serial)
             */
            MergeSort ms = new MergeSort(data);
            double beforeMs = stopwatch.elapsedTime();
            ms.sort(0 ,data.length - 1);
            double afterMs = stopwatch.elapsedTime();
            assertSort(data);
            outputToTxt(3, data);
            System.arraycopy(raw_data, 0, data, 0, raw_data.length);

            /**
             * QuickSort (Parallel)
             */
            double beforeQsp = stopwatch.elapsedTime();
            List<Future> futures = new Vector<>();
            ExecutorService executorService = Executors.newFixedThreadPool(8);
            QuickSortParallelTask mainQspTask = new QuickSortParallelTask(data, 0, data.length - 1, executorService, futures, 1000);
            futures.add(executorService.submit(mainQspTask));
            while (!futures.isEmpty()){
                Future topFuture = futures.remove(0);
                try{
                    topFuture.get();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            executorService.shutdown();
            double afterQsp = stopwatch.elapsedTime();
            assertSort(data);
            outputToTxt(4, data);
            System.arraycopy(raw_data, 0, data, 0, raw_data.length);

            /**
             * MergeSort (Parallel) with naive Thread start/join
             */
            double beforeMsp = stopwatch.elapsedTime();
            Thread mspThread = new Thread(new MergeSortParallelTask(data, 0, data.length - 1, 500));
            mspThread.start();
            try{
                mspThread.join();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            double afterMsp = stopwatch.elapsedTime();
            assertSort(data);
            outputToTxt(5, data);
            System.arraycopy(raw_data, 0, data, 0, raw_data.length);

            /**
             * MergeSort (Parallel) extended from RecursiveAction
             */
            double beforeMsp2 = stopwatch.elapsedTime();
            final ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors() - 1);
            forkJoinPool.invoke(new MergeSortParallelTask2(data, 0, data.length - 1));
            double afterMsp2 = stopwatch.elapsedTime();
            assertSort(data);
            System.arraycopy(raw_data, 0, data, 0, raw_data.length);

            /**
             * EnumSort (Parallel) extended from RecursiveAction
             */
            double beforeEsp = stopwatch.elapsedTime();
            int[] resultEsp = new int[4000];
            final ForkJoinPool forkJoinPool1 = new ForkJoinPool(Runtime.getRuntime().availableProcessors() - 1);
            forkJoinPool1.invoke(new EnumSortParallelTask(data, resultEsp, 0, data.length - 1, -1));
            double afterEsp = stopwatch.elapsedTime();
            assertSort(resultEsp);
            System.arraycopy(raw_data, 0, data, 0, raw_data.length);


            /**
             * Time consumed
             */
            System.out.println("QuickSort takes " + String.format("%.4f", (afterQs - beforeQs)) + "s");
            System.out.println("EnumSort takes " + String.format("%.4f", (afterEs - beforeEs)) + "s");
            System.out.println("MergeSort takes " + String.format("%.4f", (afterMs - beforeMs)) + "s");
            System.out.println("QuickSortParallel takes " + String.format("%.4f", (afterQsp - beforeQsp)) + "s");
            System.out.println("MergeSortParallel takes " + String.format("%.4f", (afterMsp - beforeMsp)) + "s");
            System.out.println("MergeSortParallel2 takes " + String.format("%.4f", (afterMsp2 - beforeMsp2)) + "s");
            System.out.println("EnumSortParallel takes " + String.format("%.4f", (afterEsp - beforeEsp)) + "s");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void assertSort(int[] arr){
        for (int i = 1; i < arr.length; i++){
            assert arr[i - 1] <= arr[i];
        }
    }

    private static void outputToTxt(int id, int[] arr){
        try {
            File outputFile = new File(System.getProperty("user.dir") + "/order" + String.valueOf(id) + ".txt");
            if (outputFile.exists())
                outputFile.delete();
            outputFile.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            PrintStream printStream = new PrintStream(fileOutputStream);
            for (int i = 0; i < arr.length; i++) {
                printStream.print(arr[i] + " ");
            }
            printStream.close();
            fileOutputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
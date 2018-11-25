package main;

import algorithm.EnumSort;
import algorithm.MergeSort;
import algorithm.QuickSortParallelTask;
import algorithm.Quicksort;
import utility.Stopwatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
            final ExecutorService executorService = Executors.newFixedThreadPool(10);
            List<Future> futures = new Vector<>();
            double beforeQsp = stopwatch.elapsedTime();
            futures.add(executorService.submit(new QuickSortParallelTask(data, 0, data.length - 1, executorService, futures, 2000)));
            while (!futures.isEmpty()){
                Future topFuture = futures.remove(0);
                try{
                    if (topFuture != null)
                        topFuture.get();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //executorService.shutdown();
            double afterQsp = stopwatch.elapsedTime();
            assertSort(data);
            outputToTxt(4, data);
            System.arraycopy(raw_data, 0, data, 0, raw_data.length);

            /**
             * The test of threshold of QuickSort in parallel mode
             *//*
            int bestThreshold = 0;
            double bestRunningTime = 100.0;
            for (int threshold = 0; threshold <= 3000; threshold += 500){
                double beforeTest = stopwatch.elapsedTime();
                futures.add(executorService.submit(new QuickSortParallelTask(data, 0, data.length - 1, executorService, futures, threshold)));
                while (!futures.isEmpty()){
                    Future topFuture = futures.remove(0);
                    try{
                        if (topFuture != null)
                            topFuture.get();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                //executorService.shutdown();
                double afterTest = stopwatch.elapsedTime();
                assertSort(data);
                System.arraycopy(raw_data, 0, data, 0, raw_data.length);
                System.out.print("threshold: " + threshold + ";  ");
                System.out.println("running time: " + String.format("%.4f", afterTest - beforeTest) + "s");
                if (afterTest - beforeTest < bestRunningTime) {
                    bestThreshold = threshold;
                    bestRunningTime = afterTest - beforeTest;
                }
            }
            executorService.shutdown();
            System.out.println("Best threshold: " + bestThreshold);
            System.out.println("Best running time: " + String.format("%.4f", bestRunningTime) + "s");
            */

            System.out.println("QuickSort takes " + String.format("%.4f", (afterQs - beforeQs)) + "s");
            System.out.println("EnumSort takes " + String.format("%.4f", (afterEs - beforeEs)) + "s");
            System.out.println("MergeSort takes " + String.format("%.4f", (afterMs - beforeMs)) + "s");
            System.out.println("QuickSortParallel takes " + String.format("%.4f", (afterQsp - beforeQsp)) + "s");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void assertSort(int[] arr){
        for (int i = 1; i < arr.length; i++)
            assert arr[i - 1] <= arr[i];
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
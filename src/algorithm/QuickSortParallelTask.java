package algorithm;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class QuickSortParallelTask implements Runnable{
    private int[] data;
    private int threshold = 1500;
    private int begin = 0;
    private int end = 0;
    private ExecutorService executorService;
    private List<Future>future;
    private Random random = new Random();

    public QuickSortParallelTask(int[] arr, int begin, int end, ExecutorService executorService, List<Future>future){
        this.data = arr;
        this.begin = begin;
        this.end = end;
        this.executorService = executorService;
        this.future = future;
    }

    public QuickSortParallelTask(int[] arr, int begin, int end, ExecutorService executorService, List<Future>future, int threshold){
        this(arr, begin, end, executorService, future);
        this.threshold = threshold;
    }

    @Override
    public void run(){
        sort(data, begin, end);
    }

    private void sort(int[] arr, int left, int right){
        int pivot = left;
        int key = arr[pivot];
        int l = left;
        int r = right;
        while (l < r){
            while (l < r && arr[r] >= key)
                r--;
            if(l < r) {
                arr[l] = arr[r];
                l++;
                while(arr[l] < key && l < r)
                    l++;
                if(l < r)
                {
                    arr[r] = arr[l];
                    r--;
                }
            }
        }
        arr[l] = key;
        if (l - left > 1){
            if (l - left > threshold) {
                //System.out.println("New Runnable added.");
                future.add(executorService.submit(new QuickSortParallelTask(arr, left, l - 1, executorService, future, threshold)));
            }
            else
                sort(arr, left, l - 1);
        }
        if (right - l > 1){
            if (right - l > threshold) {
                //System.out.println("New Runnable added.");
                future.add(executorService.submit(new QuickSortParallelTask(arr, l + 1, right, executorService, future, threshold)));
            }
            else
                sort(arr, l + 1, right);
        }
    }
}

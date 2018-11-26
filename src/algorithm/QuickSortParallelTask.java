package algorithm;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class QuickSortParallelTask implements Runnable{
    private int[] data;
    private int threshold = 1500;
    private int begin = 0;
    private int end = 0;
    private ExecutorService executorService;
    private List<Future> futures;

    public QuickSortParallelTask(int[] arr, int begin, int end, ExecutorService executorService, List<Future> futures){
        this.data = arr;
        this.begin = begin;
        this.end = end;
        this.executorService = executorService;
        this.futures = futures;
    }

    public QuickSortParallelTask(int[] arr, int begin, int end, ExecutorService executorService, List<Future> futures, int threshold){
        this(arr, begin, end, executorService, futures);
        this.threshold = threshold;
    }

    @Override
    public void run(){
        sort(data, begin, end);
    }

    private void sort(int[] arr, int left, int right){
        if (left >= right)
            return;
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
                if(l < r) {
                    arr[r] = arr[l];
                    r--;
                }
            }
        }
        arr[l] = key;

        if (l - left > 1){
            if (l - left > threshold){
                futures.add(executorService.submit(new QuickSortParallelTask(arr, left, l - 1, executorService, futures, threshold)));
            }
            else{
                sort(arr, left, l - 1);
            }
        }
        if (right - l > 1){
            if (right - l > threshold){
                futures.add(executorService.submit(new QuickSortParallelTask(arr, l + 1, right, executorService, futures)));
            }
            else{
                sort(arr, l + 1, right);
            }
        }
    }
}

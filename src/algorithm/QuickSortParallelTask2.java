package algorithm;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.RecursiveAction;

public class QuickSortParallelTask2 extends RecursiveAction {
    private int[] data;
    private int threshold = 1500;
    private int begin = 0;
    private int end = 0;

    public QuickSortParallelTask2(int[] arr, int begin, int end){
        this.data = arr;
        this.begin = begin;
        this.end = end;
    }

    public QuickSortParallelTask2(int[] arr, int begin ,int end, int threshold){
        this(arr, begin, end);
        this.threshold = threshold;
    }

    @Override
    public void compute(){
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

        List<QuickSortParallelTask2> futures = new Vector<>();

        if (l - left > 1){
            if (l - left > threshold){
                QuickSortParallelTask2 leftTask = new QuickSortParallelTask2(arr, left, l - 1, threshold);
                futures.add(leftTask);
            }
            else{
                sort(arr, left, l - 1);
            }
        }
        if (right - l > 1){
            if (right - l > threshold){
                QuickSortParallelTask2 rightTask = new QuickSortParallelTask2(arr, l + 1, right, threshold);
                futures.add(rightTask);
            }
            else{
                sort(arr, l + 1, right);
            }
        }
        invokeAll(futures);
    }
}

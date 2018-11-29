package algorithm;

import java.util.concurrent.RecursiveAction;

public class MergeSortParallelTask2 extends RecursiveAction {
    private int[] data;
    private int begin = 0;
    private int end = 0;

    private static int numOfProcessors = Runtime.getRuntime().availableProcessors();
    private static int count = 0;

    public MergeSortParallelTask2(int[] arr, int begin, int end){
        this.data = arr;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void compute(){
        if (begin < end) {
            if (count > numOfProcessors) {
                mergesort(data, begin, end);
            } else {
                count ++;
                final int middle = (begin + end) / 2;
                final MergeSortParallelTask2 leftTask =
                        new MergeSortParallelTask2(data, begin, middle);
                final MergeSortParallelTask2 rightTask =
                        new MergeSortParallelTask2(data, middle + 1, end);
                invokeAll(leftTask, rightTask);
                merge(data, begin, end, middle);
            }
        }
    }

    private void mergesort(int[] arr, int left, int right){
        if (left >= right)
            return;
        int mid = (left + right) / 2;
        mergesort(arr, left, mid);
        mergesort(arr, mid + 1, right);
        merge(arr, left, right, mid);
    }

    private void merge(int[] arr, int left, int right, int mid){
        int [] aux = new int[right - left + 1];
        int k;
        for (k = left; k <= right; k++)
            aux[k - left] = arr[k];
        int i = left;
        int j = mid + 1;
        for (k = left; k <= right; k++){
            if (i > mid){
                arr[k] = aux[j - left];
                j++;
            }
            else if (j > right){
                arr[k] = aux[i - left];
                i++;
            }
            else if (aux[i - left] > aux[j - left]){
                arr[k] = aux[j - left];
                j++;
            }
            else{
                arr[k] = aux[i - left];
                i++;
            }
        }
    }

}

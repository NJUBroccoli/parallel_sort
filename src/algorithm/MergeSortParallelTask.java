package algorithm;

public class MergeSortParallelTask implements Runnable{
    private int[] data;
    private int threshold = 1500;
    private int begin = 0;
    private int end = 0;

    public MergeSortParallelTask(int[] arr, int begin, int end){
        this.data = arr;
        this.begin = begin;
        this.end = end;
    }

    public MergeSortParallelTask(int[] arr, int begin, int end, int threshold){
        this(arr, begin, end);
        this.threshold = threshold;
    }

    @Override
    public void run(){
        sort(data, begin, end);
    }

    private void sort(int[] arr, int left, int right){
        if (left >= right)
            return;
        int mid = (left + right) / 2;
        if (mid - left > threshold) {
            Thread leftThread = new Thread(new MergeSortParallelTask(arr, left, mid, threshold));
            Thread rightThread = new Thread(new MergeSortParallelTask(arr, mid + 1, right, threshold));
            leftThread.start();
            rightThread.start();
            try {
                leftThread.join();
                rightThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            merge(left, right, mid);
        }
        else{
            sort(arr, left, mid);
            sort(arr, mid + 1, right);
            merge(left, right, mid);
        }
    }
    private void merge(int left, int right, int mid){
        int [] aux = new int[right - left + 1];
        int k;
        for (k = left; k <= right; k++)
            aux[k - left] = data[k];
        int i = left;
        int j = mid + 1;
        for (k = left; k <= right; k++){
            if (i > mid){
                data[k] = aux[j - left];
                j++;
            }
            else if (j > right){
                data[k] = aux[i - left];
                i++;
            }
            else if (aux[i - left] > aux[j - left]){
                data[k] = aux[j - left];
                j++;
            }
            else{
                data[k] = aux[i - left];
                i++;
            }
        }
    }
}

package algorithm;

public class MergeSort {

    private int[] data;
    public MergeSort(int[] arr){
        data = arr;
    }

    public void sort(int left, int right){
        if (left >= right)
            return;
        int mid = (left + right) / 2;
        sort(left, mid);
        sort(mid + 1, right);
        merge(left, right, mid);
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

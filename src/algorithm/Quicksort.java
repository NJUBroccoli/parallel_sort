package algorithm;

import java.util.Random;

public class Quicksort {

    private int [] data;
    private Random random;
    public Quicksort(int [] arr){
        data = arr;
        random = new Random();
    }

    private int partition(int left, int right){
        //int pivot = random.nextInt(right - left + 1) + left;
        int pivot = left;
        int key = data[pivot];
        int l = left;
        int r = right;
        while (l < r){
            while (l < r && data[r] >= key)
                r--;
            if(l < r) {
                data[l] = data[r];
                l++;
                while(data[l] < key && l < r)
                    l++;
                if(l < r)
                {
                    data[r] = data[l];
                    r--;
                }
            }
        }
        data[l] = key;
        return l;
    }

    public void sort(int left, int right){
        if (left < right){
            int q = partition(left, right);
            sort(left, q - 1);
            sort(q + 1, right);
        }
    }

}

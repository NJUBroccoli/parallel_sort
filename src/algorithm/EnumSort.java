package algorithm;

public class EnumSort {
    private int[] data;
    public int[] result;

    public EnumSort(int[] arr){
        data = arr;
        this.result = new int[40000];
    }

    public void sort(){
        for (int i = 0; i < data.length; i++){
            int k = 0;
            for (int j = 0; j < data.length; j++){
                if (data[i] > data[j] || (data[i] == data[j] && i > j))
                    k++;
            }
            result[k] = data[i];
        }
    }

}

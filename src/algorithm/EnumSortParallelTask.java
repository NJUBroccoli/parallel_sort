package algorithm;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.RecursiveAction;

public class EnumSortParallelTask extends RecursiveAction {
    private int[] data;
    private int[] result;
    private int begin = 0;
    private int end = 0;
    private int index = 0;

    public EnumSortParallelTask(int[] arr, int[] result, int begin, int end, int index){
        this.data = arr;
        this.begin = begin;
        this.end = end;
        this.index = index;
        this.result = result;
    }

    @Override
    public void compute(){
        if (index == -1) {
            List<EnumSortParallelTask> futures = new Vector<>();
            for (int i = begin; i <= end; i++) {
                final EnumSortParallelTask newTask = new EnumSortParallelTask(data, result, begin, end, i);
                futures.add(newTask);
            }
            invokeAll(futures);
        }
        else{
            assert(index >= begin && index <= end);
            int k = 0;
            for (int j = 0; j < data.length; j++){
                if (data[index] > data[j])
                    k++;
            }
            result[k] = data[index];
        }
    }
}

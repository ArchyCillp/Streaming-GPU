package cn.edu.sustech.dbgroup.GPU;

import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import java.util.ArrayList;

public class GPUComplexArraySumProcessWindowFunction extends ProcessAllWindowFunction<Long, String, GlobalWindow> {

    @Override
    public void process(Context context, Iterable<Long> iterable, Collector<String> collector) throws Exception {
        if (iterable instanceof ArrayList) {
            long[] arr = ((ArrayList<Long>) iterable).stream().mapToLong(Long::longValue).toArray();
            GPUNativeLib gpu = GPUAggregatorTask.gpuLib;
            double res = gpu.calculateArrayComplexSum(arr);
            collector.collect("Recent result "  + res + "\n");
        }
        else {
            throw new RuntimeException("Iterator must be ArrayList.");
        }
    }
}

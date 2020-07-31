package cn.edu.sustech.dbgroup.pureCPU;

import org.apache.flink.streaming.api.datastream.AllWindowedStream;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

public class ComplexArraySumProcessWindowFunction extends ProcessAllWindowFunction<Long, String, GlobalWindow> {

    @Override
    public void process(Context context, Iterable<Long> iterable, Collector<String> collector) throws Exception {
        double res = 0;
        for (Long in : iterable) {
            res += Math.sqrt(Math.sin(Math.abs(Math.cos(in))));
        }
        collector.collect("Recent result "  + res + "\n");
    }
}

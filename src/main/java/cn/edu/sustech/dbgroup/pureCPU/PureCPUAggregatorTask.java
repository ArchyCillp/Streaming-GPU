package cn.edu.sustech.dbgroup.pureCPU;

import cn.edu.sustech.dbgroup.inputData.DataGenerator;
import cn.edu.sustech.dbgroup.utils;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger;
import org.apache.flink.streaming.api.windowing.triggers.PurgingTrigger;

public class PureCPUAggregatorTask {
    public static void Main(SingleOutputStreamOperator<Long> stream) throws Exception {
        stream
                .windowAll(GlobalWindows.create())
                .trigger(PurgingTrigger.of(CountTrigger.of(DataGenerator.windowSize)))
                .process(new ComplexArraySumProcessWindowFunction())
                .print();

    }
}

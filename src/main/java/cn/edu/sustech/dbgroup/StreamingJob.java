/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.edu.sustech.dbgroup;

import cn.edu.sustech.dbgroup.GPU.GPUAggregatorTask;
import cn.edu.sustech.dbgroup.GPU.GPUNativeLib;
import cn.edu.sustech.dbgroup.inputData.DataGenerator;
import cn.edu.sustech.dbgroup.pureCPU.ComplexArraySumProcessWindowFunction;
import cn.edu.sustech.dbgroup.pureCPU.PureCPUAggregatorTask;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.GlobalWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger;
import org.apache.flink.streaming.api.windowing.triggers.PurgingTrigger;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;

import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="https://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class StreamingJob {
	public static void main(String[] args) throws Exception {
		PrintStream ps = new PrintStream(new FileOutputStream("output.txt"));
		System.setOut(ps);

		// set up the streaming execution environment
		String opt = "GPU";
		System.err.println(opt + " Mode Start");
		final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		DataStreamSource<String> source = env.readTextFile(DataGenerator.outputFile.getAbsolutePath());

		SingleOutputStreamOperator<Long> stream = source.map(new MapFunction<String, Long>() {
			@Override
			public Long map(String s) throws Exception {
				return Long.valueOf(s);
			}
		});

		if (opt.equals("GPU")) {
			GPUAggregatorTask.Main(stream);
		}
		else if (opt.equals("CPU")) {
			PureCPUAggregatorTask.Main(stream);
		}

		// execute program
		utils.startTimeCounter();
		env.execute("Flink Streaming Java API Skeleton");
		utils.PrintTimeCounterAndRestart();
	}
}

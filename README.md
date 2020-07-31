# Streaming-GPU

The work aims to practice the applicability to use GPU resources in Streaming systems (Apache Flink in this repo).  We wish we could use GPU to accelerate the SQL processing speed by utilizing the advanced hardware resources. 

The current plan of this work is as follows:

1. Use JNI to call GPU resources in simple Flink UDF.

2. Design some experiments to show what difference can GPU bring to work.

3. TODO...(Update 2020/7/24)

## Prerequisite

1. CUDA (version >= 10.0, maybe) 
2. JDK
3. Check your GPU Compute Capability, <https://developer.nvidia.com/cuda-gpus>
4. Check the environment variables: 
   1. NVCUDASAMPLES_ROOT
   2. JAVA_HOME

## Run

In order to run the Flink program in `StreamingJob` using GPU, you need to do the following things.

cd into `src\main\java`, `make jni` to generate c header file (`cn_edu_sustech_dbgroup_GPU_GPUNativeLib.h`) according to the native method in `cn.edu.sustech.dbgroup.GPU.GPUNativeLib`.

According to the function declaration in the header file, implement the function content in `GPUDLL.cu`, run `make` to compile `cu` file into dynamic link library.  Then you can be able to load the library in Java , and successfully run Flink program `StreamingJob`.


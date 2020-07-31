#include "cn_edu_sustech_dbgroup_GPU_GPUNativeLib.h"
#include <math.h>
#include <cub/cub.cuh>
#include <assert.h>

#define gpuErrchk(ans) { gpuAssert((ans), __FILE__, __LINE__); }
#define THREAD_PER_BLOCK 1024

inline void gpuAssert(cudaError_t code, const char *file, int line, bool abort=true)
{
   if (code != cudaSuccess) 
   {
      fprintf(stderr,"GPUassert: %s %s %d\n", cudaGetErrorString(code), file, line);
      if (abort) exit(code);
   }
}

__global__ void
arrayComplexify(jlong *A, jdouble *B, jdouble *result, int numElements) {
    int i = blockDim.x * blockIdx.x + threadIdx.x;
    if (i < numElements) {
        B[i] = sqrt(sin(fabs(cos((jdouble)A[i]))));
        // B[i] = 123.0;
    }
    if (i == 0) {
        result[0] = 0;
    }

    __syncthreads();

    typedef cub::BlockReduce<double, THREAD_PER_BLOCK> BlockReduceT; 
    __shared__ typename BlockReduceT::TempStorage temp_storage; 
    jdouble res;
    if (i < numElements) {
        res = BlockReduceT(temp_storage).Sum(B[i]);
        if (threadIdx.x == 0) {
            atomicAdd(result, res);
        }    
    }
}

JNIEXPORT jdouble JNICALL Java_cn_edu_sustech_dbgroup_GPU_GPUNativeLib_calculateArrayComplexSum
  (JNIEnv * env_, jobject this_, jlongArray arr_){
    jsize len = (*env_).GetArrayLength(arr_);
    jdouble sum = 0;
    jlong *body = (*env_).GetLongArrayElements(arr_, 0);

    jlong *d_body = NULL;
    int size = len * sizeof(jlong);
    gpuErrchk(cudaMalloc((void**)&d_body, size));
    gpuErrchk(cudaMemcpy(d_body, body, size, cudaMemcpyHostToDevice));

    jdouble *d_tmp = NULL;
    int tmp_size = len * sizeof(jdouble);
    gpuErrchk(cudaMalloc((void**)&d_tmp, tmp_size));

    jdouble *res = (jdouble *)malloc(sizeof(jdouble));
    jdouble *d_res = NULL;
    gpuErrchk(cudaMalloc((void**)&d_res, sizeof(jdouble)));

    int threadsPerBlock = THREAD_PER_BLOCK;
    int blocksPerGrid = (len + threadsPerBlock - 1) / threadsPerBlock;

    arrayComplexify<<<blocksPerGrid, threadsPerBlock>>>(d_body, d_tmp, d_res, len);
    
    cudaError_t err = cudaGetLastError();

    if (err != cudaSuccess)
    {
        fprintf(stderr, "Failed to launch vectorAdd kernel (error code %s)!\n", cudaGetErrorString(err));
        exit(EXIT_FAILURE);
    }

    gpuErrchk(cudaMemcpy(res, d_res, sizeof(jdouble), cudaMemcpyDeviceToHost));


//DEBUG
    // jdouble *tmp = (jdouble *)malloc(sizeof(jdouble));
    // cudaMemcpy(tmp, d_tmp, tmp_size, cudaMemcpyDeviceToHost);
    // for (int i = 0; i < len; i++) {
    //     printf("%lf\n", tmp[i]);
    // }
    // free(tmp);

//--

    sum = res[0];

    cudaFree(d_body);
    cudaFree(d_tmp);
    cudaFree(d_res);

    (*env_).ReleaseLongArrayElements(arr_, body, 0);
    return sum;
  }

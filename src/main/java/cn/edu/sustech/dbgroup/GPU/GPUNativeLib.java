package cn.edu.sustech.dbgroup.GPU;

public class GPUNativeLib {
    public static String libName = "GPUDLL";
    public GPUNativeLib(){
        System.loadLibrary(libName);
    }
    // return sum of sqrt(sin(abs(cos(arr_i))))
    public native double calculateArrayComplexSum(long[] arr);
}

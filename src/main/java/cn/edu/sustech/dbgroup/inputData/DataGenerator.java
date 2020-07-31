package cn.edu.sustech.dbgroup.inputData;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataGenerator {

    public static int dataAmount = 100000000;
    public static int windowSize = 10000;
    public static File outputFile = new File("src\\main\\resources\\inputData.txt");


    public static void main(String[] args) throws IOException {
        if (!outputFile.exists()) {
            outputFile.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(outputFile.getAbsoluteFile());
        StringBuffer result = new StringBuffer("");
        Random random = new Random();
        for (int i = 0; i < dataAmount; i++) {
//            int nextNumber = random.nextInt()%1000;
            int nextNumber = 1;
            result.append(nextNumber);
            result.append("\n");
            if (i % 1000000 == 0) {
                fileWriter.write(result.toString());
                result = new StringBuffer("");
            }
        }
        fileWriter.write(result.toString());
        fileWriter.close();
    }
}

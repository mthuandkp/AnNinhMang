package Utils;

import java.util.Arrays;
import java.util.List;
import java.io.File;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FileUtils {
    public static void delete(String fileInput) {
        File file = new File(fileInput);
        file.delete();
    }

    public static List<String> readAllFileinFolder(List<String> list,File file){
        if (file.isDirectory()) { // Nếu là thư mục thì tiếp tục, ko thì là tập tin -> dừng

            File[] children = file.listFiles();
            for (File child : children) {
                readAllFileinFolder(list,child); // Gọi đệ quy
            }
        } else {
            list.add(file.getAbsolutePath());
        }
        return list;
    }

    public static List<String> fillterFile(List<String> fileList) {
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

        return fileList.stream().filter(path-> predicate.test(path)).collect(Collectors.toList());
    }

}
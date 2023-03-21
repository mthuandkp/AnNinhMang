package src;

import java.util.Arrays;
import java.util.List;
import java.io.File;

public class FileUtils {
    public static void delete(String fileInput) {
        File file = new File(fileInput);

        if (file.delete()) {
            System.out.println("File deleted successfully");
        } else {
            System.out.println("Failed to delete the file");
        }
    }

    public static List<String> readAllFileinFolder(List<String> list,File file){
        if (file.isDirectory()) { // Nếu là thư mục thì tiếp tục, ko thì là tập tin -> dừng

            File[] children = file.listFiles();
            for (File child : children) {
                readAllFileinFolder(list,child); // Gọi đệ quy
            }
        } else {
            list.add(file.getName());
        }
        return list;
    }

    //in ra nhìn cho đẹp
    public String getPadding(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= level; i++) {
            sb.append("    "); // Thêm dấu tab.
        }
        return sb.toString();
    }
}

package src;

import java.util.Arrays;
import java.util.List;
import java.io.File;

public class FileUtils {
    public List<String> readAllFileinFolder(File file, int level){
        if (file.isDirectory()) { // Nếu là thư mục thì tiếp tục, ko thì là tập tin -> dừng
            
            System.out.println(getPadding(level) + " - " + file.getName());
            File[] children = file.listFiles();
            for (File child : children) {
                this.readAllFileinFolder(child, level + 1); // Gọi đệ quy
            }
        } else {
            System.out.println(getPadding(level) + " + " + file.getName());
        }
        return null;
    }

    //in ra nhìn cho đẹp
    public String getPadding(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= level; i++) {
            sb.append("    "); // Thêm dấu tab.
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        FileUtils e=new FileUtils();
        File dir = new File("");
        String currentDirectory = dir.getAbsolutePath();
        dir = new File(currentDirectory);

        
        e.readAllFileinFolder(dir, 0);
    }
}

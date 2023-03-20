package src;

public class App {
    public static void main(String[] args) {
        String fileInput = "src/file_demo/test.pdf";
        String fileOutput = "src/file_demo/test.pdf.MãHoá";

        try {
            AES.encrypt(fileOutput,fileInput);
            FileUtils.delete(fileOutput);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

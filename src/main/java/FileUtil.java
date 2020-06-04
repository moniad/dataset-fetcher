import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class FileUtil {
    public static void saveDataToFile(Map<String, String> descriptionsByBreed) throws IOException {
        File file = new File("dataset.csv");
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        insertHeading(bufferedWriter);

        descriptionsByBreed.values().forEach(description -> {
            try {
                insertRow(bufferedWriter, description);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bufferedWriter.close();
    }

    private static void insertRow(BufferedWriter bufferedWriter, String description) throws IOException {
        bufferedWriter.write("pies,\"" + description + "\"");
        bufferedWriter.newLine();
    }

    private static void insertHeading(BufferedWriter bw) throws IOException {
        bw.write("rasa,opis");
        bw.newLine();
    }
}

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FileUtil {
    private static final String DATASET_FILE_NAME = "dataset.csv";

    public static void saveDataToFile(List<Map.Entry<String, String>> descriptionsByBreed) throws IOException {
        File file = new File(DATASET_FILE_NAME);
        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        insertHeading(bufferedWriter);
        descriptionsByBreed.forEach(pair -> {
            try {
                insertRow(bufferedWriter, pair);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bufferedWriter.close();
    }

    private static void insertRow(BufferedWriter bufferedWriter, Map.Entry<String, String> pair) throws IOException {
        bufferedWriter.write(pair.getKey() + ",\"" + pair.getValue() + "\"");
        bufferedWriter.newLine();
    }

    private static void insertHeading(BufferedWriter bw) throws IOException {
        bw.write("rasa,opis");
        bw.newLine();
    }
}

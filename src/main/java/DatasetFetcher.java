import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DatasetFetcher {
    private final String WAMIZ_URL = "https://wamiz.pl";
    private final String DOGS_CONNECTION_URL = "https://wamiz.pl/pies/rasy";
    private final int DATASET_SIZE = 8;

    public Map<String, String> fetchData() throws IOException {
        Document doc = Jsoup.connect(DOGS_CONNECTION_URL).get();
        List<String> breedUrls = fetchBreedUrls(doc);

        Map<String, String> descriptionsByBreed = new HashMap<>();

        breedUrls.forEach(breedUrl -> {
            try {
                Document breedDocument = Jsoup.connect(breedUrl).get();
                String breedName = fetchBreedName(breedDocument);
                String description = fetchBreedDescription(breedDocument);
                descriptionsByBreed.put(breedName, description);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return descriptionsByBreed;
    }

    private String fetchBreedDescription(Document breedDocument) {
        return breedDocument
                .getElementsByClass("breed-wysiwyg-content")
                .get(0)
                .select("p")
                .text();
    }

    private String fetchBreedName(Document breedDocument) {
        return breedDocument
                .getElementsByClass("breed-title")
                .text();
    }

    private List<String> fetchBreedUrls(Document doc) {
        return doc.getElementsByClass("listView-list--homepageBreed")
                .get(0)
                .getElementsByClass("listView-item--homepageBreed")
                .subList(0, DATASET_SIZE)
                .stream()
                .map(el -> WAMIZ_URL.concat(el.getElementsByClass("listView-item-title--homepageBreed").get(0)
                        .attr("href")))
                .collect(Collectors.toList());
    }
}

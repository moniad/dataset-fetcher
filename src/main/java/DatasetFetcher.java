import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DatasetFetcher {
    private final String WAMIZ_URL = "https://wamiz.pl";
    private final String BREED = "/rasy";
    private final String DOGS_CONNECTION_URL = WAMIZ_URL + "/pies" + BREED;
    private final String CATS_CONNECTION_URL = WAMIZ_URL + "/kot" + BREED;
    private final int DATASET_SIZE = 8;

    public List<Map.Entry<String, String>> fetchData() throws IOException {
        List<Map.Entry<String, String>> descriptionsByBreed = new ArrayList<>();
        addDescriptions(DogOrCat.DOG, descriptionsByBreed);
        addDescriptions(DogOrCat.CAT, descriptionsByBreed);

        return descriptionsByBreed;
    }

    private void addDescriptions(DogOrCat dogOrCat, List<Map.Entry<String, String>> descriptionsByBreed) throws IOException {
        String connectionUrl = dogOrCat.equals(DogOrCat.DOG) ? DOGS_CONNECTION_URL : CATS_CONNECTION_URL;
        Document doc = Jsoup.connect(connectionUrl).get();

        List<String> breedUrls = fetchBreedUrls(doc);
        String breedName = dogOrCat.getBreedName();
        breedUrls.forEach(breedUrl -> {
            try {
                Map.Entry<String, String> pair = createMapEntry(breedUrl, breedName);
                descriptionsByBreed.add(pair);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Map.Entry<String, String> createMapEntry(String breedUrl, String breedName) throws IOException {
        Document breedDocument = Jsoup.connect(breedUrl).get();
        String description = fetchBreedDescription(breedDocument);
        return new AbstractMap.SimpleEntry<>(breedName, description);
    }

    private enum DogOrCat {
        DOG("pies"), CAT("kot");

        private final String breedName;

        DogOrCat(String breedName) {
            this.breedName = breedName;
        }

        public String getBreedName() {
            return breedName;
        }
    }

    private String fetchBreedDescription(Document breedDocument) {
        return breedDocument
                .getElementsByClass("breed-wysiwyg-content")
                .get(0)
                .select("p")
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

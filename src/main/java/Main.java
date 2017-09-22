import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;
import java.util.Map;


public class Main {

    public static void main(String[] args) throws IOException {
        process();
    }

    static void process() throws IOException {
        System.out.println("Start parsing...\n");
        String categoryUrl = "https://rozetka.com.ua/all-tv/c80037";
        RozetkaCategoryParser parser = new RozetkaCategoryParser(categoryUrl);

        String path = "data/";

        ArrayList<HashMap<String, String>> products = parser.getAllProducts();
        int maxReviewsCounter = -1;
        String maxReviewedProductName = "is not defined";
        System.out.println("All products are parsed\n");
        System.out.println("Start fetching details and writing to the file");
        for (HashMap<String, String> product : products) {
            System.out.println("Processing: " + product.get("title"));

            if(!product.get("reviews_count").equals("") && Integer.parseInt(product.get("reviews_count")) > maxReviewsCounter){
                maxReviewedProductName = product.get("title");
            }

            String filename = String.format("%s/%s.csv",
                    path, product.get("title").replace(" ", "-").replace("/", "-"));

            FileWriter writer;
            writer = new FileWriter(filename, true);  //True = Append to file, false =

            writer.write(product.get("title"));
            writer.write("\r\n\r\n\r\n");
            // Write Characteristics
            for (Map.Entry<String, String> entry : parser.getProductCharacteristics(product.get("link")).entrySet()) {
                writer.write("\"" + entry.getKey() + "\"");
                writer.write(",");
                writer.write("\"" + entry.getValue() + "\"");
                writer.write("\r\n");
            }

            // spacing between blocks of content
            writer.write("\r\nReviews:\r\n");

            // Write Reviews
            for (HashMap<String, String> review : parser.getAllProductReviews(product.get("link"))) {
                writer.write("Review:\r\n");
                for (Map.Entry<String, String> entry : review.entrySet()){
                    if(!entry.getValue().equals("")){
                        writer.write("\"" + entry.getKey() + "\"");
                        writer.write(",");
                        writer.write("\"" + entry.getValue() + "\"");
                        writer.write("\r\n");
                    }
                }
                writer.write("\r\n");
            }

            writer.close();

        }

        System.out.println("Parsing is successfully done!");

        // research
        FileWriter writer;
        String filename = String.format("%s/%s.txt", path, "result");
        writer = new FileWriter(filename, false);
        writer.write("The most reviewed product: " + maxReviewedProductName);
        writer.close();

        System.out.println("The most reviewed product: " + maxReviewedProductName);
    }

}

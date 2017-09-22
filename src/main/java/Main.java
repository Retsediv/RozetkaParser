import com.opencsv.CSVWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileWriter;


public class Main {

    public static void main(String[] args) throws IOException {
        process();
    }

    static void process() throws IOException {
        String categoryUrl = "https://rozetka.com.ua/all-tv/c80037";
        RozetkaCategoryParser parser = new RozetkaCategoryParser(categoryUrl);

        String path = "data/";

        ArrayList<HashMap<String, String>> products = parser.getAllProducts();
        for (HashMap<String, String> product : products) {

        }

        String csv = "output.csv";
        CSVWriter writer = new CSVWriter(new FileWriter(csv));
        String [] country = "India#China#United States".split("#");
        writer.writeNext(country);

        writer.close();

    }

}

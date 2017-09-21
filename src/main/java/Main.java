import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) throws IOException {
        process();
    }

    static void process() throws IOException {
        String categoryUrl = "https://rozetka.com.ua/all-tv/c80037";
        RozetkaCategoryParser parser = new RozetkaCategoryParser(categoryUrl);

//        ArrayList<HashMap<String, String>> products = this.getProductsOnPage(1);
//        System.out.println(this.getProductFullDesctiption(products.get(0).get("link")));
    }

}

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class RozetkaCategoryParser {

    /**
     * Link to specific category on rozetka.com
     */
    private String categoryUrl;

    /**
     * Raw grabber html page
     */
    private Document doc;

    /**
     * Init class and set categoryUrl
     *
     * @param categoryUrl
     */
    public RozetkaCategoryParser(String categoryUrl) throws IOException {
        this.categoryUrl = categoryUrl;
        this.doc = Jsoup.connect(categoryUrl).get();
    }

    /**
     * categoryUrl getter
     *
     * @return categoryUrl
     */
    public String getCategoryUrl() {
        return categoryUrl;
    }

    /**
     * categoryUrl setter
     *
     * @param categoryUrl Link to category on rozetka website
     */
    public void setCategoryUrl(String categoryUrl) {
        this.categoryUrl = categoryUrl;
    }


    /**
     * Get number of pages in the catalog by parsing html
     * (fetch relevant block and get the last pagination item)
     *
     * @param url String
     * @return pagesCount
     * Integer
     * @throws IOException
     */
    public int getNumberOfPages(String url) throws IOException {
        // get all pagination elements
        Elements paginationElement = this.doc.select(".paginator-catalog .paginator-catalog-l-i span");
        // fetch a value of the last element
        int pagesCount = Integer.parseInt(paginationElement.last().text());

        return pagesCount;
    }

    public void getProductFullDesctiption(String url) {

    }

    public void getProductReviews(String url) {

    }

    /**
     * Get all products of category on all pages
     *
     * @return
     * @throws IOException
     */
    public ArrayList<HashMap<String, String>> getAllProducts() throws IOException {
        int pagesCount = this.getNumberOfPages(this.categoryUrl);

        return getAllProducts(pagesCount);
    }

    /**
     * Get all products of category on all pages
     *
     * @param pagesCount
     * @return
     */
    public ArrayList<HashMap<String, String>> getAllProducts(int pagesCount) {
        ArrayList<HashMap<String, String>> allProducts = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < pagesCount; i++) {
            ArrayList<HashMap<String, String>> productsOnPage = this.getProductsOnPage(i + 1);
            allProducts.addAll(productsOnPage);
        }

        return allProducts;
    }

    /**
     * Parse single page of products, get all of them here and parse data
     *
     * @param pageNumber Number of page category we want to parse
     * @return Array of HashMaps
     */
    public ArrayList<HashMap<String, String>> getProductsOnPage(int pageNumber) {
        String url = String.format(this.categoryUrl + "/page=%s", pageNumber);
        Document page = Jsoup.parse(url);

        ArrayList<HashMap<String, String>> products = new ArrayList<HashMap<String, String>>();
        Elements rawItems = this.doc.select(".g-i-tile-catalog");

        for (Element item : rawItems) {
            HashMap<String, String> shortDescription = new HashMap<String, String>();

            shortDescription.put("title", item.select(".g-i-tile-i-title a").text());
            shortDescription.put("link", item.select(".g-i-tile-i-title a").attr("href"));
            shortDescription.put("image", item.select(".g-i-tile-i-image img").attr("src"));
            shortDescription.put("reviews_count", item.select(".g-rating > a").attr("data-count"));
            shortDescription.put("reviews_rate", item.select(".g-rating a .g-rating-stars-i").attr("style"));

            products.add(shortDescription);
        }

        return products;
    }


    public void process() {

        ArrayList<HashMap<String, String>> products = this.getProductsOnPage(1);
        System.out.println(products.size());
        System.out.println(Arrays.toString(products.toArray()));
    }
}

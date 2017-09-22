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

    public HashMap<String, Cloneable> getProductFullDesctiption(String url) throws IOException {
        HashMap<String, Cloneable> fullDescription = new HashMap<String, Cloneable>();
        fullDescription.put("characteristics", this.getProductCharacteristics(url));
        System.out.println(this.getProductCharacteristics(url));
        fullDescription.put("reviews", this.getAllProductReviews(url));

        return fullDescription;
    }

    public HashMap<String, String> getProductCharacteristics(String url) throws IOException {
        url = url + "characteristics";
        Document productPage = Jsoup.connect(url).get();
        Elements characteristicsElements = productPage.select(".pp-characteristics-tab-i");
        HashMap<String, String> characteristics = new HashMap<String, String>();

        for(Element characteristicsElement: characteristicsElements){
            characteristics.put(
                    characteristicsElement.select(".pp-characteristics-tab-i-title span").text(),
                    characteristicsElement.select(".pp-characteristics-tab-i-field a").text());
        }

        return characteristics;
    }

    public ArrayList<HashMap<String, String>> getAllProductReviews(String url) throws IOException {
        Document doc = Jsoup.connect(url + "comments").get();

        int numberOfPages = 0;
        if(!doc.select(".paginator-catalog-l-i:last-child span").text().equals("")){
            numberOfPages = Integer.parseInt(doc.select(".paginator-catalog-l-i:last-child span").text());
        }

        ArrayList<HashMap<String, String>> reviews = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < numberOfPages; i++) {
            ArrayList<HashMap<String, String>> reviewsOnPage = this.getProductReviewsOnPage(url, i+1);
            reviews.addAll(reviewsOnPage);
        }

        return reviews;
    }

    public ArrayList<HashMap<String, String>> getProductReviewsOnPage(String url, int pageCount) throws IOException {
        url = url + "comments/page=" + pageCount;
        Document reviewPage = Jsoup.connect(url).get();
        Elements rawReviews = reviewPage.select(".pp-review-i");
        ArrayList<HashMap<String, String>> reviews = new ArrayList<HashMap<String, String>>();

        for (Element rawReview: rawReviews){
            HashMap<String, String> review = new HashMap<String, String>();
            review.put("author", rawReview.select(".pp-review-author .pp-review-author-name").text());
            review.put("status", rawReview.select(".pp-review-author .pp-review-buyer-note").text());
            review.put("date", rawReview.select(".pp-review-date .pp-review-date-text").text());
            review.put("rate", rawReview.select(".g-rating-stars-i").attr("content"));
            review.put("full_review", rawReview.select(".pp-review-text > div:first-child").text());
            review.put("full_review", rawReview.select(".pp-review-text > div:first-child").text());
            review.put("pros", rawReview.select(".pp-review-text > div:nth-child(2)").text());
            review.put("cons", rawReview.select(".pp-review-text > div:nth-child(3)").text());

            reviews.add(review);
        }

        return reviews;
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
}
